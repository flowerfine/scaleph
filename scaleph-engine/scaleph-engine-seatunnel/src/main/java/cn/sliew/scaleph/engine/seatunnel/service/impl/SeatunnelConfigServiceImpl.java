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

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.job.JobStepType;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginName;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginType;
import cn.sliew.scaleph.core.di.service.dto.DiJobAttrDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobLinkDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobStepDTO;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConfigService;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConnectorService;
import cn.sliew.scaleph.engine.seatunnel.service.constant.SeaTunnelConstant;
import cn.sliew.scaleph.plugin.framework.exception.PluginException;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.JobNameProperties;
import cn.sliew.scaleph.plugin.seatunnel.flink.resource.ResourceProperty;
import cn.sliew.scaleph.plugin.seatunnel.flink.util.SeaTunnelPluginUtil;
import cn.sliew.scaleph.resource.service.ResourceService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static cn.sliew.scaleph.engine.seatunnel.service.constant.GraphConstants.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties.RESULT_TABLE_NAME;
import static cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties.SOURCE_TABLE_NAME;

@Slf4j
@Service
public class SeatunnelConfigServiceImpl implements SeatunnelConfigService {

    @Autowired
    private SeatunnelConnectorService seatunnelConnectorService;
    @Autowired
    private ResourceService resourceService;

    @Override
    public String buildConfig(DiJobDTO job) throws Exception {
        ObjectNode conf = JacksonUtil.createObjectNode();
        // env
        buildEnvs(conf, job.getJobName(), job.getJobAttrList());
        // source, sink, transform
        MutableGraph<ObjectNode> graph = buildGraph(job);
        buildNodes(conf, graph.nodes());
        // append source_table_name and result_table_name
        buildEdges(graph.edges());
        // remove utilty fields
        clearUtiltyField(graph.nodes());
        return conf.toPrettyString();
    }

    private ObjectNode buildEnv(String jobName, List<DiJobAttrDTO> jobAttrs) {
        ObjectNode env = JacksonUtil.createObjectNode();
        env.put(JobNameProperties.JOB_NAME.getName(), jobName);
        if (CollectionUtils.isEmpty(jobAttrs)) {
            return env;
        }
        for (DiJobAttrDTO attr : jobAttrs) {
            switch (attr.getJobAttrType()) {
                case ENV:
                    break;
                case VARIABLE:
                    env.put(attr.getJobAttrKey(), attr.getJobAttrValue());
                    break;
                case PROPERTIES:
                    break;
                default:
            }
        }
        return env;
    }

    private MutableGraph<ObjectNode> buildGraph(DiJobDTO diJobDTO) throws PluginException {
        MutableGraph<ObjectNode> graph = GraphBuilder.directed().build();
        List<DiJobStepDTO> jobStepList = diJobDTO.getJobStepList();
        List<DiJobLinkDTO> jobLinkList = diJobDTO.getJobLinkList();
        if (CollectionUtils.isEmpty(jobStepList) || CollectionUtils.isEmpty(jobLinkList)) {
            return graph;
        }
        Map<String, ObjectNode> stepMap = new HashMap<>();
        for (DiJobStepDTO step : jobStepList) {
            Properties properties = mergeJobAttrs(step);
            SeaTunnelPluginType stepType = step.getStepType();
            SeaTunnelPluginName stepName = step.getStepName();
            SeaTunnelConnectorPlugin connector = seatunnelConnectorService.newConnector(SeaTunnelPluginUtil.getIdentity(stepType, stepName), properties);
            ObjectNode stepConf = connector.createConf();
            stepConf.put(NODE_ID, step.getId());
            stepConf.put(NODE_TYPE, stepType.getValue());
            stepConf.put(SeaTunnelConstant.PLUGIN_NAME, stepName.getValue());
            stepMap.put(step.getStepCode(), stepConf);
            graph.addNode(stepConf);
        }
        jobLinkList.forEach(link -> {
            String from = link.getFromStepCode();
            String to = link.getToStepCode();
            graph.putEdge(stepMap.get(from), stepMap.get(to));
        });
        return graph;
    }

    private Properties mergeJobAttrs(DiJobStepDTO step) throws PluginException {
        Properties properties = convertToProperties(step.getStepAttrs());
        SeaTunnelPluginType pluginType = SeaTunnelPluginType.of(step.getStepType().getValue());
        SeaTunnelConnectorPlugin connector = seatunnelConnectorService.getConnector(pluginType, step.getStepName());
        for (ResourceProperty resource : connector.getRequiredResources()) {
            String name = resource.getProperty().getName();
            if (properties.containsKey(name)) {
                Object property = properties.get(name);
                // fixme force conform property to resource id
                Object value = resourceService.getRaw(resource.getType(), Long.valueOf(property.toString()));
                properties.put(name, JacksonUtil.toJsonString(value));
            }
        }
        return properties;
    }

    private Properties convertToProperties(Map<String, Object> stepAttrList) {
        Properties properties = new Properties();
        if (CollectionUtils.isEmpty(stepAttrList)) {
            return properties;
        }
        stepAttrList.forEach((key, value) -> {
            if (value instanceof String) {
                if (StringUtils.hasText(String.valueOf(value))) {
                    properties.put(key, value);
                }
            } else {
                properties.put(key, value);
            }
        });
        return properties;
    }

    private void buildEnvs(ObjectNode conf, String jobName, List<DiJobAttrDTO> jobAttrList) {
        conf.set(SeaTunnelConstant.ENV, buildEnv(jobName, jobAttrList));
    }

    private void buildNodes(ObjectNode conf, Set<ObjectNode> nodes) {
        ArrayNode sourceConf = JacksonUtil.createArrayNode();
        ArrayNode transformConf = JacksonUtil.createArrayNode();
        ArrayNode sinkConf = JacksonUtil.createArrayNode();

        nodes.forEach(node -> {
            String nodeType = node.get(NODE_TYPE).asText();
            JobStepType stepType = JobStepType.of(nodeType);
            switch (stepType) {
                case SOURCE:
                    sourceConf.add(node);
                    break;
                case SINK:
                    sinkConf.add(node);
                    break;
                case TRANSFORM:
                    transformConf.add(node);
                    break;
                default:
            }
        });

        conf.set(SeaTunnelConstant.SOURCE, sourceConf);
        conf.set(SeaTunnelConstant.TRANSFORM, transformConf);
        conf.set(SeaTunnelConstant.SINK, sinkConf);
    }

    private void buildEdges(Set<EndpointPair<ObjectNode>> edges) {
        edges.forEach(edge -> {
            ObjectNode source = edge.source();
            ObjectNode target = edge.target();
            String pluginName = source.get(SeaTunnelConstant.PLUGIN_NAME).asText().toLowerCase();
            String nodeId = source.get(NODE_ID).asText();
            source.put(RESULT_TABLE_NAME.getName(), TABLE_PREFIX + pluginName + "_" + nodeId);
            target.put(SOURCE_TABLE_NAME.getName(), TABLE_PREFIX + pluginName + "_" + nodeId);
        });
    }

    private void clearUtiltyField(Set<ObjectNode> nodes) {
        nodes.forEach(node -> {
            node.remove(NODE_TYPE);
            node.remove(NODE_ID);
        });
    }

}
