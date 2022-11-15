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

package cn.sliew.scaleph.workflow.engine.action;

import cn.sliew.milky.common.constant.Attribute;
import cn.sliew.milky.common.constant.AttributeKey;
import cn.sliew.milky.common.constant.AttributeMap;
import cn.sliew.milky.common.constant.DefaultAttributeMap;
import cn.sliew.scaleph.common.container.pool.ContainerPool;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Data
public class ActionContext implements AttributeMap {

    private Long workflowDefinitionId;

    private Long workflowTaskDefinitionId;

    private Long workflowInstanceId;

    private Long workflowTaskInstanceId;

    private Map<String, Object> params;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date previousFireTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date nextFireTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date scheduledFireTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date fireTime;

    private ContainerPool containerPool;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final DefaultAttributeMap attributeMap = new DefaultAttributeMap();

    @Override
    public <T> Collection<Attribute<T>> attrs() {
        return attributeMap.attrs();
    }

    @Override
    public <T> Attribute<T> attr(AttributeKey<T> key) {
        return attributeMap.attr(key);
    }

    @Override
    public <T> boolean hasAttr(AttributeKey<T> key) {
        return attributeMap.hasAttr(key);
    }
}
