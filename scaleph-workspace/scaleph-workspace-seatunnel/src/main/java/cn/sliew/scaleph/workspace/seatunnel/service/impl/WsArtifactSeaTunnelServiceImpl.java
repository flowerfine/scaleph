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

package cn.sliew.scaleph.workspace.seatunnel.service.impl;

import cn.sliew.carp.framework.dag.service.dto.DagConfigComplexDTO;
import cn.sliew.carp.framework.dag.service.dto.DagConfigLinkDTO;
import cn.sliew.carp.framework.dag.service.dto.DagConfigStepDTO;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelEngineType;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginName;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginType;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelVersion;
import cn.sliew.scaleph.common.util.PropertyUtil;
import cn.sliew.scaleph.dag.constant.GraphConstants;
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactSeaTunnel;
import cn.sliew.scaleph.dao.mapper.master.ws.WsArtifactSeaTunnelMapper;
import cn.sliew.scaleph.plugin.framework.exception.PluginException;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.JobNameProperties;
import cn.sliew.scaleph.plugin.seatunnel.flink.resource.ResourceProperty;
import cn.sliew.scaleph.plugin.seatunnel.flink.util.SeaTunnelPluginUtil;
import cn.sliew.scaleph.resource.service.ResourceService;
import cn.sliew.scaleph.workspace.project.service.WsArtifactService;
import cn.sliew.scaleph.workspace.project.service.dto.WsArtifactDTO;
import cn.sliew.scaleph.workspace.seatunnel.service.SeaTunnelConnectorService;
import cn.sliew.scaleph.workspace.seatunnel.service.SeaTunnelDagService;
import cn.sliew.scaleph.workspace.seatunnel.service.WsArtifactSeaTunnelService;
import cn.sliew.scaleph.workspace.seatunnel.service.constant.SeaTunnelConstant;
import cn.sliew.scaleph.workspace.seatunnel.service.convert.WsArtifactSeaTunnelConvert;
import cn.sliew.scaleph.workspace.seatunnel.service.dto.WsArtifactSeaTunnelDTO;
import cn.sliew.scaleph.workspace.seatunnel.service.param.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static cn.sliew.milky.common.check.Ensures.checkState;
import static cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties.RESULT_TABLE_NAME;
import static cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties.SOURCE_TABLE_NAME;

@Service
public class WsArtifactSeaTunnelServiceImpl implements WsArtifactSeaTunnelService {

    @Autowired
    private WsArtifactSeaTunnelMapper wsArtifactSeaTunnelMapper;
    @Autowired
    private WsArtifactService wsArtifactService;
    @Autowired
    private SeaTunnelDagService seaTunnelDagService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private SeaTunnelConnectorService seaTunnelConnectorService;

    @Override
    public Page<WsArtifactSeaTunnelDTO> list(WsArtifactSeaTunnelListParam param) {
        Page<WsArtifactSeaTunnel> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<WsArtifactSeaTunnel> seaTunnelPage = wsArtifactSeaTunnelMapper.list(page, param.getProjectId(), param.getName(), param.getFlinkVersion());
        Page<WsArtifactSeaTunnelDTO> result =
                new Page<>(seaTunnelPage.getCurrent(), seaTunnelPage.getSize(), seaTunnelPage.getTotal());
        result.setRecords(WsArtifactSeaTunnelConvert.INSTANCE.toDto(seaTunnelPage.getRecords()));
        return result;
    }

    @Override
    public Page<WsArtifactSeaTunnelDTO> listByArtifact(WsArtifactSeaTunnelArtifactParam param) {
        Page<WsArtifactSeaTunnel> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsArtifactSeaTunnel> queryWrapper = Wrappers.lambdaQuery(WsArtifactSeaTunnel.class)
                .eq(WsArtifactSeaTunnel::getArtifactId, param.getArtifactId())
                .orderByDesc(WsArtifactSeaTunnel::getId);
        Page<WsArtifactSeaTunnel> wsArtifactSeaTunnelPage = wsArtifactSeaTunnelMapper.selectPage(page, queryWrapper);
        Page<WsArtifactSeaTunnelDTO> result =
                new Page<>(wsArtifactSeaTunnelPage.getCurrent(), wsArtifactSeaTunnelPage.getSize(), wsArtifactSeaTunnelPage.getTotal());
        result.setRecords(WsArtifactSeaTunnelConvert.INSTANCE.toDto(wsArtifactSeaTunnelPage.getRecords()));
        return result;
    }

    @Override
    public List<WsArtifactSeaTunnelDTO> listAll(WsArtifactSeaTunnelSelectListParam param) {
        List<WsArtifactSeaTunnel> seaTunnels = wsArtifactSeaTunnelMapper.listAll(param.getProjectId(), param.getName());
        return WsArtifactSeaTunnelConvert.INSTANCE.toDto(seaTunnels);
    }

