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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.hive.sink;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.hive.HiveProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class HiveSinkPlugin extends SeaTunnelConnectorPlugin {

    public HiveSinkPlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Hive Source Plugin.",
                HiveSinkPlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(TABLE_NAME);
        props.add(METASTORE_URI);
        props.add(PARTITION_BY);
        props.add(SINK_COLUMNS);
        props.add(IS_ENABLE_TRANSACTION);
        props.add(SAVE_MODE);

        props.add(CommonProperties.FIELD_NAME);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public ObjectNode createConf() {
        ObjectNode objectNode = JacksonUtil.createObjectNode();
        for (PropertyDescriptor descriptor : getSupportedProperties()) {
            if (properties.contains(descriptor)) {
                if (PARTITION_BY.getName().equals(descriptor.getName()) ||
                        SINK_COLUMNS.getName().equals(descriptor.getName())) {
                    String[] splitFields = properties.getValue(descriptor).split(",");
                    ArrayNode jsonNodes = objectNode.putArray(descriptor.getName());
                    for (String field : splitFields) {
                        jsonNodes.add(field);
                    }
                } else {
                    objectNode.put(descriptor.getName(), properties.getValue(descriptor));
                }
            }
        }
        return objectNode;
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_HIVE;
    }

}
