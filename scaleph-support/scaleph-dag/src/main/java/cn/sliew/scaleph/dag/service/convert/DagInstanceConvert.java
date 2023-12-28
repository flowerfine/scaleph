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
import cn.sliew.scaleph.dag.service.dto.DagInstanceDTO;
import cn.sliew.scaleph.dao.entity.master.dag.DagInstance;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DagInstanceConvert extends BaseConvert<DagInstance, DagInstanceDTO> {
    DagInstanceConvert INSTANCE = Mappers.getMapper(DagInstanceConvert.class);

    @Override
    default DagInstance toDo(DagInstanceDTO dto) {
        DagInstance entity = new DagInstance();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getDagMeta() != null) {
            entity.setDagMeta(dto.getDagMeta().toString());
        }
        if (dto.getDagAttrs() != null) {
            entity.setDagAttrs(dto.getDagAttrs().toString());
        }
        return entity;
    }

    @Override
    default DagInstanceDTO toDto(DagInstance entity) {
        DagInstanceDTO dto = new DagInstanceDTO();
        BeanUtils.copyProperties(entity, dto);
        if (StringUtils.hasText(entity.getDagMeta())) {
            dto.setDagMeta(JacksonUtil.toJsonNode(entity.getDagMeta()));
        }
        if (StringUtils.hasText(entity.getDagAttrs())) {
            dto.setDagAttrs(JacksonUtil.toJsonNode(entity.getDagAttrs()));
        }
        return dto;
    }
}
