package com.liyu.breeze.engine.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.liyu.breeze.common.constant.Constants;
import com.liyu.breeze.common.enums.JobAttrTypeEnum;
import com.liyu.breeze.common.enums.JobStepTypeEnum;
import com.liyu.breeze.common.exception.CustomException;
import com.liyu.breeze.common.exception.Rethrower;
import com.liyu.breeze.meta.util.JdbcUtil;
import com.liyu.breeze.service.dto.di.*;
import com.liyu.breeze.service.dto.meta.DataSourceMetaDTO;
import com.liyu.breeze.service.meta.DataSourceMetaService;
import com.liyu.breeze.service.vo.DictVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * seatunnel job config helper
 *
 * @author gleiyu
 */
@Slf4j
@Component
public class JobConfigHelper {

    @Autowired
    private DataSourceMetaService dataSourceMetaService;

    private static Map<String, String> JOB_STEP_MAP;

    static {
        JOB_STEP_MAP = new HashMap<>();
        JOB_STEP_MAP.put("sink-table", "JdbcSink");
        JOB_STEP_MAP.put("source-table", "JdbcSource");
    }

    /**
     * 1. 作业 job
     * 2. 作业属性  jobAttr  作业变量${}   env配置信息
     * 3. 步骤  step
     * 4. 步骤属性  steAttr
     * 5. 作业连线 job Link  判断source table and result table
     *
     * @param job
     * @return
     */
    public String buildJob(DiJobDTO job) {
        if (job == null) {
            return null;
        }
        JobConfig conf = new JobConfig();
        buildEnv(job, conf);
        buildSteps(job, conf);
        return JSONUtil.toJsonPrettyStr(conf);
    }

    private void buildEnv(DiJobDTO job, JobConfig conf) {
        List<DiJobAttrDTO> jobAttrList = job.getJobAttrList();
        Map<String, String> envMap = conf.getEnv();
        if (CollectionUtil.isNotEmpty(jobAttrList)) {
            jobAttrList.stream()
                    .filter(attr -> JobAttrTypeEnum.JOB_PROP.getValue().equals(attr.getJobAttrType().getValue()))
                    .forEach(attr -> {
                        envMap.put(attr.getJobAttrKey(), attr.getJobAttrValue());
                    });
        }
    }

    private void buildSteps(DiJobDTO job, JobConfig conf) {
        MutableGraph<Map<String, String>> graph = GraphBuilder.directed().build();
        String nodeType = "node_type";
        String nodeId = "node_id";
        String tablePrefix = "table_";
        String sourceTable = "source_table_name";
        String resultTable = "result_table_name";
        String pluginName = "plugin_name";
        List<Map<String, String>> sourceStepList = conf.getSource();
        List<Map<String, String>> transStepList = conf.getTransform();
        List<Map<String, String>> sinkStepList = conf.getSink();
        List<DiJobStepDTO> jobStepList = job.getJobStepList();
        List<DiJobLinkDTO> jobLinkList = job.getJobLinkList();
        if (CollectionUtil.isNotEmpty(jobStepList) && CollectionUtil.isNotEmpty(jobLinkList)) {
            Map<String, Map<String, String>> stepMap = new HashMap<>();
            jobStepList.forEach(step -> {
                String name = JOB_STEP_MAP.get(StrUtil.join("-", step.getStepType().getValue(), step.getStepName()));
                Map<String, String> map = new HashMap<>();
                map.put(pluginName, name);
                map.put(nodeType, step.getStepType().getValue());
                map.put(nodeId, String.valueOf(step.getId()));
                if (!JobStepTypeEnum.SINK.getValue().equals(step.getStepType().getValue())) {
                    map.put(resultTable, tablePrefix + step.getId());
                }
                buildStep(step, map);
                stepMap.put(step.getStepCode(), map);
            });
            jobLinkList.forEach(link -> {
                String from = link.getFromStepCode();
                String to = link.getToStepCode();
                graph.putEdge(stepMap.get(from), stepMap.get(to));
            });
            //generate source table name
            graph.edges().forEach(edge -> {
                Map<String, String> source = edge.source();
                Map<String, String> target = edge.target();
                if (source.containsKey(resultTable)) {
                    target.put(sourceTable, source.get(resultTable));
                }
            });
            graph.nodes().forEach(node -> {
                if (JobStepTypeEnum.SOURCE.getValue().equals(node.get(nodeType))) {
                    sourceStepList.add(node);
                } else if (JobStepTypeEnum.TRANSFORM.getValue().equals(node.get(nodeType))) {
                    transStepList.add(node);
                } else if (JobStepTypeEnum.SINK.getValue().equals(node.get(nodeType))) {
                    sinkStepList.add(node);
                }
            });
        }
    }

    private void buildStep(DiJobStepDTO step, Map<String, String> map) {
        List<DiJobStepAttrDTO> stepAttrList = step.getJobStepAttrList();
        if (CollectionUtil.isNotEmpty(stepAttrList)) {
            stepAttrList.forEach(attr -> {
                //resolve datasource
                if (Constants.JOB_STEP_ATTR_DATASOURCE.equals(attr.getStepAttrKey())) {
                    try {
                        DictVO dsAttr = JSONUtil.toBean(attr.getStepAttrValue(), DictVO.class);
                        DataSourceMetaDTO dsInfo = this.dataSourceMetaService.selectOne(dsAttr.getValue());
                        map.put(Constants.JOB_STEP_ATTR_USERNAME, dsInfo.getUserName());
                        map.put(Constants.JOB_STEP_ATTR_PASSWORD, Base64.decodeStr(dsInfo.getPassword()));
                        map.put(Constants.JOB_STEP_ATTR_DRIVER, JdbcUtil.getDriver(dsInfo));
                        map.put(Constants.JOB_STEP_ATTR_URL, JdbcUtil.getUrl(dsInfo));
                    } catch (CustomException e) {
                        log.debug(e.getMessage());
                        Rethrower.throwAs(e);
                    }
                } else {
                    map.put(attr.getStepAttrKey(), attr.getStepAttrValue());
                }
            });
        }
    }

    @Data
    public static class JobConfig {
        private Map<String, String> env;
        private List<Map<String, String>> source;
        private List<Map<String, String>> transform;
        private List<Map<String, String>> sink;

        JobConfig() {
            this.env = new HashMap<>();
            this.source = new ArrayList<>();
            this.transform = new ArrayList<>();
            this.sink = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.io.tmpdir"));
    }

}



