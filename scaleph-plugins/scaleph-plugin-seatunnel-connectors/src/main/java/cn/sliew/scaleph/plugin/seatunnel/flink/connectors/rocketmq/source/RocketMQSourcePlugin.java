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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.rocketmq.source;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.rocketmq.RocketMQProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.rocketmq.source.RocketMQSourceProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class RocketMQSourcePlugin extends SeaTunnelConnectorPlugin {

    public RocketMQSourcePlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Source connector for Apache RocketMQ.",
                RocketMQSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(NAME_SRV_ADDR);
        props.add(ACL_ENABLED);
        props.add(ACCESS_KEY);
        props.add(SECRET_KEY);
        props.add(TOPICS);
        props.add(KEY_PARTITION_DISCOVERY_INTERVAL_MILLIS);
        props.add(CONSUMER_GROUP);
        props.add(COMMIT_ON_CHECKPOINT);
        props.add(FORMAT);
        props.add(FIELD_DELIMITER);
        props.add(SCHEMA);
        props.add(START_MODE);
        props.add(START_MODE_TIMESTAMP);
        props.add(START_MODE_OFFSETS);
        props.add(BATCH_SIZE);
        props.add(POLL_TIMEOUT_MILLIS);
        props.add(CommonProperties.PARALLELISM);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_ROCKETMQ;
    }
}