    @Override
    public List<WsArtifactSeaTunnelDTO> listAllByArtifact(Long artifactId) {
        List<WsArtifactSeaTunnel> list = wsArtifactSeaTunnelMapper.selectList(
                Wrappers.lambdaQuery(WsArtifactSeaTunnel.class)
                        .eq(WsArtifactSeaTunnel::getArtifactId, artifactId)
                        .orderByDesc(WsArtifactSeaTunnel::getId)
        );
        return WsArtifactSeaTunnelConvert.INSTANCE.toDto(list);
    }

    @Override
    public WsArtifactSeaTunnelDTO selectOne(Long id) {
        WsArtifactSeaTunnel record = wsArtifactSeaTunnelMapper.selectOne(id);
        checkState(record != null, () -> "artifact seatunnel not exists for id: " + id);
        WsArtifactSeaTunnelDTO dto = WsArtifactSeaTunnelConvert.INSTANCE.toDto(record);
        dto.setDag(seaTunnelDagService.getDag(dto.getDagId()));
        return dto;
    }

    @Override
    public WsArtifactSeaTunnelDTO selectCurrent(Long artifactId) {
        WsArtifactSeaTunnel record = wsArtifactSeaTunnelMapper.selectCurrent(artifactId);
        return WsArtifactSeaTunnelConvert.INSTANCE.toDto(record);
    }

    @Override
    public String buildConfig(Long id, Optional<String> jobName, Optional<String> jobMode) throws Exception {
        WsArtifactSeaTunnelDTO dto = selectOne(id);
        ObjectNode conf = JacksonUtil.createObjectNode();
        DagConfigComplexDTO dag = dto.getDag();
        // env
        buildEnvs(conf, jobName.isPresent() ? jobName.get() : dto.getArtifact().getName(), jobMode, dag.getDagAttrs());
        // source, sink, transform
        MutableGraph<ObjectNode> graph = buildGraph(dag);
        buildNodes(conf, graph.nodes());
        // append source_table_name and result_table_name
        buildEdges(graph.edges());
        // remove utilty fields
        clearUtiltyField(graph.nodes());
        return conf.toPrettyString();
    }

    @Override
    public WsArtifactSeaTunnelDTO insert(WsArtifactSeaTunnelAddParam param) {
        WsArtifactDTO artifactDTO = new WsArtifactDTO();
        artifactDTO.setProjectId(param.getProjectId());
        artifactDTO.setType(FlinkJobType.SEATUNNEL);
        artifactDTO.setName(param.getName());
        artifactDTO.setRemark(param.getRemark());
        artifactDTO = wsArtifactService.insert(artifactDTO);
        WsArtifactSeaTunnel record = new WsArtifactSeaTunnel();
        record.setArtifactId(artifactDTO.getId());
        record.setSeaTunnelEngine(SeaTunnelEngineType.SEATUNNEL);
        record.setFlinkVersion(FlinkVersion.V_1_16_3);
        record.setSeaTunnelVersion(SeaTunnelVersion.current());
        record.setDagId(seaTunnelDagService.initialize(param.getName(), param.getRemark()));
        record.setCurrent(YesOrNo.YES);
        wsArtifactSeaTunnelMapper.insert(record);
        return selectOne(record.getId());
    }

    @Override
    public int update(WsArtifactSeaTunnelUpdateParam param) {
        WsArtifactSeaTunnelDTO wsArtifactSeaTunnelDTO = selectOne(param.getId());
        WsArtifactDTO flinkArtifact = new WsArtifactDTO();
        flinkArtifact.setId(wsArtifactSeaTunnelDTO.getArtifact().getId());
        flinkArtifact.setName(param.getName());
        flinkArtifact.setRemark(param.getRemark());
        wsArtifactService.update(flinkArtifact);

        WsArtifactSeaTunnel record = new WsArtifactSeaTunnel();
        record.setId(param.getId());
        record.setCurrent(YesOrNo.YES);
        return wsArtifactSeaTunnelMapper.updateById(record);
    }

    @Override
    public void updateGraph(WsArtifactSeaTunnelGraphParam param) {
        WsArtifactSeaTunnelDTO wsArtifactSeaTunnelDTO = selectOne(param.getId());
        seaTunnelDagService.update(wsArtifactSeaTunnelDTO.getDagId(), param.getJobGraph());
    }

    @Override
    public int delete(Long id) {
        WsArtifactSeaTunnelDTO wsArtifactSeaTunnelDTO = selectOne(id);
        checkState(wsArtifactSeaTunnelDTO.getCurrent() != YesOrNo.YES, () -> "Unsupport delete current seatunnel");
        return doDelete(wsArtifactSeaTunnelDTO);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
        return ids.size();
    }

