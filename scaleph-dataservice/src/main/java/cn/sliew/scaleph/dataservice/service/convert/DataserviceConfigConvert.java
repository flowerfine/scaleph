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

package cn.sliew.scaleph.dataservice.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.entity.master.dataservice.DataserviceConfig;
import cn.sliew.scaleph.dataservice.service.dto.DataserviceConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataserviceConfigConvert extends BaseConvert<DataserviceConfig, DataserviceConfigDTO> {
    DataserviceConfigConvert INSTANCE = Mappers.getMapper(DataserviceConfigConvert.class);

    @Override
    default DataserviceConfig toDo(DataserviceConfigDTO dto) {
        DataserviceConfig entity = BeanUtil.copy(dto, new DataserviceConfig());
        if (dto.getParameterMap() != null) {
            entity.setParameterMapId(dto.getParameterMap().getId());
            entity.setParameterMap(DataserviceParameterMapConvert.INSTANCE.toDo(dto.getParameterMap()));
        }
        if (dto.getResultMap() != null) {
            entity.setResultMapId(dto.getResultMap().getId());
            entity.setResultMap(DataserviceResultMapConvert.INSTANCE.toDo(dto.getResultMap()));
        }
        return entity;
    }

    @Override
    default DataserviceConfigDTO toDto(DataserviceConfig entity) {
        DataserviceConfigDTO dto = BeanUtil.copy(entity, new DataserviceConfigDTO());
        if (entity.getParameterMap() != null) {
            dto.setParameterMap(DataserviceParameterMapConvert.INSTANCE.toDto(entity.getParameterMap()));
        }
        if (entity.getResultMap() != null) {
            dto.setResultMap(DataserviceResultMapConvert.INSTANCE.toDto(entity.getResultMap()));
        }
        return dto;
    }
}
