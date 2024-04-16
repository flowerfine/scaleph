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
import cn.sliew.scaleph.dag.service.dto.DagConfigLinkDTO;
import cn.sliew.scaleph.dao.entity.master.dag.DagConfigLink;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DagConfigLinkConvert extends BaseConvert<DagConfigLink, DagConfigLinkDTO> {
    DagConfigLinkConvert INSTANCE = Mappers.getMapper(DagConfigLinkConvert.class);

    @Override
    default DagConfigLink toDo(DagConfigLinkDTO dto) {
        DagConfigLink entity = new DagConfigLink();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getStyle() != null) {
            entity.setStyle(dto.getStyle().toString());
        }
        if (dto.getLinkMeta() != null) {
            entity.setLinkMeta(dto.getLinkMeta().toString());
        }
        if (dto.getLinkAttrs() != null) {
            entity.setLinkAttrs(dto.getLinkAttrs().toString());
        }
        return entity;
    }

    @Override
    default DagConfigLinkDTO toDto(DagConfigLink entity) {
        DagConfigLinkDTO dto = new DagConfigLinkDTO();
        BeanUtils.copyProperties(entity, dto);
        if (StringUtils.hasText(entity.getStyle())) {
            dto.setStyle(JacksonUtil.toJsonNode(entity.getStyle()));
        }
        if (StringUtils.hasText(entity.getLinkMeta())) {
            dto.setLinkMeta(JacksonUtil.toJsonNode(entity.getLinkMeta()));
        }
        if (StringUtils.hasText(entity.getLinkAttrs())) {
            dto.setLinkAttrs(JacksonUtil.toJsonNode(entity.getLinkAttrs()));
        }
        return dto;
    }
}
