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

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.common.dict.job.JobAttrType;
import cn.sliew.scaleph.common.util.PropertyUtil;
import cn.sliew.scaleph.workspace.seatunnel.service.vo.DiJobAttrVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WsDiJobAttrVOConvert extends BaseConvert<JsonNode, DiJobAttrVO> {
    WsDiJobAttrVOConvert INSTANCE = Mappers.getMapper(WsDiJobAttrVOConvert.class);

    @Override
    default JsonNode toDo(DiJobAttrVO dto) {
        ObjectNode objectNode = JacksonUtil.createObjectNode();
        objectNode.putPOJO(JobAttrType.VARIABLE.getValue(), PropertyUtil.formatPropFromStr(dto.getJobAttr()));
        objectNode.putPOJO(JobAttrType.ENV.getValue(), PropertyUtil.formatPropFromStr(dto.getJobProp()));
        objectNode.putPOJO(JobAttrType.PROPERTIES.getValue(), PropertyUtil.formatPropFromStr(dto.getEngineProp()));
        return objectNode;
    }

    @Override
    default DiJobAttrVO toDto(JsonNode entity) {
        DiJobAttrVO vo = new DiJobAttrVO();
        if (entity == null) {
            return vo;
        }
        ObjectNode dagAttrs = (ObjectNode) entity;
        Map<String, String> variable = JacksonUtil.toObject(dagAttrs.get(JobAttrType.VARIABLE.getValue()), new TypeReference<Map<String, String>>() {});
        Map<String, String> env = JacksonUtil.toObject(dagAttrs.get(JobAttrType.ENV.getValue()), new TypeReference<Map<String, String>>() {});
        Map<String, String> properties = JacksonUtil.toObject(dagAttrs.get(JobAttrType.PROPERTIES.getValue()), new TypeReference<Map<String, String>>() {});
        vo.setJobAttr(PropertyUtil.mapToFormatProp(variable));
        vo.setJobProp(PropertyUtil.mapToFormatProp(env));
        vo.setEngineProp(PropertyUtil.mapToFormatProp(properties));
        return vo;
    }
}
