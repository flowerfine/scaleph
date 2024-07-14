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

package cn.sliew.scaleph.plugin.flink.cdc.connectors.doris.sink;

import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCPluginMapping;
import cn.sliew.scaleph.ds.modal.AbstractDataSource;
import cn.sliew.scaleph.ds.modal.olap.DorisDataSource;
import cn.sliew.scaleph.plugin.flink.cdc.FlinkCDCPipilineConnectorPlugin;
import cn.sliew.scaleph.plugin.flink.cdc.connectors.CommonProperties;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.resource.ResourceProperties;
import cn.sliew.scaleph.plugin.framework.resource.ResourceProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.flink.cdc.connectors.doris.sink.DorisSinkProperties.*;
import static cn.sliew.scaleph.plugin.flink.cdc.connectors.mysql.source.MySQLSourceProperties.PASSWORD;
import static cn.sliew.scaleph.plugin.flink.cdc.connectors.mysql.source.MySQLSourceProperties.USERNAME;

@AutoService(FlinkCDCPipilineConnectorPlugin.class)
public class DorisSinkPlugin extends FlinkCDCPipilineConnectorPlugin {

    public DorisSinkPlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "doris",
                DorisSinkPlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(CommonProperties.NAME);
        props.add(CommonProperties.TYPE);
        props.add(AUTO_REDIRECT);
        props.add(SINK_ENABLE_BATCH_MODE);
        props.add(SINK_FLUSH_QUEUE_SIZE);
        props.add(SINK_BUFFER_FLUSH_MAX_ROWS);
        props.add(SINK_BUFFER_FLUSH_MAX_BYTES);
        props.add(SINK_BUFFER_FLUSH_INTERVAL);
        props.add(SINK_PROPERTIES);
        props.add(TABLE_CREATE_PROPERTIES);
        this.supportedProperties = Collections.unmodifiableList(props);
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
        conf.putPOJO(USERNAME.getName(), dataSource.getUsername());
        conf.putPOJO(PASSWORD.getName(), dataSource.getPassword());
        return conf;
    }

    @Override
    protected FlinkCDCPluginMapping getPluginMapping() {
        return FlinkCDCPluginMapping.SINK_DORIS;
    }
}
