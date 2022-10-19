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
import cn.sliew.scaleph.engine.seatunnel.service.constant.SeaTunnelConstant;
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
import com.google.common.graph.EndpointPair;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static cn.sliew.scaleph.engine.seatunnel.service.constant.GraphConstants.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties.RESULT_TABLE_NAME;
import static cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties.SOURCE_TABLE_NAME;

@Slf4j
@Service
public class SeatunnelConfigServiceImpl implements SeatunnelConfigService {

    @Autowired
    private MetaDatasourceService metaDatasourceService;
    @Autowired
    private SeatunnelConnectorService seatunnelConnectorService;

    @Override
    public String buildConfig(DiJobDTO job) {
        ObjectNode conf = JacksonUtil.createObjectNode();
        // env
        buildEnvs(conf, job.getJobName(), job.getJobAttrList());
        // source, sink, transform
        MutableGraph<ObjectNode> graph = buildGraph(job);
        buildNodes(conf, graph.nodes());
        // append source_table_name and result_table_name
        buildEdges(graph.edges());
        return conf.toPrettyString();
    }

    private ObjectNode buildEnv(String jobName, List<DiJobAttrDTO> jobAttrs) {
        ObjectNode env = JacksonUtil.createObjectNode();
        env.put(JobNameProperties.JOB_NAME.getName(), jobName);
        if (CollectionUtils.isEmpty(jobAttrs)) {
            return env;
        }
        for (DiJobAttrDTO attr : jobAttrs) {
            if (JobAttrTypeEnum.JOB_PROP.getValue().equals(attr.getJobAttrType().getValue())) {
                env.put(attr.getJobAttrKey(), attr.getJobAttrValue());
            }
        }
        return env;
    }


    private MutableGraph<ObjectNode> buildGraph(DiJobDTO diJobDTO) {
        List<DiJobStepDTO> jobStepList = diJobDTO.getJobStepList();
        List<DiJobLinkDTO> jobLinkList = diJobDTO.getJobLinkList();
        Assert.notEmpty(jobStepList, () -> "SeaTunnel job steps can't be empty");
        Assert.notEmpty(jobLinkList, () -> "SeaTunnel job links can't be empty");

        MutableGraph<ObjectNode> graph = GraphBuilder.directed().build();

        Map<String, ObjectNode> stepMap = new HashMap<>();
        for (DiJobStepDTO step : jobStepList) {
            // step_name + step_title
            String name = JOB_STEP_MAP.get(step.getStepType().getValue() + "-" + step.getStepName());
            Properties properties = mergeJobAttrs(step);
            SeaTunnelConnectorPlugin connector = seatunnelConnectorService.newConnector(name, properties);
            ObjectNode stepConf = connector.createConf();
            stepConf.put(SeaTunnelConstant.PLUGIN_NAME, name);
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

    private void buildEnvs(ObjectNode conf, String jobName, List<DiJobAttrDTO> jobAttrList) {
        conf.set(SeaTunnelConstant.ENV, buildEnv(jobName, jobAttrList));
    }

    private void buildNodes(ObjectNode conf, Set<ObjectNode> nodes) {
        ArrayNode sourceConf = JacksonUtil.createArrayNode();
        ArrayNode transformConf = JacksonUtil.createArrayNode();
        ArrayNode sinkConf = JacksonUtil.createArrayNode();

        nodes.forEach(node -> {
            String nodeType = node.get(NODE_TYPE).asText();
            if (JobStepTypeEnum.SOURCE.getValue().equals(nodeType)) {
                sourceConf.add(node);
            } else if (JobStepTypeEnum.TRANSFORM.getValue().equals(nodeType)) {
                transformConf.add(node);
            } else if (JobStepTypeEnum.SINK.getValue().equals(nodeType)) {
                sinkConf.add(node);
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
            String nodeId = source.get(NODE_ID).asText();
            source.put(RESULT_TABLE_NAME.getName(), TABLE_PREFIX + nodeId);
            target.put(SOURCE_TABLE_NAME.getName(), TABLE_PREFIX + nodeId);
        });
    }

}
