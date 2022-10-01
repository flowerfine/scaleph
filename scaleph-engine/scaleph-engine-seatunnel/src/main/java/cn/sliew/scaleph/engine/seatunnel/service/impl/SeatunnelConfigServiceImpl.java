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

package cn.sliew.scaleph.engine.seatunnel.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.enums.JobAttrTypeEnum;
import cn.sliew.scaleph.common.enums.JobStepTypeEnum;
import cn.sliew.scaleph.common.exception.Rethrower;
import cn.sliew.scaleph.common.param.PropertyUtil;
import cn.sliew.scaleph.core.di.service.dto.DiJobAttrDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobLinkDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobStepDTO;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConfigService;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConnectorService;
import cn.sliew.scaleph.meta.service.MetaDatasourceService;
import cn.sliew.scaleph.meta.service.dto.MetaDatasourceDTO;
import cn.sliew.scaleph.plugin.datasource.DatasourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.JobNameProperties;
import cn.sliew.scaleph.system.service.vo.DictVO;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static cn.sliew.scaleph.common.enums.SeatunnelNativeFlinkPluginEnum.*;
import static cn.sliew.scaleph.engine.seatunnel.service.util.GraphConstants.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties.RESULT_TABLE_NAME;
import static cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties.SOURCE_TABLE_NAME;

@Slf4j
@Service
public class SeatunnelConfigServiceImpl implements SeatunnelConfigService {

    @Autowired
    private MetaDatasourceService metaDatasourceService;
    @Autowired
    private SeatunnelConnectorService seatunnelConnectorService;

    private static final Map<String, String> JOB_STEP_MAP = new HashMap<>();
    private static final Map<String, String> PLUGIN_MAP = new HashMap<>();

    static {
        //init job step map
        JOB_STEP_MAP.put("source-table", JDBC_SOURCE.getValue());
        JOB_STEP_MAP.put("source-mock", FAKE_SOURCE.getValue());
        JOB_STEP_MAP.put("source-mockStream", FAKE_STREAM_SOURCE.getValue());
        JOB_STEP_MAP.put("source-kafka", KAFKA_SOURCE.getValue());
        JOB_STEP_MAP.put("source-druid", DRUID_SOURCE.getValue());
        JOB_STEP_MAP.put("sink-table", JDBC_SINK.getValue());
        JOB_STEP_MAP.put("sink-console", CONSOLE_SINK.getValue());
        JOB_STEP_MAP.put("sink-kafka", KAFKA_SINK.getValue());
        JOB_STEP_MAP.put("sink-doris", DORIS_SINK.getValue());
        JOB_STEP_MAP.put("sink-clickhouse", CLICKHOUSE_SINK.getValue());
        JOB_STEP_MAP.put("sink-elasticsearch", ELASTICSEARCH_SINK.getValue());
        JOB_STEP_MAP.put("sink-druid", DRUID_SINK.getValue());

        //init plugin map
        PLUGIN_MAP.put("source-table", "jdbc");
        PLUGIN_MAP.put("sink-table", "jdbc");
        PLUGIN_MAP.put("source-mock", "fake");
        PLUGIN_MAP.put("source-mockStream", "fake");
        PLUGIN_MAP.put("sink-console", "console");
        PLUGIN_MAP.put("sink-kafka", "kafka");
        PLUGIN_MAP.put("source-kafka", "kafka");
        PLUGIN_MAP.put("sink-doris", "doris");
        PLUGIN_MAP.put("sink-clickhouse", "clickhouse");
        PLUGIN_MAP.put("sink-elasticsearch", "elasticsearch");
        PLUGIN_MAP.put("sink-druid", "druid");
        PLUGIN_MAP.put("source-druid", "druid");
//        PLUGIN_MAP.put("source-csv", "file");
//        PLUGIN_MAP.put("sink-csv", "file");
    }

    @Override
    public String buildConfig(DiJobDTO diJobDTO) {
        ObjectNode conf = JacksonUtil.createObjectNode();
        conf.set("env", buildEnv(diJobDTO));

        MutableGraph<ObjectNode> graph = buildGraph(diJobDTO);
        ArrayNode sourceConf = JacksonUtil.createArrayNode();
        ArrayNode transformConf = JacksonUtil.createArrayNode();
        ArrayNode sinkConf = JacksonUtil.createArrayNode();
        conf.set("source", sourceConf);
        conf.set("transform", transformConf);
        conf.set("sink", sinkConf);
        //source and result table name
        graph.edges().forEach(edge -> {
            ObjectNode source = edge.source();
            ObjectNode target = edge.target();
            String nodeId = source.get(NODE_ID).asText();
            source.put(RESULT_TABLE_NAME.getName(), TABLE_PREFIX + nodeId);
            target.put(SOURCE_TABLE_NAME.getName(), TABLE_PREFIX + nodeId);
        });

        graph.nodes().forEach(node -> {
            String pluginName = node.get(PLUGIN_NAME).asText();
            String nodeType = node.get(NODE_TYPE).asText();
            if (JobStepTypeEnum.SOURCE.getValue().equals(nodeType)) {
                sourceConf.add(node);
            } else if (JobStepTypeEnum.TRANSFORM.getValue().equals(nodeType)) {
                transformConf.add(node);
            } else if (JobStepTypeEnum.SINK.getValue().equals(nodeType)) {
                sinkConf.add(node);
            }
        });
        return conf.toPrettyString();
    }

