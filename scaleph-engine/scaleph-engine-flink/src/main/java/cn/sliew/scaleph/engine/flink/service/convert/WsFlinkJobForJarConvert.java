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

package cn.sliew.scaleph.engine.flink.service.convert;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkJobForJar;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkJobForJarDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WsFlinkJobForJarConvert extends BaseConvert<WsFlinkJobForJar, WsFlinkJobForJarDTO> {
    WsFlinkJobForJarConvert INSTANCE = Mappers.getMapper(WsFlinkJobForJarConvert.class);

    @Override
    default WsFlinkJobForJar toDo(WsFlinkJobForJarDTO dto) {
        WsFlinkJobForJar entity = new WsFlinkJobForJar();
        BeanUtils.copyProperties(dto, entity);
        entity.setWsFlinkArtifactJar(WsFlinkArtifactJarConvert.INSTANCE.toDo(dto.getFlinkArtifactJar()));
        entity.setWsFlinkClusterConfig(WsFlinkClusterConfigConvert.INSTANCE.toDo(dto.getFlinkClusterConfig()));
        entity.setWsFlinkClusterInstance(WsFlinkClusterInstanceConvert.INSTANCE.toDo(dto.getFlinkClusterInstance()));
        entity.setFlinkJobInstance(WsFlinkJobInstanceConvert.INSTANCE.toDo(dto.getFlinkJobInstance()));
        if (!CollectionUtils.isEmpty(dto.getJobConfig()) ) {
            entity.setJobConfig(JacksonUtil.toJsonString(dto.getJobConfig()));
        }
        if (!CollectionUtils.isEmpty(dto.getFlinkConfig()) ) {
            entity.setFlinkConfig(JacksonUtil.toJsonString(dto.getFlinkConfig()));
        }
        if (!CollectionUtils.isEmpty(dto.getJars()) ) {
            entity.setJars(JacksonUtil.toJsonString(dto.getJars()));
        }
        return entity;
    }

    @Override
    default WsFlinkJobForJarDTO toDto(WsFlinkJobForJar entity) {
        WsFlinkJobForJarDTO dto = new WsFlinkJobForJarDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setFlinkArtifactJar(WsFlinkArtifactJarConvert.INSTANCE.toDto(entity.getWsFlinkArtifactJar()));
        dto.setFlinkClusterConfig(WsFlinkClusterConfigConvert.INSTANCE.toDto(entity.getWsFlinkClusterConfig()));
        dto.setFlinkClusterInstance(WsFlinkClusterInstanceConvert.INSTANCE.toDto(entity.getWsFlinkClusterInstance()));
        dto.setFlinkJobInstance(WsFlinkJobInstanceConvert.INSTANCE.toDto(entity.getFlinkJobInstance()));
        if (StringUtils.hasText(entity.getJobConfig())) {
            dto.setJobConfig(JacksonUtil.parseJsonString(entity.getJobConfig(), Map.class));
        }
        if (StringUtils.hasText(entity.getFlinkConfig())) {
            dto.setFlinkConfig(JacksonUtil.parseJsonString(entity.getFlinkConfig(), Map.class));
        }
        if (StringUtils.hasText(entity.getJars())) {
            dto.setJars(JacksonUtil.parseJsonArray(entity.getJars(), Long.TYPE));
        }
        return dto;
    }
}
