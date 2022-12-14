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

package cn.sliew.scaleph.meta.service.convert;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.constant.DictConstants;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.meta.MetaDatasource;
import cn.sliew.scaleph.meta.service.dto.MetaDatasourceDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import cn.sliew.scaleph.system.service.vo.DictVO;
import com.fasterxml.jackson.core.type.TypeReference;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@Mapper(uses = {DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetaDataSourceConvert extends BaseConvert<MetaDatasource, MetaDatasourceDTO> {
    MetaDataSourceConvert INSTANCE = Mappers.getMapper(MetaDataSourceConvert.class);

    @Override
    default MetaDatasource toDo(MetaDatasourceDTO dto) {
        if (dto == null) {
            return null;
        }
        MetaDatasource metaDatasource = new MetaDatasource();
        BeanUtils.copyProperties(dto, metaDatasource);
        metaDatasource.setDatasourceType(DictVoConvert.INSTANCE.toDo(dto.getDatasourceType()));
        if (CollectionUtils.isEmpty(dto.getProps()) == false) {
            metaDatasource.setProps(JacksonUtil.toJsonString(dto.getProps()));
        }
        if (CollectionUtils.isEmpty(dto.getAdditionalProps()) == false) {
            metaDatasource.setAdditionalProps(JacksonUtil.toJsonString(dto.getAdditionalProps()));
        }
        return metaDatasource;
    }

    @Override
    default MetaDatasourceDTO toDto(MetaDatasource entity) {
        if (entity == null) {
            return null;
        }
        MetaDatasourceDTO metaDatasourceDTO = new MetaDatasourceDTO();
        BeanUtils.copyProperties(entity, metaDatasourceDTO);
        metaDatasourceDTO.setDatasourceType(DictVO.toVO(DictConstants.DATASOURCE_TYPE, entity.getDatasourceType()));
        if (StringUtils.hasText(entity.getProps())) {
            metaDatasourceDTO.setProps(JacksonUtil.parseJsonString(entity.getProps(),
                    new TypeReference<Map<String, Object>>() {}));
            metaDatasourceDTO.setPropsStr(metaDatasourceDTO.getProps());
        }
        if (StringUtils.hasText(entity.getAdditionalProps())) {
            metaDatasourceDTO.setAdditionalProps(JacksonUtil.parseJsonString(entity.getAdditionalProps(),
                    new TypeReference<Map<String, Object>>() {}));
            metaDatasourceDTO.setAdditionalPropsStr(metaDatasourceDTO.getAdditionalProps());
        }
        return metaDatasourceDTO;
    }
}
