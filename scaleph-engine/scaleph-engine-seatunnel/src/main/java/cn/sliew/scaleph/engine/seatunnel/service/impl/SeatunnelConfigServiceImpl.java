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
import cn.sliew.scaleph.core.di.service.dto.*;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConfigService;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConnectorService;
import cn.sliew.scaleph.meta.service.MetaDatasourceService;
import cn.sliew.scaleph.meta.service.dto.MetaDatasourceDTO;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeatunnelNativeFlinkConnector;
import cn.sliew.scaleph.plugin.seatunnel.flink.common.JobNameProperties;
import cn.sliew.scaleph.system.service.vo.DictVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static cn.sliew.scaleph.engine.seatunnel.service.util.GraphConstants.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.common.CommonProperties.RESULT_TABLE_NAME;
import static cn.sliew.scaleph.plugin.seatunnel.flink.common.CommonProperties.SOURCE_TABLE_NAME;

@Slf4j
@Service
public class SeatunnelConfigServiceImpl implements SeatunnelConfigService {

    @Autowired
    private MetaDatasourceService metaDatasourceService;
    @Autowired
    private SeatunnelConnectorService seatunnelConnectorService;

    @Override
    public String buildConfig(DiJobDTO diJobDTO) {
        ObjectNode conf = JacksonUtil.createObjectNode();
        conf.put(JobNameProperties.JOB_NAME.getName(), diJobDTO.getJobCode());
        conf.set("env", buildEnv(diJobDTO));

        MutableGraph<ObjectNode> graph = buildGraph(diJobDTO);
        ObjectNode sourceConf = JacksonUtil.createObjectNode();
        ObjectNode transformConf = JacksonUtil.createObjectNode();
        ObjectNode sinkConf = JacksonUtil.createObjectNode();
        conf.set("source", sourceConf);
        conf.set("transform", transformConf);
        conf.set("sink", sinkConf);

        //source and result table name
        graph.edges().forEach(edge -> {
            ObjectNode source = edge.source();
            ObjectNode target = edge.target();
            JsonNode nodeId = source.get(NODE_ID);
            source.set(RESULT_TABLE_NAME.getName(), nodeId);
            target.set(SOURCE_TABLE_NAME.getName(), nodeId);
        });

        graph.nodes().forEach(node -> {
            String pluginName = node.get(PLUGIN_NAME).asText();
            String nodeType = node.get(NODE_TYPE).asText();
            if (JobStepTypeEnum.SOURCE.getValue().equals(nodeType)) {
                sourceConf.set(pluginName, node);
            } else if (JobStepTypeEnum.TRANSFORM.getValue().equals(nodeType)) {
                transformConf.set(pluginName, node);
            } else if (JobStepTypeEnum.SINK.getValue().equals(nodeType)) {
                sinkConf.set(pluginName, node);
            }
        });

        return conf.toPrettyString();
    }

    private ObjectNode buildEnv(DiJobDTO job) {
        ObjectNode env = JacksonUtil.createObjectNode();
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
                Properties properties = mergeJobAttrs(step);
                SeatunnelNativeFlinkConnector connector = seatunnelConnectorService.newConnector(step.getStepName(), properties);
                ObjectNode connectorConf = connector.createConf();
                connectorConf.put(PLUGIN_NAME, step.getStepName());
                connectorConf.put(NODE_TYPE, step.getStepType().getValue());
                connectorConf.put(NODE_ID, step.getId());
                stepMap.put(step.getStepCode(), connectorConf);
                graph.addNode(connectorConf);
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
        List<DiJobStepAttrDTO> stepAttrList = step.getJobStepAttrList();
        if (CollectionUtil.isNotEmpty(stepAttrList)) {
            stepAttrList.forEach(attr -> {
                if (Constants.JOB_STEP_ATTR_DATASOURCE.equals(attr.getStepAttrKey())) {
                    DictVO dsAttr = JSONUtil.toBean(attr.getStepAttrValue(), DictVO.class);
                    MetaDatasourceDTO datasourceDTO =
                            metaDatasourceService.selectOne(Long.parseLong(dsAttr.getValue()), false);
                    if (datasourceDTO != null) {
                        properties.putAll(datasourceDTO.getProps());
                        properties.putAll(datasourceDTO.getAdditionalProps());
                    }
                } else {
                    properties.put(attr.getStepAttrKey(), attr.getStepAttrValue());
                }
            });
        }
        return properties;
    }
}
