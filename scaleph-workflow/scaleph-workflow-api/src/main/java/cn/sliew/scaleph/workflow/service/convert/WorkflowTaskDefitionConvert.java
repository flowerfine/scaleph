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

package cn.sliew.scaleph.workflow.service.convert;

import cn.sliew.carp.framework.dag.service.dto.DagConfigStepDTO;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskDefinitionAttrs;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskDefinitionDTO;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskDefinitionMeta;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WorkflowTaskDefitionConvert extends BaseConvert<DagConfigStepDTO, WorkflowTaskDefinitionDTO> {
    WorkflowTaskDefitionConvert INSTANCE = Mappers.getMapper(WorkflowTaskDefitionConvert.class);

    @Override
    default DagConfigStepDTO toDo(WorkflowTaskDefinitionDTO dto) {
        DagConfigStepDTO entity = new DagConfigStepDTO();
        BeanUtils.copyProperties(dto, entity);
        ObjectNode stepMeta = (ObjectNode) JacksonUtil.toJsonNode(dto.getStepMeta());
        stepMeta.putPOJO("type", dto.getStepMeta().getType().getValue());
        entity.setStepMeta(stepMeta);
        entity.setStepAttrs(JacksonUtil.toJsonNode(dto.getStepAttrs()));
        return entity;
    }

    @Override
    default WorkflowTaskDefinitionDTO toDto(DagConfigStepDTO entity) {
        WorkflowTaskDefinitionDTO dto = new WorkflowTaskDefinitionDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setStepMeta(JacksonUtil.toObject(entity.getStepMeta(), WorkflowTaskDefinitionMeta.class));
        dto.setStepAttrs(JacksonUtil.toObject(entity.getStepMeta(), WorkflowTaskDefinitionAttrs.class));
        return dto;
    }
}
