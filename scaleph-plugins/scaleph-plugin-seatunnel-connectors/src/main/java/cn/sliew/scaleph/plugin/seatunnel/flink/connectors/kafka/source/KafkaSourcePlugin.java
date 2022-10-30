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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kafka.source;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kafka.source.KafkaSourceProperties.BOOTSTRAP_SERVERS;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kafka.source.KafkaSourceProperties.COMMIT_ON_CHECKPOINT;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kafka.source.KafkaSourceProperties.CONSUMER_GROUP;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kafka.source.KafkaSourceProperties.KAFKA_CONF;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kafka.source.KafkaSourceProperties.PATTERN;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kafka.source.KafkaSourceProperties.TOPIC;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.param.PropertyUtil;
import cn.sliew.scaleph.plugin.datasource.kafka.KafkaProperties;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.service.AutoService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author lizu
 * @since 2022/10/17
 */
@AutoService(SeaTunnelConnectorPlugin.class)
public class KafkaSourcePlugin extends SeaTunnelConnectorPlugin {

    public KafkaSourcePlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
            "Kafka Source Plugin",
            KafkaSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(TOPIC);
        props.add(BOOTSTRAP_SERVERS);
        props.add(PATTERN);
        props.add(CONSUMER_GROUP);
        props.add(COMMIT_ON_CHECKPOINT);
        props.add(KAFKA_CONF);

        props.add(CommonProperties.FIELD_NAME);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public ObjectNode createConf() {
        ObjectNode objectNode = JacksonUtil.createObjectNode();
        for (PropertyDescriptor descriptor : getSupportedProperties()) {
            if (properties.contains(descriptor)) {
                if (BOOTSTRAP_SERVERS.getName().equals(descriptor.getName())) {
                    String server = properties.getValue(KafkaProperties.BOOTSTRAP_SERVERS);
                    objectNode.put(BOOTSTRAP_SERVERS.getName(), server);
                } else if (KAFKA_CONF.getName().equals(descriptor.getName())) {
                    Map<String, Object> map = PropertyUtil
                        .formatPropFromStr(properties.getValue(descriptor), "\n", "=");
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        objectNode.put("kafka." + entry.getKey(), String.valueOf(entry.getValue()));
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
        return SeaTunnelPluginMapping.SOURCE_KAFKA;
    }
}
