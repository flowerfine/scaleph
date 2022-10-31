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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.neo4j.sink;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.neo4j.sink.Neo4jSinkProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class Neo4jSinkPlugin extends SeaTunnelConnectorPlugin {

    public Neo4jSinkPlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Write data to Neo4j.",
                Neo4jSinkPlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(URI);
        props.add(USERNAME);
        props.add(PASSWORD);
        props.add(BEARER_TOKEN);
        props.add(KERBEROS_TICKET);
        props.add(DATABASE);
        props.add(QUERY);
        props.add(QUERY_PARAM_POSITION);
        props.add(MAX_TRANSACTION_RETRY_SIZE);
        props.add(MAX_CONNECTION_TIMEOUT);
        props.add(CommonProperties.SOURCE_TABLE_NAME);
        this.supportedProperties = props;
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_NEO4J;
    }
}
