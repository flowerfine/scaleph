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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.neo4j.source;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.ds.modal.AbstractDataSource;
import cn.sliew.scaleph.ds.modal.Neo4jDataSource;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import cn.sliew.scaleph.plugin.seatunnel.flink.resource.ResourceProperties;
import cn.sliew.scaleph.plugin.seatunnel.flink.resource.ResourceProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.service.AutoService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.neo4j.Neo4jProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.neo4j.source.Neo4jSourceProperties.SCHEMA;

@AutoService(SeaTunnelConnectorPlugin.class)
public class Neo4jSourcePlugin extends SeaTunnelConnectorPlugin {

    public Neo4jSourcePlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Neo4j source connector",
                Neo4jSourcePlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(DATABASE);
        props.add(QUERY);
        props.add(SCHEMA);
        props.add(MAX_TRANSACTION_RETRY_SIZE);
        props.add(MAX_CONNECTION_TIMEOUT);
        props.add(CommonProperties.PARALLELISM);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        this.supportedProperties = props;
    }

    @Override
    public List<ResourceProperty> getRequiredResources() {
        return Collections.singletonList(ResourceProperties.DATASOURCE_RESOURCE);
    }

    @Override
    public ObjectNode createConf() {
        ObjectNode conf = super.createConf();
        JsonNode jsonNode = properties.get(ResourceProperties.DATASOURCE);
        Neo4jDataSource dataSource = (Neo4jDataSource) AbstractDataSource.fromDsInfo((ObjectNode) jsonNode);
        conf.putPOJO(URI.getName(), dataSource.getUri());
        if (StringUtils.hasText(dataSource.getUsername())) {
            conf.putPOJO(USERNAME.getName(), dataSource.getUsername());
            conf.putPOJO(PASSWORD.getName(), dataSource.getPassword());
        }
        if (StringUtils.hasText(dataSource.getBearerToken())) {
            conf.putPOJO(BEARER_TOKEN.getName(), dataSource.getBearerToken());
        }
        if (StringUtils.hasText(dataSource.getKerberosTicket())) {
            conf.putPOJO(KERBEROS_TICKET.getName(), dataSource.getKerberosTicket());
        }
        return conf;
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_NEO4J;
    }
}
