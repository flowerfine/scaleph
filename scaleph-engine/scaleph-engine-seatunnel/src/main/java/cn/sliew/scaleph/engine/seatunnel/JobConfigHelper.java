/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.engine.seatunnel;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.enums.DataSourceTypeEnum;
import cn.sliew.scaleph.common.enums.JobAttrTypeEnum;
import cn.sliew.scaleph.common.enums.JobStepTypeEnum;
import cn.sliew.scaleph.common.exception.Rethrower;
import cn.sliew.scaleph.core.di.service.dto.*;
import cn.sliew.scaleph.meta.service.MetaDatasourceService;
import cn.sliew.scaleph.meta.service.dto.MetaDatasourceDTO;
import cn.sliew.scaleph.plugin.datasource.jdbc.JDBCDataSourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.system.service.vo.DictVO;
import cn.sliew.scaleph.system.util.PropertyUtil;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static cn.sliew.scaleph.common.enums.SeatunnelNativeFlinkPluginEnum.JDBC_SINK;
import static cn.sliew.scaleph.common.enums.SeatunnelNativeFlinkPluginEnum.JDBC_SOURCE;

/**
 * seatunnel job config helper
 *
 * @author gleiyu
 */
@Slf4j
@Component
@Deprecated
public class JobConfigHelper {

    private static final Map<String, String> JOB_STEP_MAP = new HashMap<>();

    static {
        //init job step map
        JOB_STEP_MAP.put("sink-table", JDBC_SINK.getValue());
        JOB_STEP_MAP.put("source-table", JDBC_SOURCE.getValue());
    }

    @Autowired
    private MetaDatasourceService metaDatasourceService;

    /**
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
        envMap.put(Constants.JOB_NAME, job.getJobCode());
        if (CollectionUtil.isNotEmpty(jobAttrList)) {
            jobAttrList.stream()
                    .filter(attr -> JobAttrTypeEnum.JOB_PROP.getValue()
                            .equals(attr.getJobAttrType().getValue()))
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
                String name = JOB_STEP_MAP.get(
                        StrUtil.join("-", step.getStepType().getValue(), step.getStepName()));
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
                        MetaDatasourceDTO metaDatasourceDTO = this.metaDatasourceService.selectOne(dsAttr.getValue(), false);
                        if (DataSourceTypeEnum.JDBC.getValue().equals(metaDatasourceDTO.getDatasourceType().getValue())
                                || DataSourceTypeEnum.MYSQL.getValue().equals(metaDatasourceDTO.getDatasourceType().getValue())
                                || DataSourceTypeEnum.ORACLE.getValue().equals(metaDatasourceDTO.getDatasourceType().getValue())
                                || DataSourceTypeEnum.POSTGRESQL.getValue().equals(metaDatasourceDTO.getDatasourceType().getValue())
                        ) {
                            Set<PluginInfo> pluginInfoSet = this.metaDatasourceService.getAvailableDataSources();
                            for (PluginInfo pluginInfo : pluginInfoSet) {
                                if (pluginInfo.getName().equalsIgnoreCase(metaDatasourceDTO.getDatasourceType().getValue())) {
                                    Class clazz = Class.forName(pluginInfo.getClassname());
                                    JDBCDataSourcePlugin datasource = (JDBCDataSourcePlugin) clazz.newInstance();
                                    datasource.configure(PropertyContext.fromMap(metaDatasourceDTO.getProps()));
                                    datasource.setAdditionalProperties(PropertyUtil.mapToProperties(metaDatasourceDTO.getAdditionalProps()));
                                    map.put(Constants.JOB_STEP_ATTR_USERNAME, datasource.getUsername());
                                    map.put(Constants.JOB_STEP_ATTR_PASSWORD, datasource.getPassword());
                                    map.put(Constants.JOB_STEP_ATTR_DRIVER, datasource.getDriverClassNmae());
                                    map.put(Constants.JOB_STEP_ATTR_URL, datasource.getJdbcUrl());
                                }
                            }
                        } else if (DataSourceTypeEnum.KAFKA.getValue().equals(metaDatasourceDTO.getDatasourceType().getValue())) {
                            //todo
                        }
                    } catch (Exception e) {
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

}



