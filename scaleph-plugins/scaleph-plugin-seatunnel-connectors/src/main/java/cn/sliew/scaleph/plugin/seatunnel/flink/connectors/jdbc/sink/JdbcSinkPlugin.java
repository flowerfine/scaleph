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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.jdbc.sink;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.jdbc.JdbcProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.jdbc.sink.JdbcSinkProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class JdbcSinkPlugin extends SeaTunnelConnectorPlugin {

    public JdbcSinkPlugin() {
        this.pluginInfo = new PluginInfo(getPluginName().getLabel(),
                "Jdbc Sink Plugin, output records from jdbc connection.",
                JdbcSinkPlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(URL);
        props.add(DRIVER);
        props.add(USER);
        props.add(PASSWORD);
        props.add(QUERY);
        props.add(CONNECTION_CHECK_TIMEOUT_SEC);
        props.add(MAX_RETRIES);
        props.add(BATCH_SIZE);
        props.add(BATCH_INTERVAL_MS);
        props.add(IS_EXACTLY_ONCE);
        props.add(XA_DATA_SOURCE_CLASS_NAME);
        props.add(MAX_COMMIT_ATTEMPTS);
        props.add(TRANSACTION_TIMEOUT_SEC);
        props.add(CommonProperties.SOURCE_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_JDBC;
    }

}
