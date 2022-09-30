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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.fake.source;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.fake.source.FakeProperties.ROW_NUM;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.fake.source.FakeProperties.SCHEMA;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelPluginMapping;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.service.AutoService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


@AutoService(SeaTunnelConnectorPlugin.class)
public class FakeSourcePlugin extends SeaTunnelConnectorPlugin {

    public FakeSourcePlugin() {
        this.pluginInfo = new PluginInfo(getPluginName().getLabel(),
            "Fake Source Plugin , input records from random data.",
            FakeSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();

        props.add(SCHEMA);
        props.add(ROW_NUM);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public ObjectNode createConf() {
        ObjectNode objectNode = JacksonUtil.createObjectNode();
        for (PropertyDescriptor descriptor : getSupportedProperties()) {
            if (properties.contains(descriptor)) {
                if (SCHEMA.getName().equals(descriptor.getName())) {
                    ObjectNode fieldsNode = JacksonUtil.createObjectNode();
                    ObjectNode node = JacksonUtil.createObjectNode();
                    ArrayNode jsonNode = (ArrayNode) JacksonUtil
                        .toJsonNode(properties.getValue(descriptor));
                    Iterator<JsonNode> schemas = jsonNode.elements();
                    while (schemas.hasNext()) {
                        JsonNode filedSchema = schemas.next();
                        String field = filedSchema.get("field").textValue();
                        String type = filedSchema.get("type").textValue().toUpperCase();
                        node.put(field, type);
                    }
                    fieldsNode.set("fields", node);
                    objectNode.set(descriptor.getName(), fieldsNode);
                } else if (descriptor.getName().contains("_")) {
                    objectNode.put(descriptor.getName().replace("_", "."),
                        properties.getValue(descriptor));
                } else {
                    objectNode.put(descriptor.getName(), properties.getValue(descriptor));
                }
            }
        }
        return objectNode;
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_FAKE;
    }


}