    private ObjectNode buildEnv(DiJobDTO job) {
        ObjectNode env = JacksonUtil.createObjectNode();
        env.put(JobNameProperties.JOB_NAME.getName(), job.getJobCode());
        List<DiJobAttrDTO> jobAttrList = job.getJobAttrList();
        if (CollectionUtils.isEmpty(jobAttrList)) {
            return env;
        }
        for (DiJobAttrDTO attr : jobAttrList) {
            if (JobAttrTypeEnum.JOB_PROP.getValue().equals(attr.getJobAttrType().getValue())) {
                env.put(attr.getJobAttrKey(), attr.getJobAttrValue());
            }
        }
        return env;
    }


    private MutableGraph<ObjectNode> buildGraph(DiJobDTO diJobDTO) {
        MutableGraph<ObjectNode> graph = GraphBuilder.directed().build();
        List<DiJobStepDTO> jobStepList = diJobDTO.getJobStepList();
        List<DiJobLinkDTO> jobLinkList = diJobDTO.getJobLinkList();
        if (CollectionUtil.isNotEmpty(jobStepList) && CollectionUtil.isNotEmpty(jobLinkList)) {
            Map<String, ObjectNode> stepMap = new HashMap<>();
            for (DiJobStepDTO step : jobStepList) {
                String name = JOB_STEP_MAP.get(step.getStepType().getValue() + "-" + step.getStepName());
                Properties properties = mergeJobAttrs(step);
                SeaTunnelConnectorPlugin connector = seatunnelConnectorService.newConnector(name, properties);
                ObjectNode stepConf = connector.createConf();
                stepConf.put(PLUGIN_NAME, name);
                stepConf.put(NODE_TYPE, step.getStepType().getValue());
                stepConf.put(NODE_ID, step.getId());
                stepMap.put(step.getStepCode(), stepConf);
                graph.addNode(stepConf);
            }
            jobLinkList.forEach(link -> {
                String from = link.getFromStepCode();
                String to = link.getToStepCode();
                graph.putEdge(stepMap.get(from), stepMap.get(to));
            });
        }
        return graph;
    }

    private Properties mergeJobAttrs(DiJobStepDTO step) {
        Properties properties = new Properties();
        Map<String, Object> stepAttrList = step.getStepAttrs();
        if (CollectionUtil.isNotEmpty(stepAttrList)) {
            stepAttrList.forEach((k, v) -> {
                if (v != null && !"".equals(v)) {
                    if (Constants.JOB_STEP_ATTR_DATASOURCE.equals(k)) {
                        DictVO dsAttr = JSONUtil.toBean(String.valueOf(v), DictVO.class);
                        MetaDatasourceDTO datasourceDTO =
                                metaDatasourceService.selectOne(Long.parseLong(dsAttr.getValue()), false);
                        Set<PluginInfo> pluginInfoSet = this.metaDatasourceService.getAvailableDataSources();
                        for (PluginInfo pluginInfo : pluginInfoSet) {
                            if (pluginInfo.getName().equalsIgnoreCase(datasourceDTO.getDatasourceType().getValue())) {
                                try {
                                    Class<?> clazz = Class.forName(pluginInfo.getClassname());
                                    DatasourcePlugin<?> dsPlugin = (DatasourcePlugin<?>) clazz.newInstance();
                                    dsPlugin.setAdditionalProperties(PropertyUtil.mapToProperties(datasourceDTO.getAdditionalProps()));
                                    dsPlugin.configure(PropertyContext.fromMap(datasourceDTO.getProps()));
                                    properties.putAll(dsPlugin.getProperties());
                                } catch (ClassNotFoundException | IllegalAccessException |
                                         InstantiationException e) {
                                    Rethrower.throwAs(e);
                                }
                            }
                        }
                    } else {
                        properties.put(k, v);
                    }
                }
            });
        }
        return properties;
    }

    public String getSeatunnelPluginTag(String stepType, String stepName) {
        return PLUGIN_MAP.get(stepType + "-" + stepName);
    }
}
