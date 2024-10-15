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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.elasticsearch.source;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.ds.modal.AbstractDataSource;
import cn.sliew.scaleph.ds.modal.nosql.ElasticsearchDataSource;
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

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.elasticsearch.ElasticsearchProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.elasticsearch.source.ElasticsearchSourceProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class ElasticsearchSourcePlugin extends SeaTunnelConnectorPlugin {

    public ElasticsearchSourcePlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Used to read data from Elasticsearch. support version >= 2.x and < 8.x.",
                ElasticsearchSourcePlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(INDEX_LIST);
        props.add(INDEX);
        props.add(SOURCE);
        props.add(ARRAY_COLUMN);
        props.add(QUERY);
        props.add(SCROLL_TIME);
        props.add(SCROLL_SIZE);
        props.add(SCHEMA);
        props.add(TLS_VERIFY_CERTIFICATE);
        props.add(TLS_VERIFY_HOSTNAMES);
        props.add(TLS_KEYSTORE_PATH);
        props.add(TLS_KEYSTORE_PASSWORD);
        props.add(TLS_TRUSTSTORE_PATH);
        props.add(TLS_TRUSTSTORE_PASSWORD);
        props.add(CommonProperties.PARALLELISM);
        props.add(CommonProperties.SOURCE_TABLE_NAME);
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
        ElasticsearchDataSource dataSource = (ElasticsearchDataSource) AbstractDataSource.fromDsInfo((ObjectNode) jsonNode);
        conf.putPOJO(HOSTS.getName(), StringUtils.commaDelimitedListToStringArray(dataSource.getHosts()));
        if (StringUtils.hasText(dataSource.getUsername())) {
            conf.putPOJO(USERNAME.getName(), dataSource.getUsername());
            conf.putPOJO(PASSWORD.getName(), dataSource.getPassword());
        }
        return conf;
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_ELASTICSEARCH;
    }
}