    @Override
    public int deleteArtifact(Long artifactId) {
        List<WsArtifactSeaTunnelDTO> dtos = listAllByArtifact(artifactId);
        for (WsArtifactSeaTunnelDTO seaTunnelDTO : dtos) {
            doDelete(seaTunnelDTO);
        }
        return wsArtifactService.deleteById(artifactId);
    }

    private int doDelete(WsArtifactSeaTunnelDTO cdc) {
        seaTunnelDagService.destroy(cdc.getDagId());
        return wsArtifactSeaTunnelMapper.deleteById(cdc.getId());
    }

    private void buildEnvs(ObjectNode conf, String jobName, Optional<String> jobMode, JsonNode dagAttrs) {
        conf.set(SeaTunnelConstant.ENV, buildEnv(jobName, jobMode, dagAttrs));
    }

    private ObjectNode buildEnv(String jobName, Optional<String> jobMode, JsonNode dagAttrs) {
        ObjectNode env = JacksonUtil.createObjectNode();
        env.put(JobNameProperties.JOB_NAME.getName(), jobName);
        jobMode.ifPresent(mode -> env.put(JobNameProperties.JOB_MODE.getName(), mode));
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
        if (CollectionUtils.isEmpty(steps) || CollectionUtils.isEmpty(links)) {
            return graph;
        }
        Map<String, ObjectNode> stepMap = new HashMap<>();
        for (DagConfigStepDTO step : steps) {
            Properties properties = mergeJobAttrs(step);
            SeaTunnelPluginType stepType = SeaTunnelPluginType.of(step.getStepMeta().get("type").asText());
            SeaTunnelPluginName stepName = SeaTunnelPluginName.of(step.getStepMeta().get("name").asText());
            SeaTunnelConnectorPlugin connector = seaTunnelConnectorService.newConnector(SeaTunnelPluginUtil.getIdentity(stepType, stepName), properties);
            ObjectNode stepConf = connector.createConf();
            stepConf.put(GraphConstants.NODE_ID, step.getId());
            stepConf.put(GraphConstants.NODE_TYPE, stepType.getValue());
            stepConf.put(SeaTunnelConstant.PLUGIN_NAME, stepName.getValue());
            stepMap.put(step.getStepId(), stepConf);
            graph.addNode(stepConf);
        }
        links.forEach(link -> graph.putEdge(stepMap.get(link.getFromStepId()), stepMap.get(link.getToStepId())));
        return graph;
    }

    private Properties mergeJobAttrs(DagConfigStepDTO step) throws PluginException {
        Properties properties = PropertyUtil.mapToProperties(JacksonUtil.toObject(step.getStepAttrs(), new TypeReference<Map<String, Object>>() {
        }));
        SeaTunnelPluginType pluginType = SeaTunnelPluginType.of(step.getStepMeta().get("type").asText());
        SeaTunnelPluginName stepName = SeaTunnelPluginName.of(step.getStepMeta().get("name").asText());

        SeaTunnelConnectorPlugin connector = seaTunnelConnectorService.getConnector(pluginType, stepName);
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

    private void buildNodes(ObjectNode conf, Set<ObjectNode> nodes) {
        ArrayNode sourceConf = JacksonUtil.createArrayNode();
        ArrayNode transformConf = JacksonUtil.createArrayNode();
        ArrayNode sinkConf = JacksonUtil.createArrayNode();

        nodes.forEach(node -> {
            String nodeType = node.get(GraphConstants.NODE_TYPE).asText();
            SeaTunnelPluginType stepType = SeaTunnelPluginType.of(nodeType);
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

        conf.set(SeaTunnelPluginType.SOURCE.getValue(), sourceConf);
        conf.set(SeaTunnelPluginType.TRANSFORM.getValue(), transformConf);
        conf.set(SeaTunnelPluginType.SINK.getValue(), sinkConf);
    }

    private void buildEdges(Set<EndpointPair<ObjectNode>> edges) {
        edges.forEach(edge -> {
            ObjectNode source = edge.source();
            ObjectNode target = edge.target();
            // 部分 connector 如 MySQL-CDC 中间的 - 转换成的 table_name 会抛异常
            String pluginName = source.get(SeaTunnelConstant.PLUGIN_NAME).asText().toLowerCase().replace("-", "_");
            String nodeId = source.get(GraphConstants.NODE_ID).asText();
            source.put(RESULT_TABLE_NAME.getName(), GraphConstants.TABLE_PREFIX + pluginName + "_" + nodeId);
            target.put(SOURCE_TABLE_NAME.getName(), GraphConstants.TABLE_PREFIX + pluginName + "_" + nodeId);
        });
    }

    private void clearUtiltyField(Set<ObjectNode> nodes) {
        nodes.forEach(node -> {
            node.remove(GraphConstants.NODE_TYPE);
            node.remove(GraphConstants.NODE_ID);
        });
    }
}
