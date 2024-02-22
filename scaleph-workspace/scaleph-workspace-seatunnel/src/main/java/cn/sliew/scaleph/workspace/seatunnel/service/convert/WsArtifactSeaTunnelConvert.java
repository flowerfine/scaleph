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

package cn.sliew.scaleph.workspace.seatunnel.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactSeaTunnel;
import cn.sliew.scaleph.workspace.project.service.convert.WsArtifactConvert;
import cn.sliew.scaleph.workspace.seatunnel.service.dto.WsArtifactSeaTunnelDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WsArtifactSeaTunnelConvert extends BaseConvert<WsArtifactSeaTunnel, WsArtifactSeaTunnelDTO> {
    WsArtifactSeaTunnelConvert INSTANCE = Mappers.getMapper(WsArtifactSeaTunnelConvert.class);

    @Override
    default WsArtifactSeaTunnel toDo(WsArtifactSeaTunnelDTO dto) {
        WsArtifactSeaTunnel entity = new WsArtifactSeaTunnel();
        BeanUtils.copyProperties(dto, entity);
        entity.setArtifact(WsArtifactConvert.INSTANCE.toDo(dto.getArtifact()));
        entity.setArtifactId(dto.getArtifact().getId());
        return entity;
    }

    @Override
    default WsArtifactSeaTunnelDTO toDto(WsArtifactSeaTunnel entity) {
        WsArtifactSeaTunnelDTO dto = new WsArtifactSeaTunnelDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setArtifact(WsArtifactConvert.INSTANCE.toDto(entity.getArtifact()));
        return dto;
    }
}
