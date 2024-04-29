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

package cn.sliew.scaleph.application.oam.service.convert;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.application.oam.model.common.DefinitionRef;
import cn.sliew.scaleph.application.oam.model.common.MetaData;
import cn.sliew.scaleph.application.oam.model.common.OamConstants;
import cn.sliew.scaleph.application.oam.model.common.Schematic;
import cn.sliew.scaleph.application.oam.model.definition.TraitDefinition;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.oam.OamTraitDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OamTraitDefinitionConvert extends BaseConvert<OamTraitDefinition, TraitDefinition> {
    OamTraitDefinitionConvert INSTANCE = Mappers.getMapper(OamTraitDefinitionConvert.class);

    @Override
    default OamTraitDefinition toDo(TraitDefinition dto) {
        OamTraitDefinition entity = new OamTraitDefinition();
        MetaData metadata = dto.getMetadata();
        entity.setDefinitionId(metadata.getName());
        Map<String, String> annotations = metadata.getAnnotations();
        entity.setName(annotations.get(OamConstants.OAM_ANNOTATION_NAME));
        entity.setRemark(annotations.get(OamConstants.OAM_ANNOTATION_DESCRIPTION));

        TraitDefinition.Spec spec = dto.getSpec();
        if (spec.getDefinitionRef() != null) {
            entity.setDefinitionRef(JacksonUtil.toJsonString(spec.getDefinitionRef()));
        }
        if (CollectionUtils.isEmpty(spec.getAppliesToWorkloads()) == false) {
            entity.setAppliesToWorkloads(JacksonUtil.toJsonString(spec.getAppliesToWorkloads()));
        }
        if (spec.getSchematic() != null) {
            entity.setSchematic(JacksonUtil.toJsonString(spec.getSchematic()));
        }
        // todo properties
        return entity;
    }

    @Override
    default TraitDefinition toDto(OamTraitDefinition entity) {
        TraitDefinition dto = new TraitDefinition();
        dto.setApiVersion(OamConstants.OAM_API_VERSION);
        dto.setKind(OamConstants.OAM_KIND_TRAIT_DEFINITION);
        MetaData metaData = new MetaData();
        metaData.setName(entity.getDefinitionId());
        Map<String, String> annotations = new HashMap<>();
        annotations.put(OamConstants.OAM_ANNOTATION_NAME, entity.getName());
        annotations.put(OamConstants.OAM_ANNOTATION_DESCRIPTION, entity.getRemark());
        metaData.setAnnotations(annotations);
        dto.setMetadata(metaData);

        TraitDefinition.Spec spec = new TraitDefinition.Spec();
        if (StringUtils.hasText(entity.getDefinitionRef())) {
            spec.setDefinitionRef(JacksonUtil.parseJsonString(entity.getDefinitionRef(), DefinitionRef.class));
        }
        if (StringUtils.hasText(entity.getAppliesToWorkloads())) {
            spec.setAppliesToWorkloads(JacksonUtil.parseJsonArray(entity.getAppliesToWorkloads(), String.class));
        }
        if (StringUtils.hasText(entity.getSchematic())) {
            spec.setSchematic(JacksonUtil.parseJsonString(entity.getSchematic(), Schematic.class));
        }
        //todo properties
        dto.setSpec(spec);
        return dto;
    }
}
