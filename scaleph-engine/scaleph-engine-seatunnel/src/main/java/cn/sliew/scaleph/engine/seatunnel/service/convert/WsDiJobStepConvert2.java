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

package cn.sliew.scaleph.engine.seatunnel.service.convert;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginName;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginType;
import cn.sliew.scaleph.dag.service.dto.DagStepDTO;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobStepDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import com.fasterxml.jackson.core.type.TypeReference;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

import java.util.Map;

@Mapper(uses = {DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WsDiJobStepConvert2 extends BaseConvert<DagStepDTO, WsDiJobStepDTO> {
    WsDiJobStepConvert2 INSTANCE = Mappers.getMapper(WsDiJobStepConvert2.class);

    @Override
    default DagStepDTO toDo(WsDiJobStepDTO dto) {
        throw new UnsupportedOperationException();
    }

    @Override
    default WsDiJobStepDTO toDto(DagStepDTO entity) {
        WsDiJobStepDTO dto = new WsDiJobStepDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setStepCode(entity.getStepId());
        dto.setStepTitle(entity.getStepName());
        dto.setPositionX(entity.getPositionX());
        dto.setPositionY(entity.getPositionY());
        Map<String, Object> stepAttrs = JacksonUtil.toObject(entity.getStepAttrs(), new TypeReference<Map<String, Object>>() {});
        dto.setStepAttrs(stepAttrs);
        dto.setStepType(SeaTunnelPluginType.of(stepAttrs.get("type").toString()));
        dto.setStepName(SeaTunnelPluginName.of(stepAttrs.get("name").toString()));
        return dto;
    }
}
