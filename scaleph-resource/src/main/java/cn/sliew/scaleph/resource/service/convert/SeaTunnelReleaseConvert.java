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
import cn.sliew.scaleph.dao.entity.master.resource.ResourceSeaTunnelRelease;
import cn.sliew.scaleph.resource.service.dto.SeaTunnelReleaseDTO;
import cn.sliew.scaleph.resource.service.param.ResourceListParam;
import cn.sliew.scaleph.resource.service.param.SeaTunnelReleaseListParam;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SeaTunnelReleaseConvert extends BaseConvert<ResourceSeaTunnelRelease, SeaTunnelReleaseDTO> {
    SeaTunnelReleaseConvert INSTANCE = Mappers.getMapper(SeaTunnelReleaseConvert.class);

    @Override
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.SEATUNNEL_VERSION,entity.getVersion()))", target = "version")
    SeaTunnelReleaseDTO toDto(ResourceSeaTunnelRelease entity);

    default SeaTunnelReleaseListParam convert(ResourceListParam param) {
        SeaTunnelReleaseListParam target = BeanUtil.copy(param, new SeaTunnelReleaseListParam());
        target.setVersion(param.getLabel());
        target.setFileName(param.getName());
        return target;
    }

}
