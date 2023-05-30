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

package cn.sliew.scaleph.engine.sql.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactSql;
import cn.sliew.scaleph.engine.sql.service.dto.WsFlinkArtifactSqlDTO;
import cn.sliew.scaleph.project.service.convert.WsFlinkArtifactConvert;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WsFlinkArtifactSqlConvert extends BaseConvert<WsFlinkArtifactSql, WsFlinkArtifactSqlDTO> {
    WsFlinkArtifactSqlConvert INSTANCE = Mappers.getMapper(WsFlinkArtifactSqlConvert.class);

    @Override
    default WsFlinkArtifactSql toDo(WsFlinkArtifactSqlDTO dto) {
        WsFlinkArtifactSql entity = new WsFlinkArtifactSql();
        BeanUtils.copyProperties(dto, entity);
        entity.setWsFlinkArtifact(WsFlinkArtifactConvert.INSTANCE.toDo(dto.getWsFlinkArtifact()));
        entity.setFlinkArtifactId(dto.getWsFlinkArtifact().getId());
        return entity;
    }

    @Override
    default WsFlinkArtifactSqlDTO toDto(WsFlinkArtifactSql entity) {
        WsFlinkArtifactSqlDTO dto = new WsFlinkArtifactSqlDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setWsFlinkArtifact(WsFlinkArtifactConvert.INSTANCE.toDto(entity.getWsFlinkArtifact()));
        return dto;
    }
}
