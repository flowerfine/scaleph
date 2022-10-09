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
import cn.sliew.scaleph.dao.entity.master.flink.FlinkClusterConfigVO;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkClusterConfigDTO;
import cn.sliew.scaleph.engine.flink.service.dto.KubernetesOptions;
import cn.sliew.scaleph.resource.service.convert.ClusterCredentialConvert;
import cn.sliew.scaleph.resource.service.convert.FlinkReleaseConvert;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FlinkClusterConfigVOConvert extends BaseConvert<FlinkClusterConfigVO, FlinkClusterConfigDTO> {
    FlinkClusterConfigVOConvert INSTANCE = Mappers.getMapper(FlinkClusterConfigVOConvert.class);

    @Override
    default FlinkClusterConfigVO toDo(FlinkClusterConfigDTO dto) {
        FlinkClusterConfigVO entity = new FlinkClusterConfigVO();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getKubernetesOptions() != null) {
            entity.setKubernetesOptions(JacksonUtil.toJsonString(dto.getKubernetesOptions()));
        }
        if (CollectionUtils.isEmpty(dto.getConfigOptions()) == false) {
            entity.setConfigOptions(JacksonUtil.toJsonString(dto.getConfigOptions()));
        }
        return entity;
    }

    @Override
    default FlinkClusterConfigDTO toDto(FlinkClusterConfigVO entity) {
        FlinkClusterConfigDTO dto = new FlinkClusterConfigDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setFlinkRelease(FlinkReleaseConvert.INSTANCE.toDto(entity.getFlinkRelease()));
        dto.setClusterCredential(ClusterCredentialConvert.INSTANCE.toDto(entity.getClusterCredential()));
        if (StringUtils.hasText(entity.getKubernetesOptions())) {
            dto.setKubernetesOptions(JacksonUtil.parseJsonString(entity.getKubernetesOptions(), KubernetesOptions.class));
        }
        if (StringUtils.hasText(entity.getConfigOptions())) {
            dto.setConfigOptions(JacksonUtil.parseJsonString(entity.getConfigOptions(), Map.class));
        }
        return dto;
    }
}
