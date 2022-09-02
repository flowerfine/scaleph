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

package cn.sliew.scaleph.resource.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.entity.master.resource.ResourceClusterCredential;
import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import cn.sliew.scaleph.resource.service.enums.ResourceType;
import cn.sliew.scaleph.resource.service.param.ClusterCredentialListParam;
import cn.sliew.scaleph.resource.service.param.ResourceListParam;
import cn.sliew.scaleph.resource.service.vo.ResourceVO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = {DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClusterCredentialConvert extends BaseConvert<ResourceClusterCredential, ClusterCredentialDTO> {
    ClusterCredentialConvert INSTANCE = Mappers.getMapper(ClusterCredentialConvert.class);

    @Override
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.RESOURCE_CLUSTER_TYPE,entity.getConfigType()))", target = "configType")
    ClusterCredentialDTO toDto(ResourceClusterCredential entity);

    default ClusterCredentialListParam convert(ResourceListParam param) {
        ClusterCredentialListParam target = BeanUtil.copy(param, new ClusterCredentialListParam());
        target.setConfigType(param.getLabel());
        target.setName(param.getName());
        return target;
    }

    default List<ResourceVO> convert(List<ClusterCredentialDTO> dtos) {
        return dtos.stream().map(this::convert).collect(Collectors.toList());
    }

    default ResourceVO convert(ClusterCredentialDTO dto) {
        ResourceVO target = BeanUtil.copy(dto, new ResourceVO());
        target.setType(ResourceType.CLUSTER_CREDENTIAL);
        target.setLabel(dto.getConfigType().getLabel());
        target.setName(dto.getName());
        return target;
    }
}
