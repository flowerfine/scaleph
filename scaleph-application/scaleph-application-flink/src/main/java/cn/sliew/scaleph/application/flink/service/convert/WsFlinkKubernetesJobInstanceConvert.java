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
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkKubernetesJobInstance;
import cn.sliew.scaleph.application.flink.operator.spec.TaskManagerSpec;
import cn.sliew.scaleph.application.flink.operator.spec.JobManagerSpec;
import cn.sliew.scaleph.application.flink.operator.status.TaskManagerInfo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WsFlinkKubernetesJobInstanceConvert extends BaseConvert<WsFlinkKubernetesJobInstance, WsFlinkKubernetesJobInstanceDTO> {
    WsFlinkKubernetesJobInstanceConvert INSTANCE = Mappers.getMapper(WsFlinkKubernetesJobInstanceConvert.class);

    @Override
    default WsFlinkKubernetesJobInstance toDo(WsFlinkKubernetesJobInstanceDTO dto) {
        WsFlinkKubernetesJobInstance entity = new WsFlinkKubernetesJobInstance();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getWsFlinkKubernetesJob() != null) {
            entity.setWsFlinkKubernetesJob(WsFlinkKubernetesJobConvert.INSTANCE.toDo(dto.getWsFlinkKubernetesJob()));
        }
        if (dto.getJobManager() != null) {
            entity.setJobManager(JacksonUtil.toJsonString(dto.getJobManager()));
        }
        if (dto.getTaskManager() != null) {
            entity.setTaskManager(JacksonUtil.toJsonString(dto.getTaskManager()));
        }
        if (CollectionUtils.isEmpty(dto.getUserFlinkConfiguration()) == false) {
            entity.setUserFlinkConfiguration(JacksonUtil.toJsonString(dto.getUserFlinkConfiguration()));
        }
        if (CollectionUtils.isEmpty(dto.getMergedFlinkConfiguration()) == false) {
            entity.setMergedFlinkConfiguration(JacksonUtil.toJsonString(dto.getMergedFlinkConfiguration()));
        }
        if (CollectionUtils.isEmpty(dto.getClusterInfo()) == false) {
            entity.setClusterInfo(JacksonUtil.toJsonString(dto.getClusterInfo()));
        }
        if (dto.getTaskManagerInfo() != null) {
            entity.setTaskManagerInfo(JacksonUtil.toJsonString(dto.getTaskManagerInfo()));
        }
        return entity;
    }

    @Override
    default WsFlinkKubernetesJobInstanceDTO toDto(WsFlinkKubernetesJobInstance entity) {
        WsFlinkKubernetesJobInstanceDTO dto = new WsFlinkKubernetesJobInstanceDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getWsFlinkKubernetesJob() != null) {
            dto.setWsFlinkKubernetesJob(WsFlinkKubernetesJobConvert.INSTANCE.toDto(entity.getWsFlinkKubernetesJob()));
        }
        if (StringUtils.hasText(entity.getJobManager())) {
            dto.setJobManager(JacksonUtil.parseJsonString(entity.getJobManager(), JobManagerSpec.class));
        }
        if (StringUtils.hasText(entity.getTaskManager())) {
            dto.setTaskManager(JacksonUtil.parseJsonString(entity.getTaskManager(), TaskManagerSpec.class));
        }
        if (StringUtils.hasText(entity.getUserFlinkConfiguration())) {
            dto.setUserFlinkConfiguration(JacksonUtil.parseJsonString(entity.getUserFlinkConfiguration(), Map.class));
        }
        if (StringUtils.hasText(entity.getMergedFlinkConfiguration())) {
            dto.setMergedFlinkConfiguration(JacksonUtil.parseJsonString(entity.getMergedFlinkConfiguration(), Map.class));
        }
        if (StringUtils.hasText(entity.getClusterInfo())) {
            dto.setClusterInfo(JacksonUtil.parseJsonString(entity.getClusterInfo(), Map.class));
        }
        if (StringUtils.hasText(entity.getTaskManagerInfo())) {
            dto.setTaskManagerInfo(JacksonUtil.parseJsonString(entity.getTaskManagerInfo(), TaskManagerInfo.class));
        }
        return dto;
    }
}
