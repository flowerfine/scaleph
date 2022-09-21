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
import cn.sliew.scaleph.common.dict.flink.FlinkResourceProvider;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.entity.master.resource.ResourceClusterCredential;
import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import cn.sliew.scaleph.resource.service.param.ClusterCredentialListParam;
import cn.sliew.scaleph.resource.service.param.ResourceListParam;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClusterCredentialConvert extends BaseConvert<ResourceClusterCredential, ClusterCredentialDTO> {
    ClusterCredentialConvert INSTANCE = Mappers.getMapper(ClusterCredentialConvert.class);

    default ClusterCredentialListParam convert(ResourceListParam param) {
        ClusterCredentialListParam target = BeanUtil.copy(param, new ClusterCredentialListParam());
        if (StringUtils.hasText(param.getLabel())) {
            target.setConfigType(FlinkResourceProvider.of(param.getLabel()));
        }
        target.setName(param.getName());
        return target;
    }

}
