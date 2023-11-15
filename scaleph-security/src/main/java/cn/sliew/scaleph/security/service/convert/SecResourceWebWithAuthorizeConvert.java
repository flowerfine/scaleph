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
import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.dao.entity.master.security.SecResourceWebVO;
import cn.sliew.scaleph.security.service.dto.SecResourceWebWithAuthorizeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

@Mapper
public interface SecResourceWebWithAuthorizeConvert extends BaseConvert<SecResourceWebVO, SecResourceWebWithAuthorizeDTO> {

    SecResourceWebWithAuthorizeConvert INSTANCE = Mappers.getMapper(SecResourceWebWithAuthorizeConvert.class);

    @Override
    default SecResourceWebVO toDo(SecResourceWebWithAuthorizeDTO dto) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SecResourceWebWithAuthorizeDTO toDto(SecResourceWebVO entity) {
        SecResourceWebWithAuthorizeDTO dto = new SecResourceWebWithAuthorizeDTO();
        BeanUtils.copyProperties(SecResourceWebConvert.INSTANCE.toDto(entity), dto);
        dto.setAuthorized(entity.getRoleId() != null ? YesOrNo.YES : YesOrNo.NO);
        return dto;
    }
}
