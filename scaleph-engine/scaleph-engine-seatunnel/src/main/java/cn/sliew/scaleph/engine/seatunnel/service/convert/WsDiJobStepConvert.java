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
import cn.sliew.scaleph.system.util.BeanUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsDiJobStep;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobStepDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import com.fasterxml.jackson.core.type.TypeReference;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WsDiJobStepConvert extends BaseConvert<WsDiJobStep, WsDiJobStepDTO> {
    WsDiJobStepConvert INSTANCE = Mappers.getMapper(WsDiJobStepConvert.class);

    @Override
    default WsDiJobStep toDo(WsDiJobStepDTO dto) {
        if (dto == null) {
            return null;
        }
        WsDiJobStep jobStep = BeanUtil.copy(dto, new WsDiJobStep());
        if (CollectionUtils.isEmpty(dto.getStepAttrs()) == false) {
            jobStep.setStepAttrs(JacksonUtil.toJsonString(dto.getStepAttrs()));
        }
        return jobStep;
    }

    @Override
    default WsDiJobStepDTO toDto(WsDiJobStep entity) {
        if (entity == null) {
            return null;
        }
        WsDiJobStepDTO dto = BeanUtil.copy(entity, new WsDiJobStepDTO());
        if (StringUtils.hasText(entity.getStepAttrs())) {
            dto.setStepAttrs(JacksonUtil.parseJsonString(entity.getStepAttrs(), new TypeReference<Map<String, Object>>() {
            }));
        }
        return dto;
    }
}
