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
package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.cassandra.source;

import cn.sliew.carp.module.datasource.modal.DataSourceInfo;
import cn.sliew.carp.module.datasource.modal.nosql.CassandraDataSourceProperties;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
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

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.cassandra.CassandraProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.cassandra.source.CassandraSourceProperties.CQL;

@AutoService(SeaTunnelConnectorPlugin.class)
public class CassandraSourcePlugin extends SeaTunnelConnectorPlugin {

    public CassandraSourcePlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Cassandra source connector",
                CassandraSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(CQL);
        props.add(CONSISTENCY_LEVEL);
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

        DataSourceInfo dataSourceInfo = JacksonUtil.toObject(jsonNode, DataSourceInfo.class);
        CassandraDataSourceProperties props = (CassandraDataSourceProperties) dataSourceInfo.getProps();
        conf.putPOJO(HOST.getName(), props.getHost());
        conf.putPOJO(KEYSPACE.getName(), props.getKeyspace());
        if (StringUtils.hasText(props.getUsername())) {
            conf.putPOJO(USERNAME.getName(), props.getUsername());
        }
        if (StringUtils.hasText(props.getPassword())) {
            conf.putPOJO(PASSWORD.getName(), props.getPassword());
        }
        if (StringUtils.hasText(props.getDatacenter())) {
            conf.putPOJO(DATACENTER.getName(), props.getDatacenter());
        }
        return conf;
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_CASSANDRA;
    }

}
