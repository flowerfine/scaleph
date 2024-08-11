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

import cn.sliew.carp.module.datasource.service.CarpDsInfoService;
import cn.sliew.carp.module.datasource.service.dto.DsInfoDTO;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCPluginName;
import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCPluginType;
import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCVersion;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.common.util.PropertyUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactFlinkCDC;
import cn.sliew.scaleph.dao.mapper.master.ws.WsArtifactFlinkCDCMapper;
import cn.sliew.scaleph.plugin.flink.cdc.FlinkCDCPipilineConnectorPlugin;
import cn.sliew.scaleph.plugin.flink.cdc.connectors.CommonProperties;
import cn.sliew.scaleph.plugin.flink.cdc.pipeline.PipelineProperties;
import cn.sliew.scaleph.plugin.flink.cdc.util.FlinkCDCPluginUtil;
import cn.sliew.scaleph.plugin.framework.exception.PluginException;
import cn.sliew.scaleph.plugin.framework.resource.ResourceProperties;
import cn.sliew.scaleph.workspace.flink.cdc.service.FlinkCDCConnectorService;
import cn.sliew.scaleph.workspace.flink.cdc.service.WsArtifactFlinkCDCService;
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
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WsArtifactFlinkCDCServiceImpl implements WsArtifactFlinkCDCService {

    @Autowired
    private WsArtifactFlinkCDCMapper wsArtifactFlinkCDCMapper;
    @Autowired
    private WsArtifactService wsArtifactService;
    @Autowired
    private FlinkCDCConnectorService flinkCDCConnectorService;
    @Autowired
    private CarpDsInfoService dsInfoService;

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
    public JsonNode buildConfig(WsArtifactFlinkCDCDTO dto) throws Exception {
        ObjectNode conf = JacksonUtil.createObjectNode();
        conf.set("pipeline", buildPipeline(dto));
        if (dto.getFromDsId() != null) {
            conf.set("source", buildSourceOrSink(FlinkCDCPluginType.SOURCE, dto));
        }
        if (dto.getToDsId() != null) {
            conf.set("sink", buildSourceOrSink(FlinkCDCPluginType.SINK, dto));
        }
        conf.set("transform", dto.getTransform());
        conf.set("route", dto.getRoute());
        return conf;
    }

    private ObjectNode buildPipeline(WsArtifactFlinkCDCDTO dto) {
        ObjectNode pipeline = JacksonUtil.createObjectNode();
        pipeline.put(PipelineProperties.NAME.getName(), dto.getArtifact().getName());
        pipeline.put(PipelineProperties.PARALLELISM.getName(), dto.getParallelism());
        pipeline.put(PipelineProperties.LOCAL_TIME_ZONE.getName(), dto.getLocalTimeZone());
        return pipeline;
    }

    private ObjectNode buildSourceOrSink(FlinkCDCPluginType pluginType, WsArtifactFlinkCDCDTO dto) throws PluginException {
        Properties properties = new Properties();
        DsInfoDTO dsInfoDTO = null;
        FlinkCDCPluginName pluginName = null;
        switch (pluginType) {
            case SOURCE:
                dsInfoDTO = dsInfoService.selectOne(dto.getFromDsId(), false);
                pluginName = convertPluginName(dsInfoDTO).get();
                properties = mergeJobAttrs(pluginType, pluginName, dsInfoDTO, dto.getFromDsConfig());
                break;
            case SINK:
                dsInfoDTO = dsInfoService.selectOne(dto.getToDsId(), false);
                pluginName = convertPluginName(dsInfoDTO).get();
                properties = mergeJobAttrs(pluginType, pluginName, dsInfoDTO, dto.getToDsConfig());
                break;
            default:
        }
        FlinkCDCPipilineConnectorPlugin connectorPlugin = flinkCDCConnectorService.newConnector(FlinkCDCPluginUtil.getIdentity(pluginType, pluginName), properties);
        return connectorPlugin.createConf();
    }

    private Optional<FlinkCDCPluginName> convertPluginName(DsInfoDTO dsInfoDTO) {
        String name = dsInfoDTO.getDsType().getType().name();
        return Optional.ofNullable(EnumUtils.getEnumIgnoreCase(FlinkCDCPluginName.class, name));
    }

    private Properties mergeJobAttrs(FlinkCDCPluginType pluginType, FlinkCDCPluginName pluginName, DsInfoDTO dsInfoDTO, JsonNode config) throws PluginException {
        Properties properties = PropertyUtil.mapToProperties(JacksonUtil.toObject(config, new TypeReference<Map<String, Object>>() {
        }));
        properties.put(CommonProperties.TYPE.getName(), pluginName.getValue());
        properties.put(CommonProperties.NAME.getName(), dsInfoDTO.getName());
        properties.put(ResourceProperties.DATASOURCE.getName(), JacksonUtil.toJsonString(dsInfoDTO));
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
