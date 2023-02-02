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

package cn.sliew.scaleph.engine.flink.kubernetes.service.convert;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkKubernetesDeployment;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesDeploymentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WsFlinkKubernetesDeploymentConvert extends BaseConvert<WsFlinkKubernetesDeployment, WsFlinkKubernetesDeploymentDTO> {
    WsFlinkKubernetesDeploymentConvert INSTANCE = Mappers.getMapper(WsFlinkKubernetesDeploymentConvert.class);

    @Override
    default WsFlinkKubernetesDeployment toDo(WsFlinkKubernetesDeploymentDTO dto) {
        WsFlinkKubernetesDeployment entity = new WsFlinkKubernetesDeployment();
        BeanUtils.copyProperties(dto, entity);
        entity.setMetadata(dto.getMetadata().toString());
        entity.setSpec(dto.getSpec().toString());
        return entity;
    }

    @Override
    default WsFlinkKubernetesDeploymentDTO toDto(WsFlinkKubernetesDeployment entity) {
        WsFlinkKubernetesDeploymentDTO dto = new WsFlinkKubernetesDeploymentDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setMetadata(JacksonUtil.toJsonNode(entity.getMetadata()));
        dto.setSpec(JacksonUtil.toJsonNode(entity.getSpec()));
        return dto;
    }
}
