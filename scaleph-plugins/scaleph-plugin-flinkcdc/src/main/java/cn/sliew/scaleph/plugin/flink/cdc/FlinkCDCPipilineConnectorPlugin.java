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

package cn.sliew.scaleph.plugin.flink.cdc;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCPluginMapping;
import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCPluginName;
import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCPluginType;
import cn.sliew.scaleph.plugin.flink.cdc.util.FlinkCDCPluginUtil;
import cn.sliew.scaleph.plugin.framework.core.AbstractPlugin;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.resource.ResourceProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collections;
import java.util.List;

public abstract class FlinkCDCPipilineConnectorPlugin extends AbstractPlugin {

    public final String getIdentity() {
        return FlinkCDCPluginUtil.getIdentity(getPluginType(), getPluginName());
    }

    public final FlinkCDCPluginType getPluginType() {
        return getPluginMapping().getPluginType();
    }

    public final FlinkCDCPluginName getPluginName() {
        return getPluginMapping().getPluginName();
    }

    public ObjectNode createConf() {
        ObjectNode objectNode = JacksonUtil.createObjectNode();
        for (PropertyDescriptor descriptor : getSupportedProperties()) {
            if (properties.contains(descriptor)) {
                objectNode.putPOJO(descriptor.getName(), properties.get(descriptor));
            }
        }
        return objectNode;
    }

    public List<ResourceProperty> getRequiredResources() {
        return Collections.emptyList();
    }

    protected abstract FlinkCDCPluginMapping getPluginMapping();
}
