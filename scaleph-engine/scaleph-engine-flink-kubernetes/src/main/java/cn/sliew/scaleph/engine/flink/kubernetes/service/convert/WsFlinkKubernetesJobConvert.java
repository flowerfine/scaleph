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

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkKubernetesJob;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WsFlinkKubernetesJobConvert extends BaseConvert<WsFlinkKubernetesJob, WsFlinkKubernetesJobDTO> {
    WsFlinkKubernetesJobConvert INSTANCE = Mappers.getMapper(WsFlinkKubernetesJobConvert.class);

    @Override
    default WsFlinkKubernetesJob toDo(WsFlinkKubernetesJobDTO dto) {
        WsFlinkKubernetesJob entity = new WsFlinkKubernetesJob();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getFlinkDeployment() != null) {
            entity.setFlinkDeploymentId(dto.getFlinkDeployment().getId());
            entity.setFlinkDeployment(WsFlinkKubernetesDeploymentConvert.INSTANCE.toDo(dto.getFlinkDeployment()));
        }
        if (dto.getFlinkSessionCluster() != null) {
            entity.setFlinkSessionClusterId(dto.getFlinkSessionCluster().getId());
            entity.setFlinkSessionCluster(WsFlinkKubernetesSessionClusterConvert.INSTANCE.toDo(dto.getFlinkSessionCluster()));
        }
        if (dto.getFlinkArtifactJar() != null) {
            entity.setFlinkArtifactJarId(dto.getFlinkArtifactJar().getId());
        }
        if (dto.getFlinkArtifactSql() != null) {
            entity.setFlinkArtifactSqlId(dto.getFlinkArtifactSql().getId());
        }
        return entity;
    }

    @Override
    default WsFlinkKubernetesJobDTO toDto(WsFlinkKubernetesJob entity) {
        WsFlinkKubernetesJobDTO dto = new WsFlinkKubernetesJobDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getFlinkDeployment() != null) {
            dto.setFlinkDeployment(WsFlinkKubernetesDeploymentConvert.INSTANCE.toDto(entity.getFlinkDeployment()));
        }
        if (entity.getFlinkSessionCluster() != null) {
            dto.setFlinkSessionCluster(WsFlinkKubernetesSessionClusterConvert.INSTANCE.toDto(entity.getFlinkSessionCluster()));
        }
        return dto;
    }
}
