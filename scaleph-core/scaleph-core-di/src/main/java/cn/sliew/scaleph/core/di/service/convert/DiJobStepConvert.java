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

package cn.sliew.scaleph.core.di.service.convert;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.core.di.service.dto.DiJobStepDTO;
import cn.sliew.scaleph.dao.entity.master.di.DiJobStep;
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
public interface DiJobStepConvert extends BaseConvert<DiJobStep, DiJobStepDTO> {
    DiJobStepConvert INSTANCE = Mappers.getMapper(DiJobStepConvert.class);

    @Override
    default DiJobStep toDo(DiJobStepDTO dto) {
        if (dto == null) {
            return null;
        }
        DiJobStep jobStep = BeanUtil.copy(dto, new DiJobStep());
        if (CollectionUtils.isEmpty(dto.getStepAttrs()) == false) {
            jobStep.setStepAttrs(JacksonUtil.toJsonString(dto.getStepAttrs()));
        }
        return jobStep;
    }

    @Override
    default DiJobStepDTO toDto(DiJobStep entity) {
        if (entity == null) {
            return null;
        }
        DiJobStepDTO dto = BeanUtil.copy(entity, new DiJobStepDTO());
        if (StringUtils.hasText(entity.getStepAttrs())) {
            dto.setStepAttrs(JacksonUtil.parseJsonString(entity.getStepAttrs(), new TypeReference<Map<String, Object>>() {
            }));
        }
        return dto;
    }
}
