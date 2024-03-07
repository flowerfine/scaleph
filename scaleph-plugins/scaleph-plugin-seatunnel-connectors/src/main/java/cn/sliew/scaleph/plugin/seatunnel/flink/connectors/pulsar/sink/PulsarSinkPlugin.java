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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.pulsar.sink;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.ds.modal.AbstractDataSource;
import cn.sliew.scaleph.ds.modal.mq.PulsarDataSource;
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

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.pulsar.PulsarProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.pulsar.sink.PulsarSinkProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class PulsarSinkPlugin extends SeaTunnelConnectorPlugin {

    public PulsarSinkPlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Sink connector for Apache Pulsar.",
                PulsarSinkPlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(TOPIC);
        props.add(FORMAT);
        props.add(FIELD_DELIMITER);
        props.add(PULSAR_CONFIG);
        props.add(SEMANTICS);
        props.add(TRANSACTION_TIMEOUT);
        props.add(MESSAGE_ROUTING_MODE);
        props.add(PARTITION_KEY_FIELDS);
        props.add(CommonProperties.PARALLELISM);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public List<ResourceProperty> getRequiredResources() {
        return Collections.singletonList(ResourceProperties.DATASOURCE_RESOURCE);
    }

    @Override
    public ObjectNode createConf() {
        ObjectNode conf = super.createConf();
        JsonNode jsonNode = properties.get(ResourceProperties.DATASOURCE);
        PulsarDataSource dataSource = (PulsarDataSource) AbstractDataSource.fromDsInfo((ObjectNode) jsonNode);
        conf.putPOJO(ADMIN_SERVICE_URL.getName(), dataSource.getWebServiceUrl());
        conf.putPOJO(CLIENT_SERVICE_URL.getName(), dataSource.getClientServiceUrl());
        if (StringUtils.hasText(dataSource.getAuthPlugin())) {
            conf.putPOJO(AUTH_PLUGIN_CLASS.getName(), dataSource.getAuthPlugin());
        }
        if (StringUtils.hasText(dataSource.getAuthParams())) {
            conf.putPOJO(AUTH_PARAMS.getName(), dataSource.getAuthParams());
        }
        return conf;
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_PULSAR;
    }
}
