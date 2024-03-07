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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.starrocks.sink;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.ds.modal.AbstractDataSource;
import cn.sliew.scaleph.ds.modal.olap.StarRocksDataSource;
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

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.starrocks.StarRocksProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.starrocks.sink.StarRocksSinkProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class StarRocksSinkPlugin extends SeaTunnelConnectorPlugin {

    public StarRocksSinkPlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Used to send data to StarRocks. Both support streaming and batch mode. The internal implementation of StarRocks sink connector is cached and imported by stream load in batches.",
                StarRocksSinkPlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(BASE_URL);
        props.add(DATABASE);
        props.add(TABLE);
        props.add(LABEL_PREFIX);
        props.add(BATCH_MAX_ROWS);
        props.add(BATCH_MAX_BYTES);
        props.add(BATCH_INTERVAL_MS);
        props.add(MAX_RETRIES);
        props.add(RETRY_BACKOFF_MULTIPLIER_MS);
        props.add(MAX_RETRY_BACKOFF_MS);
        props.add(ENABLE_UPSERT_DELETE);
        props.add(SAVE_MODE_CREATE_TEMPLATE);
        props.add(STARROCKS_CONFIG);
        props.add(HTTP_SOCKET_TIMEOUT_MS);
        props.add(SCHEMA_SAVE_MODE);
        props.add(DATA_SAVE_MODE);
        props.add(CUSTOM_SQL);
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
        StarRocksDataSource dataSource = (StarRocksDataSource) AbstractDataSource.fromDsInfo((ObjectNode) jsonNode);
        conf.putPOJO(NODE_URLS.getName(), StringUtils.commaDelimitedListToStringArray(dataSource.getNodeUrls()));
        if (StringUtils.hasText(dataSource.getUsername())) {
            conf.putPOJO(USERNAME.getName(), dataSource.getUsername());
        }
        if (StringUtils.hasText(dataSource.getPassword())) {
            conf.putPOJO(PASSWORD.getName(), dataSource.getPassword());
        }
        if (StringUtils.hasText(dataSource.getBaseUrl())) {
            conf.putPOJO(BASE_URL.getName(), dataSource.getBaseUrl());
        }
        return conf;
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_STARROCKS;
    }
}
