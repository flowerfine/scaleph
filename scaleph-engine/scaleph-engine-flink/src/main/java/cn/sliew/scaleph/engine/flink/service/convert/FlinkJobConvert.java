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

package cn.sliew.scaleph.engine.flink.service.convert;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.flink.FlinkJob;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FlinkJobConvert extends BaseConvert<FlinkJob, FlinkJobDTO> {
    FlinkJobConvert INSTANCE = Mappers.getMapper(FlinkJobConvert.class);

    @Override
    default FlinkJob toDo(FlinkJobDTO dto) {
        FlinkJob entity = new FlinkJob();
        BeanUtils.copyProperties(dto, entity);
        if (CollectionUtils.isEmpty(dto.getJobConfig()) == false) {
            entity.setJobConfig(JacksonUtil.toJsonString(dto.getJobConfig()));
        }
        if (CollectionUtils.isEmpty(dto.getFlinkConfig()) == false) {
            entity.setFlinkConfig(JacksonUtil.toJsonString(dto.getFlinkConfig()));
        }
        if (CollectionUtils.isEmpty(dto.getJars()) == false) {
            entity.setJars(JacksonUtil.toJsonString(dto.getJars()));
        }
        return entity;
    }

    @Override
    default FlinkJobDTO toDto(FlinkJob entity) {
        FlinkJobDTO dto = new FlinkJobDTO();
        BeanUtils.copyProperties(entity, dto);
        if (StringUtils.hasText(entity.getJobConfig())) {
            dto.setJobConfig(JacksonUtil.parseJsonString(entity.getJobConfig(), Map.class));
        }
        if (StringUtils.hasText(entity.getFlinkConfig())) {
            dto.setFlinkConfig(JacksonUtil.parseJsonString(entity.getFlinkConfig(), Map.class));
        }
        if (StringUtils.hasText(entity.getJars())) {
            dto.setJars(JacksonUtil.parseJsonArray(entity.getJars(), Long.TYPE));
        }
        return dto;
    }
}
