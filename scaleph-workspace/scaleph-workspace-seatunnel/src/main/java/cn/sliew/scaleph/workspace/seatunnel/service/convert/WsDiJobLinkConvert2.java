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

package cn.sliew.scaleph.workspace.seatunnel.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dag.service.dto.DagLinkDTO;
import cn.sliew.scaleph.workspace.seatunnel.service.dto.WsDiJobLinkDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WsDiJobLinkConvert2 extends BaseConvert<DagLinkDTO, WsDiJobLinkDTO> {
    WsDiJobLinkConvert2 INSTANCE = Mappers.getMapper(WsDiJobLinkConvert2.class);

    @Override
    default DagLinkDTO toDo(WsDiJobLinkDTO dto) {
        throw new UnsupportedOperationException();
    }

    @Override
    default WsDiJobLinkDTO toDto(DagLinkDTO entity) {
        WsDiJobLinkDTO dto = new WsDiJobLinkDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setLinkCode(entity.getLinkId());
        dto.setFromStepCode(entity.getFromStepId());
        dto.setToStepCode(entity.getToStepId());
        return dto;
    }
}
