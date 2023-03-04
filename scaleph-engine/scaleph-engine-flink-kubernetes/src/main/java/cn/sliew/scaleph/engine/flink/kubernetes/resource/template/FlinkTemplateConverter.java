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

package cn.sliew.scaleph.engine.flink.kubernetes.resource.template;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesTemplateDTO;
import cn.sliew.scaleph.kubernetes.ResourceConverter;
import io.fabric8.kubernetes.api.model.ObjectMeta;

public enum FlinkTemplateConverter implements ResourceConverter<WsFlinkKubernetesTemplateDTO, FlinkTemplate> {
    INSTANCE;

    @Override
    public FlinkTemplate convertTo(WsFlinkKubernetesTemplateDTO source) {
        FlinkTemplate template = new FlinkTemplate();
        template.setMetadata(JacksonUtil.toObject(source.getMetadata(), ObjectMeta.class));
        template.setSpec(JacksonUtil.toObject(source.getSpec(), FlinkTemplateSpec.class));
        return template;
    }

    @Override
    public WsFlinkKubernetesTemplateDTO convertFrom(FlinkTemplate target) {
        WsFlinkKubernetesTemplateDTO dto = new WsFlinkKubernetesTemplateDTO();
        dto.setName(target.getMetadata().getName());
        dto.setMetadata(JacksonUtil.toJsonNode(target.getMetadata()));
        dto.setSpec(JacksonUtil.toJsonNode(target.getSpec()));
        return dto;
    }
}
