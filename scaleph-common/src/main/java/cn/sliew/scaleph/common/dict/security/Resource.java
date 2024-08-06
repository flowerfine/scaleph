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

package cn.sliew.scaleph.common.dict.security;

import cn.sliew.carp.framework.common.dict.DictInstance;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;
import java.util.Optional;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Resource implements DictInstance {

    STUDIO(null, ResourceType.NAV, "studio", "工作台"),
    PROJECT(null, ResourceType.NAV, "project", "项目"),
    RESOURCE(null, ResourceType.NAV, "resource", "资源"),
    DATASOURCE(null, ResourceType.NAV, "datasource", "数据源"),
    META(null, ResourceType.NAV, "meta", "数据元"),
    SYSTEM(null, ResourceType.NAV, "system", "系统管理"),

    ;

    @JsonCreator
    public static Resource of(String value) {
        return Arrays.stream(values())
                .filter(instance -> instance.getValue().equals(value))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(Resource.class, value));
    }

    private Resource parent;
    private ResourceType type;
    @EnumValue
    private String value;
    private String label;

    Resource(Resource parent, ResourceType type, String value, String label) {
        this.parent = parent;
        this.type = type;
        this.value = value;
        this.label = label;
    }

    public Optional<Resource> getParent() {
        return Optional.ofNullable(parent);
    }

    public ResourceType getType() {
        return type;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public String getFullValue() {
        Optional<Resource> optional = getParent();
        if (optional.isPresent()) {
            return optional.get().getFullValue() + getValue();
        } else {
            return getValue();
        }
    }
}
