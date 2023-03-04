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
import cn.sliew.scaleph.kubernetes.ResourceConverter;
import com.fasterxml.jackson.databind.JsonNode;

public enum FlinkTemplateDefaultsConverter implements ResourceConverter<FlinkTemplateDefaults, FlinkTemplate> {
    INSTANCE;

    @Override
    public FlinkTemplate convertTo(FlinkTemplateDefaults source) {
        FlinkTemplate template = new FlinkTemplate();
        template.setMetadata(source.getMetadata());
        template.setSpec(source.getSpec());
        return template;
    }

    @Override
    public FlinkTemplateDefaults convertFrom(FlinkTemplate target) {
        FlinkTemplateDefaults defaults = new FlinkTemplateDefaults();
        defaults.setMetadata(target.getMetadata());
        JsonNode spec = JacksonUtil.toJsonNode(target.getSpec());
        defaults.setSpec(target.getSpec());
        return defaults;
    }
}
