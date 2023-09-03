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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.rocketmq.sink;

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
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.rocketmq.sink.RocketMQSinkProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class RocketMQSinkPlugin extends SeaTunnelConnectorPlugin {

    public RocketMQSinkPlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Write Rows to a Apache RocketMQ topic.",
                RocketMQSinkPlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(NAME_SRV_ADDR);
        props.add(ACL_ENABLED);
        props.add(ACCESS_KEY);
        props.add(SECRET_KEY);
        props.add(TOPIC);
        props.add(SEMANTIC);
        props.add(PRODUCER_GROUP);
        props.add(PARTITION_KEY_FIELDS);
        props.add(EXACTLY_ONCE);
        props.add(FORMAT);
        props.add(FIELD_DELIMITER);
        props.add(SEND_SYNC);
        props.add(MAX_MESSAGE_SIZE);
        props.add(SEND_MESSAGE_TIMEOUT_MILLIS);
        props.add(CommonProperties.PARALLELISM);
        props.add(CommonProperties.SOURCE_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_ROCKETMQ;
    }
}
