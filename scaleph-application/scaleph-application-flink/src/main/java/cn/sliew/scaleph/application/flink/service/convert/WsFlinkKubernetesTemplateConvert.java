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

package cn.sliew.scaleph.application.flink.service.convert;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesTemplateDTO;
import cn.sliew.scaleph.application.flink.service.vo.KubernetesOptionsVO;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkKubernetesTemplate;
import cn.sliew.scaleph.application.flink.operator.spec.IngressSpec;
import cn.sliew.scaleph.application.flink.operator.spec.TaskManagerSpec;
import cn.sliew.scaleph.application.flink.operator.spec.JobManagerSpec;
import io.fabric8.kubernetes.api.model.Pod;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WsFlinkKubernetesTemplateConvert extends BaseConvert<WsFlinkKubernetesTemplate, WsFlinkKubernetesTemplateDTO> {
    WsFlinkKubernetesTemplateConvert INSTANCE = Mappers.getMapper(WsFlinkKubernetesTemplateConvert.class);

    @Override
    default WsFlinkKubernetesTemplate toDo(WsFlinkKubernetesTemplateDTO dto) {
        WsFlinkKubernetesTemplate entity = new WsFlinkKubernetesTemplate();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getKubernetesOptions() != null) {
            entity.setKubernetesOptions(JacksonUtil.toJsonString(dto.getKubernetesOptions()));
        }
        if (dto.getJobManager() != null) {
            entity.setJobManager(JacksonUtil.toJsonString(dto.getJobManager()));
        }
        if (dto.getTaskManager() != null) {
            entity.setTaskManager(JacksonUtil.toJsonString(dto.getTaskManager()));
        }
        if (dto.getPodTemplate() != null) {
            entity.setPodTemplate(JacksonUtil.toJsonString(dto.getPodTemplate()));
        }
        if (CollectionUtils.isEmpty(dto.getFlinkConfiguration()) == false) {
            entity.setFlinkConfiguration(JacksonUtil.toJsonString(dto.getFlinkConfiguration()));
        }
        if (CollectionUtils.isEmpty(dto.getLogConfiguration()) == false) {
            entity.setLogConfiguration(JacksonUtil.toJsonString(dto.getLogConfiguration()));
        }
        if (dto.getIngress() != null) {
            entity.setIngress(JacksonUtil.toJsonString(dto.getIngress()));
        }
        if (dto.getAdditionalDependencies() != null) {
            entity.setAdditionalDependencies(JacksonUtil.toJsonString(dto.getAdditionalDependencies()));
        }
        return entity;
    }

    @Override
    default WsFlinkKubernetesTemplateDTO toDto(WsFlinkKubernetesTemplate entity) {
        WsFlinkKubernetesTemplateDTO dto = new WsFlinkKubernetesTemplateDTO();
        BeanUtils.copyProperties(entity, dto);
        if (StringUtils.hasText(entity.getKubernetesOptions())) {
            dto.setKubernetesOptions(JacksonUtil.parseJsonString(entity.getKubernetesOptions(), KubernetesOptionsVO.class));
        }
        if (StringUtils.hasText(entity.getJobManager())) {
            dto.setJobManager(JacksonUtil.parseJsonString(entity.getJobManager(), JobManagerSpec.class));
        }
        if (StringUtils.hasText(entity.getTaskManager())) {
            dto.setTaskManager(JacksonUtil.parseJsonString(entity.getTaskManager(), TaskManagerSpec.class));
        }
        if (StringUtils.hasText(entity.getPodTemplate())) {
            dto.setPodTemplate(JacksonUtil.parseJsonString(entity.getPodTemplate(), Pod.class));
        }
        if (StringUtils.hasText(entity.getFlinkConfiguration())) {
            dto.setFlinkConfiguration(JacksonUtil.parseJsonString(entity.getFlinkConfiguration(), Map.class));
        }
        if (StringUtils.hasText(entity.getLogConfiguration())) {
            dto.setLogConfiguration(JacksonUtil.parseJsonString(entity.getLogConfiguration(), Map.class));
        }
        if (StringUtils.hasText(entity.getIngress())) {
            dto.setIngress(JacksonUtil.parseJsonString(entity.getIngress(), IngressSpec.class));
        }
        if (StringUtils.hasText(entity.getAdditionalDependencies())) {
            dto.setAdditionalDependencies(JacksonUtil.parseJsonArray(entity.getAdditionalDependencies(), Long.TYPE));
        }
        return dto;
    }
}
