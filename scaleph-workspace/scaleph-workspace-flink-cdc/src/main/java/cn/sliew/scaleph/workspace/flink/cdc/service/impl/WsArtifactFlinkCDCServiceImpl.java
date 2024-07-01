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

package cn.sliew.scaleph.workspace.flink.cdc.service.impl;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCPluginName;
import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCPluginType;
import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCVersion;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.common.util.PropertyUtil;
import cn.sliew.scaleph.dag.constant.GraphConstants;
import cn.sliew.scaleph.dag.service.dto.DagConfigComplexDTO;
import cn.sliew.scaleph.dag.service.dto.DagConfigLinkDTO;
import cn.sliew.scaleph.dag.service.dto.DagConfigStepDTO;
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactFlinkCDC;
import cn.sliew.scaleph.dao.mapper.master.ws.WsArtifactFlinkCDCMapper;
import cn.sliew.scaleph.plugin.flink.cdc.FlinkCDCPipilineConnectorPlugin;
import cn.sliew.scaleph.plugin.flink.cdc.pipeline.PipelineProperties;
import cn.sliew.scaleph.plugin.flink.cdc.util.FlinkCDCPluginUtil;
import cn.sliew.scaleph.plugin.framework.exception.PluginException;
import cn.sliew.scaleph.plugin.framework.resource.ResourceProperty;
import cn.sliew.scaleph.resource.service.ResourceService;
import cn.sliew.scaleph.workspace.flink.cdc.service.FlinkCDCConnectorService;
import cn.sliew.scaleph.workspace.flink.cdc.service.WsArtifactFlinkCDCService;
import cn.sliew.scaleph.workspace.flink.cdc.service.constant.FlinkCDCConstant;
import cn.sliew.scaleph.workspace.flink.cdc.service.convert.WsArtifactFlinkCDCConvert;
import cn.sliew.scaleph.workspace.flink.cdc.service.dto.WsArtifactFlinkCDCDTO;
import cn.sliew.scaleph.workspace.flink.cdc.service.param.*;
import cn.sliew.scaleph.workspace.project.service.WsArtifactService;
import cn.sliew.scaleph.workspace.project.service.dto.WsArtifactDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WsArtifactFlinkCDCServiceImpl implements WsArtifactFlinkCDCService {

    // todo 迁移到 JacksonUtil
    private YAMLMapper yamlMapper = new YAMLMapper();

    @Autowired
    private WsArtifactFlinkCDCMapper wsArtifactFlinkCDCMapper;
    @Autowired
    private WsArtifactService wsArtifactService;
    @Autowired
    private FlinkCDCConnectorService flinkCDCConnectorService;
    @Autowired
    private ResourceService resourceService;

    @Override
    public Page<WsArtifactFlinkCDCDTO> list(WsArtifactFlinkCDCListParam param) {
        Page<WsArtifactFlinkCDC> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<WsArtifactFlinkCDC> cdcPage = wsArtifactFlinkCDCMapper.list(page, param.getProjectId(), param.getName(), param.getFlinkVersion());
        Page<WsArtifactFlinkCDCDTO> result =
                new Page<>(cdcPage.getCurrent(), cdcPage.getSize(), cdcPage.getTotal());
        result.setRecords(WsArtifactFlinkCDCConvert.INSTANCE.toDto(cdcPage.getRecords()));
        return result;
    }

    @Override
    public Page<WsArtifactFlinkCDCDTO> listByArtifact(WsArtifactFlinkCDCArtifactParam param) {
        Page<WsArtifactFlinkCDC> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsArtifactFlinkCDC> queryWrapper = Wrappers.lambdaQuery(WsArtifactFlinkCDC.class)
                .eq(WsArtifactFlinkCDC::getArtifactId, param.getArtifactId())
                .orderByDesc(WsArtifactFlinkCDC::getId);
        Page<WsArtifactFlinkCDC> wsArtifactFlinkCDCPage = wsArtifactFlinkCDCMapper.selectPage(page, queryWrapper);
        Page<WsArtifactFlinkCDCDTO> result =
                new Page<>(wsArtifactFlinkCDCPage.getCurrent(), wsArtifactFlinkCDCPage.getSize(), wsArtifactFlinkCDCPage.getTotal());
        result.setRecords(WsArtifactFlinkCDCConvert.INSTANCE.toDto(wsArtifactFlinkCDCPage.getRecords()));
        return result;
    }

    @Override
    public List<WsArtifactFlinkCDCDTO> listAll(WsArtifactFlinkCDCSelectListParam param) {
        List<WsArtifactFlinkCDC> cdcs = wsArtifactFlinkCDCMapper.listAll(param.getProjectId(), param.getName());
        return WsArtifactFlinkCDCConvert.INSTANCE.toDto(cdcs);
    }

    @Override
    public List<WsArtifactFlinkCDCDTO> listAllByArtifact(Long artifactId) {
        List<WsArtifactFlinkCDC> list = wsArtifactFlinkCDCMapper.selectList(
                Wrappers.lambdaQuery(WsArtifactFlinkCDC.class)
                        .eq(WsArtifactFlinkCDC::getArtifactId, artifactId)
                        .orderByDesc(WsArtifactFlinkCDC::getId)
        );
        return WsArtifactFlinkCDCConvert.INSTANCE.toDto(list);
    }

    @Override
    public WsArtifactFlinkCDCDTO selectOne(Long id) {
        WsArtifactFlinkCDC record = wsArtifactFlinkCDCMapper.selectOne(id);
        checkState(record != null, () -> "artifact flink-cdc not exists for id: " + id);
        return WsArtifactFlinkCDCConvert.INSTANCE.toDto(record);
    }

    @Override
    public WsArtifactFlinkCDCDTO selectCurrent(Long artifactId) {
        WsArtifactFlinkCDC record = wsArtifactFlinkCDCMapper.selectCurrent(artifactId);
        return WsArtifactFlinkCDCConvert.INSTANCE.toDto(record);
    }

    @Override
    public String buildConfig(Long id, Optional<String> jobName) throws Exception {
        WsArtifactFlinkCDCDTO dto = selectOne(id);
        ObjectNode conf = JacksonUtil.createObjectNode();

        return yamlMapper.writeValueAsString(conf);
    }

    private void buildEnvs(ObjectNode conf, String jobName, JsonNode dagAttrs) {
        conf.set(FlinkCDCConstant.PIPELINE, buildEnv(jobName, dagAttrs));
    }

    private ObjectNode buildEnv(String jobName, JsonNode dagAttrs) {
        ObjectNode env = JacksonUtil.createObjectNode();
        env.put(PipelineProperties.NAME.getName(), jobName);
        if (dagAttrs == null || dagAttrs.isEmpty()) {
            return env;
        }
        Iterator<Map.Entry<String, JsonNode>> fields = dagAttrs.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            env.put(entry.getKey(), entry.getValue());
        }
        return env;
    }

    private MutableGraph<ObjectNode> buildGraph(DagConfigComplexDTO dag) throws PluginException {
        MutableGraph<ObjectNode> graph = GraphBuilder.directed().build();
        List<DagConfigStepDTO> steps = dag.getSteps();
        List<DagConfigLinkDTO> links = dag.getLinks();
        if (CollectionUtils.isEmpty(steps)) {
            return graph;
        }
        Map<String, ObjectNode> stepMap = new HashMap<>();
        for (DagConfigStepDTO step : steps) {
            Properties properties = mergeJobAttrs(step);
            FlinkCDCPluginType stepType = FlinkCDCPluginType.of(step.getStepMeta().get("type").asText());
            FlinkCDCPluginName stepName = FlinkCDCPluginName.of(step.getStepMeta().get("name").asText());
            FlinkCDCPipilineConnectorPlugin connector = flinkCDCConnectorService.newConnector(FlinkCDCPluginUtil.getIdentity(stepType, stepName), properties);
            ObjectNode stepConf = connector.createConf();
            stepConf.put(GraphConstants.NODE_ID, step.getId());
            stepConf.put(GraphConstants.NODE_TYPE, stepType.getValue());
            stepMap.put(step.getStepId(), stepConf);
            graph.addNode(stepConf);
        }
        links.forEach(link -> graph.putEdge(stepMap.get(link.getFromStepId()), stepMap.get(link.getToStepId())));
        return graph;
    }

    private void buildNodes(ObjectNode conf, Set<ObjectNode> nodes) {
        ArrayNode sourceConf = JacksonUtil.createArrayNode();
        ArrayNode sinkConf = JacksonUtil.createArrayNode();
        ArrayNode transformConf = JacksonUtil.createArrayNode();
        ArrayNode routeConf = JacksonUtil.createArrayNode();

        nodes.forEach(node -> {
            String nodeType = node.get(GraphConstants.NODE_TYPE).asText();
            FlinkCDCPluginType stepType = FlinkCDCPluginType.of(nodeType);
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
                case ROUTE:
                    routeConf.add(node);
                    break;
                default:
            }
        });

        conf.set(FlinkCDCPluginType.SOURCE.getValue(), sourceConf);
        conf.set(FlinkCDCPluginType.SINK.getValue(), sinkConf);
        if (transformConf.isEmpty() == false) {
            conf.set(FlinkCDCPluginType.TRANSFORM.getValue(), transformConf);
        }
        if (routeConf.isEmpty() == false) {
            conf.set(FlinkCDCPluginType.ROUTE.getValue(), routeConf);
        }
    }

    private void buildEdges(Set<EndpointPair<ObjectNode>> edges) {
//        edges.forEach(edge -> {
//            ObjectNode source = edge.source();
//            ObjectNode target = edge.target();
//            String pluginName = source.get(SeaTunnelConstant.PLUGIN_NAME).asText().toLowerCase();
//            String nodeId = source.get(GraphConstants.NODE_ID).asText();
//            source.put(RESULT_TABLE_NAME.getName(), GraphConstants.TABLE_PREFIX + pluginName + "_" + nodeId);
//            target.put(SOURCE_TABLE_NAME.getName(), GraphConstants.TABLE_PREFIX + pluginName + "_" + nodeId);
//        });
    }

    private void clearUtiltyField(Set<ObjectNode> nodes) {
        nodes.forEach(node -> {
            node.remove(GraphConstants.NODE_TYPE);
            node.remove(GraphConstants.NODE_ID);
        });
    }

    private Properties mergeJobAttrs(DagConfigStepDTO step) throws PluginException {
        Properties properties = PropertyUtil.mapToProperties(JacksonUtil.toObject(step.getStepAttrs(), new TypeReference<Map<String, Object>>() {
        }));
        FlinkCDCPluginType pluginType = FlinkCDCPluginType.of(step.getStepMeta().get("type").asText());
        FlinkCDCPluginName stepName = FlinkCDCPluginName.of(step.getStepMeta().get("name").asText());

        FlinkCDCPipilineConnectorPlugin connector = flinkCDCConnectorService.getConnector(pluginType, stepName);
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

    @Override
    public WsArtifactFlinkCDCDTO insert(WsArtifactFlinkCDCAddParam param) {
        WsArtifactDTO artifactDTO = new WsArtifactDTO();
        artifactDTO.setProjectId(param.getProjectId());
        artifactDTO.setType(FlinkJobType.FLINK_CDC);
        artifactDTO.setName(param.getName());
        artifactDTO.setRemark(param.getRemark());
        artifactDTO = wsArtifactService.insert(artifactDTO);
        WsArtifactFlinkCDC record = new WsArtifactFlinkCDC();
        record.setArtifactId(artifactDTO.getId());
        record.setFlinkVersion(FlinkVersion.current());
        record.setFlinkCDCVersion(FlinkCDCVersion.current());
        record.setParallelism(param.getParallelism());
        record.setLocalTimeZone(param.getLocalTimeZone());
        record.setFromDsId(param.getFromDsId());
        if (param.getFromDsConfig() != null) {
            record.setFromDsConfig(param.getFromDsConfig().toString());
        }
        record.setToDsId(param.getToDsId());
        if (param.getToDsConfig() != null) {
            record.setToDsConfig(param.getToDsConfig().toString());
        }
        if (param.getTransform() != null) {
            record.setTransform(param.getTransform().toString());
        }
        if (param.getRoute() != null) {
            record.setRoute(param.getRoute().toString());
        }
        record.setCurrent(YesOrNo.YES);
        wsArtifactFlinkCDCMapper.insert(record);
        return selectOne(record.getId());
    }

    @Override
    public int update(WsArtifactFlinkCDCUpdateParam param) {
        WsArtifactFlinkCDCDTO wsArtifactFlinkCDCDTO = selectOne(param.getId());
        WsArtifactDTO flinkArtifact = new WsArtifactDTO();
        flinkArtifact.setId(wsArtifactFlinkCDCDTO.getArtifact().getId());
        flinkArtifact.setName(param.getName());
        flinkArtifact.setRemark(param.getRemark());
        wsArtifactService.update(flinkArtifact);

        WsArtifactFlinkCDC record = new WsArtifactFlinkCDC();
        record.setId(param.getId());
        record.setCurrent(YesOrNo.YES);
        return wsArtifactFlinkCDCMapper.updateById(record);
    }

    @Override
    public int delete(Long id) throws ScalephException {
        WsArtifactFlinkCDCDTO wsArtifactFlinkCDCDTO = selectOne(id);
        checkState(wsArtifactFlinkCDCDTO.getCurrent() != YesOrNo.YES, () -> "Unsupport delete current flink cdc");
        return doDelete(wsArtifactFlinkCDCDTO);
    }

    @Override
    public int deleteBatch(List<Long> ids) throws ScalephException {
        for (Long id : ids) {
            delete(id);
        }
        return ids.size();
    }

    @Override
    public int deleteArtifact(Long artifactId) {
        List<WsArtifactFlinkCDCDTO> dtos = listAllByArtifact(artifactId);
        for (WsArtifactFlinkCDCDTO cdc : dtos) {
            doDelete(cdc);
        }
        return wsArtifactService.deleteById(artifactId);
    }

    private int doDelete(WsArtifactFlinkCDCDTO cdc) {
        return wsArtifactFlinkCDCMapper.deleteById(cdc.getId());
    }
}
