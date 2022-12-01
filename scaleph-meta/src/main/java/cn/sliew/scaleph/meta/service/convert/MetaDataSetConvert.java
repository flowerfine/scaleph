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

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.meta.MetaDataSet;
import cn.sliew.scaleph.meta.service.dto.MetaDataSetDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class, MetaSystemConvert.class,
        MetaDataSetTypeConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetaDataSetConvert extends BaseConvert<MetaDataSet, MetaDataSetDTO> {
    MetaDataSetConvert INSTANCE = Mappers.getMapper(MetaDataSetConvert.class);

    @Override
    @Mapping(source = "dataSetType", target = "dataSetType")
    @Mapping(source = "system", target = "system")
    MetaDataSetDTO toDto(MetaDataSet entity);

    @Override
    @Mapping(source = "dataSetType.id", target = "dataSetTypeId")
    @Mapping(source = "system.id", target = "systemId")
    MetaDataSet toDo(MetaDataSetDTO dto);
}
