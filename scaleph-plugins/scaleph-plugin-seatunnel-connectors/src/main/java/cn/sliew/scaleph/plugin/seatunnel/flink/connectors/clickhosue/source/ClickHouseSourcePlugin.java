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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.source;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.ds.modal.AbstractDataSource;
import cn.sliew.scaleph.ds.modal.olap.ClickHouseDataSource;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import cn.sliew.scaleph.plugin.seatunnel.flink.resource.ResourceProperties;
import cn.sliew.scaleph.plugin.seatunnel.flink.resource.ResourceProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.ClickHouseProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.source.ClickHouseSourceProperties.SERVER_TIME_ZONE;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.source.ClickHouseSourceProperties.SQL;

@AutoService(SeaTunnelConnectorPlugin.class)
public class ClickHouseSourcePlugin extends SeaTunnelConnectorPlugin {

    public ClickHouseSourcePlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "ClickHouse source connector",
                ClickHouseSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(SQL);
        props.add(SERVER_TIME_ZONE);
        props.add(CLICKHOUSE_CONFIG);
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
        ClickHouseDataSource dataSource = (ClickHouseDataSource) AbstractDataSource.fromDsInfo((ObjectNode) jsonNode);
        conf.putPOJO(HOST.getName(), dataSource.getHost());
        conf.putPOJO(DATABASE.getName(), dataSource.getDatabase());
        conf.putPOJO(USERNAME.getName(), dataSource.getUsername());
        conf.putPOJO(PASSWORD.getName(), dataSource.getPassword());
        return conf;
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_CLICKHOUSE;
    }

}
