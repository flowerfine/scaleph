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

package cn.sliew.scaleph.catalog.service.convert;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.catalog.service.dto.CatalogDatabaseDTO;
import cn.sliew.scaleph.common.codec.CodecUtil;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.sakura.CatalogDatabase;
import com.fasterxml.jackson.core.type.TypeReference;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.BeanUtils;

import java.util.Map;

@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatalogDatabaseConvert extends BaseConvert<CatalogDatabase, CatalogDatabaseDTO> {

    @Override
    default CatalogDatabase toDo(CatalogDatabaseDTO dto) {
        CatalogDatabase entity = new CatalogDatabase();
        BeanUtils.copyProperties(dto, entity);
        entity.setProperties(CodecUtil.encrypt(JacksonUtil.toJsonString(dto.getProperties())));
        return entity;
    }

    @Override
    default CatalogDatabaseDTO toDto(CatalogDatabase entity) {
        CatalogDatabaseDTO dto = new CatalogDatabaseDTO();
        BeanUtils.copyProperties(entity, dto);
        Map<String, String> properties = JacksonUtil.parseJsonString(CodecUtil.decrypt(entity.getProperties()), new TypeReference<Map<String, String>>() {});
        dto.setProperties(properties);
        return dto;
    }
}
