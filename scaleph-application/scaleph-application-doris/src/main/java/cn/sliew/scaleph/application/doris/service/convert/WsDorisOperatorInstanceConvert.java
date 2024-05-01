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

package cn.sliew.scaleph.application.doris.service.convert;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.application.doris.operator.status.CnStatus;
import cn.sliew.scaleph.application.doris.operator.status.ComponentStatus;
import cn.sliew.scaleph.application.doris.service.dto.WsDorisOperatorInstanceDTO;
import cn.sliew.scaleph.application.doris.operator.spec.*;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.ws.WsDorisOperatorInstance;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WsDorisOperatorInstanceConvert extends BaseConvert<WsDorisOperatorInstance, WsDorisOperatorInstanceDTO> {
    WsDorisOperatorInstanceConvert INSTANCE = Mappers.getMapper(WsDorisOperatorInstanceConvert.class);

    @Override
    default WsDorisOperatorInstance toDo(WsDorisOperatorInstanceDTO dto) {
        WsDorisOperatorInstance entity = new WsDorisOperatorInstance();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getAdmin() != null) {
            entity.setAdmin(JacksonUtil.toJsonString(dto.getAdmin()));
        }
        if (dto.getFeSpec() != null) {
            entity.setFeSpec(JacksonUtil.toJsonString(dto.getFeSpec()));
        }
        if (dto.getBeSpec() != null) {
            entity.setBeSpec(JacksonUtil.toJsonString(dto.getBeSpec()));
        }
        if (dto.getCnSpec() != null) {
            entity.setCnSpec(JacksonUtil.toJsonString(dto.getCnSpec()));
        }
        if (dto.getBrokerSpec() != null) {
            entity.setBrokerSpec(JacksonUtil.toJsonString(dto.getBrokerSpec()));
        }
        if (dto.getFeStatus() != null) {
            entity.setFeStatus(JacksonUtil.toJsonString(dto.getFeStatus()));
        }
        if (dto.getBeStatus() != null) {
            entity.setBeStatus(JacksonUtil.toJsonString(dto.getBeStatus()));
        }
        if (dto.getCnStatus() != null) {
            entity.setCnStatus(JacksonUtil.toJsonString(dto.getCnStatus()));
        }
        if (dto.getBrokerStatus() != null) {
            entity.setBrokerStatus(JacksonUtil.toJsonString(dto.getBrokerStatus()));
        }
        return entity;
    }

    @Override
    default WsDorisOperatorInstanceDTO toDto(WsDorisOperatorInstance entity) {
        WsDorisOperatorInstanceDTO dto = new WsDorisOperatorInstanceDTO();
        BeanUtils.copyProperties(entity, dto);
        if (StringUtils.hasText(entity.getAdmin())) {
            dto.setAdmin(JacksonUtil.parseJsonString(entity.getAdmin(), AdminUser.class));
        }
        if (StringUtils.hasText(entity.getFeSpec())) {
            dto.setFeSpec(JacksonUtil.parseJsonString(entity.getFeSpec(), FeSpec.class));
        }
        if (StringUtils.hasText(entity.getBeSpec())) {
            dto.setBeSpec(JacksonUtil.parseJsonString(entity.getBeSpec(), BeSpec.class));
        }
        if (StringUtils.hasText(entity.getCnSpec())) {
            dto.setCnSpec(JacksonUtil.parseJsonString(entity.getCnSpec(), CnSpec.class));
        }
        if (StringUtils.hasText(entity.getBrokerSpec())) {
            dto.setBrokerSpec(JacksonUtil.parseJsonString(entity.getBrokerSpec(), BrokerSpec.class));
        }
        if (StringUtils.hasText(entity.getFeStatus())) {
            dto.setFeStatus(JacksonUtil.parseJsonString(entity.getFeStatus(), ComponentStatus.class));
        }
        if (StringUtils.hasText(entity.getBeStatus())) {
            dto.setBeStatus(JacksonUtil.parseJsonString(entity.getBeStatus(), ComponentStatus.class));
        }
        if (StringUtils.hasText(entity.getCnStatus())) {
            dto.setCnStatus(JacksonUtil.parseJsonString(entity.getCnStatus(), CnStatus.class));
        }
        if (StringUtils.hasText(entity.getBrokerStatus())) {
            dto.setBrokerStatus(JacksonUtil.parseJsonString(entity.getBrokerStatus(), ComponentStatus.class));
        }
        return dto;
    }
}
