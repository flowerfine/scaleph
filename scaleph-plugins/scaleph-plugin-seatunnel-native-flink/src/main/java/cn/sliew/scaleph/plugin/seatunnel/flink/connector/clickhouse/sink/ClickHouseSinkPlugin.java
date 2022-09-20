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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.clickhouse.sink;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.enums.JobStepTypeEnum;
import cn.sliew.scaleph.common.param.PropertyUtil;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeatunnelNativeFlinkPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.common.CommonProperties;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.sliew.scaleph.common.enums.SeatunnelNativeFlinkPluginEnum.CLICKHOUSE_SINK;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connector.clickhouse.sink.ClickHouseProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connector.clickhouse.sink.ClickHouseSinkProperties.*;

@AutoService(SeatunnelNativeFlinkPlugin.class)
public class ClickHouseSinkPlugin extends SeatunnelNativeFlinkPlugin {

    public ClickHouseSinkPlugin() {
        this.pluginInfo = new PluginInfo(CLICKHOUSE_SINK.getValue(), "clickhouse sink connector",
                ClickHouseSinkPlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(HOST);
        props.add(USERNAME);
        props.add(PASSWORD);
        props.add(DATABASE);
        props.add(TABLE);
        props.add(FIELDS);
        props.add(SPLIT_MODE);
        props.add(SHARDING_KEY);
        props.add(CLICKHOUSE_XXX);
        props.add(BULK_SIZE);
        props.add(RETRY);
        props.add(RETRY_CODES);

        props.add(CommonProperties.SOURCE_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public JobStepTypeEnum getStepType() {
        return JobStepTypeEnum.SINK;
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
                                .put("clickhouse." + entry.getKey(), String.valueOf(entry.getValue()));
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
}
