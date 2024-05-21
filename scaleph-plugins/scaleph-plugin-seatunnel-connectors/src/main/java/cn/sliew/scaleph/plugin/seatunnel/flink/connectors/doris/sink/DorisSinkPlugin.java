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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.doris.sink;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.ds.modal.AbstractDataSource;
import cn.sliew.scaleph.ds.modal.olap.DorisDataSource;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.connectors.SaveModeProperties;
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

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.doris.DorisProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.doris.sink.DorisSinkProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class DorisSinkPlugin extends SeaTunnelConnectorPlugin {

    public DorisSinkPlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Used to send data to Doris. Both support streaming and batch mode. The internal implementation of Doris sink connector is cached and imported by stream load in batches.",
                DorisSinkPlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(DATABASE);
        props.add(TABLE);
        props.add(SINK_LABEL_PREFIX);
        props.add(SINK_ENABLE_2PC);
        props.add(SINK_ENABLE_DELETE);
        props.add(SINK_CHECK_INTERVAL);
        props.add(SINK_MAX_RETRIES);
        props.add(SINK_BUFFER_SIZE);
        props.add(SINK_BUFFER_COUNT);
        props.add(DORIS_BATCH_SIZE);
        props.add(NEEDS_UNSUPPORTED_TYPE_CASTING);
        props.add(SaveModeProperties.SCHEMA_SAVE_MODE);
        props.add(SaveModeProperties.DATA_SAVE_MODE);
        props.add(SAVE_MODE_CREATE_TEMPLATE);
        props.add(CUSTOM_SQL);
        props.add(DORIS_CONFIG);
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
        DorisDataSource dataSource = (DorisDataSource) AbstractDataSource.fromDsInfo((ObjectNode) jsonNode);
        conf.putPOJO(FENODES.getName(), dataSource.getNodeUrls());
        if (StringUtils.hasText(dataSource.getUsername())) {
            conf.putPOJO(USERNAME.getName(), dataSource.getUsername());
        }
        if (StringUtils.hasText(dataSource.getPassword())) {
            conf.putPOJO(PASSWORD.getName(), dataSource.getPassword());
        } else {
            conf.putPOJO(PASSWORD.getName(), "");
        }
        if (dataSource.getQueryPort() != null) {
            conf.putPOJO(QUERY_PORT.getName(), dataSource.getQueryPort());
        }
        return conf;
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_DORIS;
    }
}
