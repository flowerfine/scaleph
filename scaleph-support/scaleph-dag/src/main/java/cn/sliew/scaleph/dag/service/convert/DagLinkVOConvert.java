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

package cn.sliew.scaleph.dag.service.convert;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dag.service.dto.DagLinkDTO;
import cn.sliew.scaleph.dao.entity.master.dag.DagLinkVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DagLinkVOConvert extends BaseConvert<DagLinkVO, DagLinkDTO> {
    DagLinkVOConvert INSTANCE = Mappers.getMapper(DagLinkVOConvert.class);

    @Override
    default DagLinkVO toDo(DagLinkDTO dto) {
        DagLinkVO entity = new DagLinkVO();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getDagConfigLink() != null) {
            entity.setDagConfigLink(DagConfigLinkConvert.INSTANCE.toDo(dto.getDagConfigLink()));
        }
        if (dto.getInputs() != null) {
            entity.setInputs(dto.getInputs().toString());
        }
        if (dto.getOutputs() != null) {
            entity.setOutputs(dto.getOutputs().toString());
        }
        return entity;
    }

    @Override
    default DagLinkDTO toDto(DagLinkVO entity) {
        DagLinkDTO dto = new DagLinkDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setDagConfigLink(DagConfigLinkConvert.INSTANCE.toDto(entity.getDagConfigLink()));
        if (StringUtils.hasText(entity.getInputs())) {
            dto.setInputs(JacksonUtil.toJsonNode(entity.getInputs()));
        }
        if (StringUtils.hasText(entity.getOutputs())) {
            dto.setOutputs(JacksonUtil.toJsonNode(entity.getOutputs()));
        }
        return dto;
    }
}
