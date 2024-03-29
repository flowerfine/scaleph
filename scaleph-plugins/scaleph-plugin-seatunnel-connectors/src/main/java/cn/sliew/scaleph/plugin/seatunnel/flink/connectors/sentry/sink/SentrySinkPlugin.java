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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.sentry.sink;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.sentry.sink.SentrySinkProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class SentrySinkPlugin extends SeaTunnelConnectorPlugin {

    public SentrySinkPlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Write message to Sentry.",
                SentrySinkPlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(DSN);
        props.add(ENV);
        props.add(RELEASE);
        props.add(CACHE_DIR_PATH);
        props.add(ENABLE_EXTERNAL_CONFIGURATION);
        props.add(MAX_CACHE_ITEMS);
        props.add(FLUSH_TIMEOUT_MILLIS);
        props.add(MAX_QUEUE_SIZE);
        props.add(CommonProperties.PARALLELISM);
        props.add(CommonProperties.SOURCE_TABLE_NAME);
        this.supportedProperties = props;
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_SENTRY;
    }
}
