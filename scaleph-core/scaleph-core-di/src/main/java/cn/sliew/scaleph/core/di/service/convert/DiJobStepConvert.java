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

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import cn.sliew.scaleph.common.constant.DictConstants;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.core.di.service.dto.DiJobStepDTO;
import cn.sliew.scaleph.dao.entity.master.di.DiJobStep;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import cn.sliew.scaleph.system.service.vo.DictVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

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
        DiJobStep jobStep = new DiJobStep();
        jobStep.setId(dto.getId());
        jobStep.setJobId(dto.getJobId());
        jobStep.setStepCode(dto.getStepCode());
        jobStep.setStepTitle(dto.getStepTitle());
        jobStep.setStepType(DictVoConvert.INSTANCE.toDo(dto.getStepType()));
        jobStep.setStepName(dto.getStepName());
        jobStep.setPositionX(dto.getPositionX());
        jobStep.setPositionY(dto.getPositionY());
        jobStep.setStepAttrs(JSONUtil.toJsonStr(dto.getStepAttrs()));
        jobStep.setCreateTime(dto.getCreateTime());
        jobStep.setCreator(dto.getCreator());
        jobStep.setUpdateTime(dto.getUpdateTime());
        jobStep.setEditor(dto.getEditor());
        return jobStep;
    }

    @Override
    default DiJobStepDTO toDto(DiJobStep entity) {
        if (entity == null) {
            return null;
        }
        DiJobStepDTO dto = new DiJobStepDTO();
        dto.setId(entity.getId());
        dto.setJobId(entity.getJobId());
        dto.setStepCode(entity.getStepCode());
        dto.setStepTitle(entity.getStepTitle());
        dto.setStepType(DictVO.toVO(DictConstants.JOB_STEP_TYPE, entity.getStepType()));
        dto.setStepName(entity.getStepName());
        dto.setPositionX(entity.getPositionX());
        dto.setPositionY(entity.getPositionY());
        dto.setStepAttrs(JSONUtil.toBean(entity.getStepAttrs(), new TypeReference<Map<String, Object>>() {
        }.getType(), false));
        dto.setCreateTime(entity.getCreateTime());
        dto.setCreator(entity.getCreator());
        dto.setUpdateTime(entity.getUpdateTime());
        dto.setEditor(entity.getEditor());
        return dto;
    }
}
