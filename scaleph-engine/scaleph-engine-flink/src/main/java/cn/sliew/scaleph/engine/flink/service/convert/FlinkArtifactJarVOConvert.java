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

import cn.sliew.scaleph.common.constant.DictConstants;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.flink.FlinkArtifactJarVO;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkArtifactJarDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import cn.sliew.scaleph.system.service.vo.DictVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FlinkArtifactJarVOConvert extends BaseConvert<FlinkArtifactJarVO, FlinkArtifactJarDTO> {
    FlinkArtifactJarVOConvert INSTANCE = Mappers.getMapper(FlinkArtifactJarVOConvert.class);

    @Override
    default FlinkArtifactJarVO toDo(FlinkArtifactJarDTO dto) {
        FlinkArtifactJarVO entity = new FlinkArtifactJarVO();
        BeanUtils.copyProperties(dto, entity);
        entity.setFlinkArtifact(FlinkArtifactConvert.INSTANCE.toDo(dto.getFlinkArtifact()));
        entity.setFlinkVersion(DictVoConvert.INSTANCE.toDo(dto.getFlinkVersion()));
        return entity;
    }

    @Override
    default FlinkArtifactJarDTO toDto(FlinkArtifactJarVO entity) {
        FlinkArtifactJarDTO dto = new FlinkArtifactJarDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setFlinkArtifact(FlinkArtifactConvert.INSTANCE.toDto(entity.getFlinkArtifact()));
        dto.setFlinkVersion(DictVO.toVO(DictConstants.FLINK_VERSION, entity.getFlinkVersion()));
        return dto;
    }
}
