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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.pulsar.source;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.pulsar.source.PulsarSourceProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class PulsarSourcePlugin extends SeaTunnelConnectorPlugin {

    public PulsarSourcePlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Source connector for Apache Pulsar.",
                PulsarSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(CLIENT_SERVICE_URL);
        props.add(ADMIN_SERVICE_URL);
        props.add(AUTH_PLUGIN_CLASS);
        props.add(AUTH_PARAMS);
        props.add(SUBSCRIPTION_NAME);
        props.add(TOPIC);
        props.add(TOPIC_PATTERN);
        props.add(TOPIC_DISCOVERY_INTERVAL);
        props.add(POLL_TIMEOUT);
        props.add(POLL_INTERVAL);
        props.add(POLL_BATCH_SIZE);
        props.add(CURSOR_STARTUP_MODE);
        props.add(CURSOR_STARTUP_TIMESTAMP);
        props.add(CURSOR_RESET_MODE);
        props.add(CURSOR_STOP_MODE);
        props.add(CURSOR_STOP_TIMESTAMP);
        props.add(SCHEMA);
        props.add(CommonProperties.PARALLELISM);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_PULSAR;
    }
}
