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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.sink;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.sink.ClickHouseSinkProperties.BULK_SIZE;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.sink.ClickHouseSinkProperties.CLICKHOUSE_CONF;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.sink.ClickHouseSinkProperties.DATABASE;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.sink.ClickHouseSinkProperties.FIELDS;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.sink.ClickHouseSinkProperties.HOST;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.sink.ClickHouseSinkProperties.PASSWORD;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.sink.ClickHouseSinkProperties.SHARDING_KEY;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.sink.ClickHouseSinkProperties.SPLIT_MODE;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.sink.ClickHouseSinkProperties.TABLE;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.sink.ClickHouseSinkProperties.USERNAME;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.param.PropertyUtil;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.service.AutoService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@AutoService(SeaTunnelConnectorPlugin.class)
public class ClickHouseSinkPlugin extends SeaTunnelConnectorPlugin {

    public ClickHouseSinkPlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
            "sink clickhouse",
            ClickHouseSinkPlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(HOST);
        props.add(DATABASE);
        props.add(USERNAME);
        props.add(PASSWORD);

        props.add(TABLE);
        props.add(FIELDS);
        props.add(CLICKHOUSE_CONF);
        props.add(BULK_SIZE);
        props.add(SPLIT_MODE);
        props.add(SHARDING_KEY);

        props.add(CommonProperties.FIELD_NAME);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public ObjectNode createConf() {
        ObjectNode objectNode = JacksonUtil.createObjectNode();
        for (PropertyDescriptor descriptor : getSupportedProperties()) {
            if (properties.contains(descriptor)) {
                if (CLICKHOUSE_CONF.getName().equals(descriptor.getName())) {
                    Map<String, Object> map = PropertyUtil
                        .formatPropFromStr(properties.getValue(descriptor), "\n", "=");
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        objectNode
                            .put(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                } else if (FIELDS.getName().equals(descriptor.getName())) {
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
        return SeaTunnelPluginMapping.SINK_CLICKHOUSE;
    }

}
