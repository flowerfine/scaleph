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

package cn.sliew.scaleph.security.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.entity.master.security.SecDept;
import cn.sliew.scaleph.security.service.dto.SecDeptDTO;
import cn.sliew.scaleph.security.service.dto.SecDeptTreeDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gleiyu
 */
@Mapper(uses = DictVoConvert.class)
public interface SecDeptConvert extends BaseConvert<SecDept, SecDeptDTO> {
    SecDeptConvert INSTANCE = Mappers.getMapper(SecDeptConvert.class);

    default List<SecDeptTreeDTO> entity2TreeDTO(List<SecDept> entityList) {
        return entityList.stream().map(entity -> BeanUtil.copy(entity, new SecDeptTreeDTO())).collect(Collectors.toList());
    }

    default List<SecDeptTreeDTO> dto2TreeDTO(List<SecDeptDTO> dtoList) {
        return dtoList.stream()
                .map(dto -> BeanUtil.copy(dto, new SecDeptTreeDTO()))
                .collect(Collectors.toList());
    }

}
