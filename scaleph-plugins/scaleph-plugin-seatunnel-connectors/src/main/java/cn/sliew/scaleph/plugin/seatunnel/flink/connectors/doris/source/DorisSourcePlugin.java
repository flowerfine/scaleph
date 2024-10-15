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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.doris.source;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.ds.modal.AbstractDataSource;
import cn.sliew.scaleph.ds.modal.olap.DorisDataSource;
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

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.doris.DorisProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.doris.DorisProperties.DORIS_BATCH_SIZE;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.doris.source.DorisSourceProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class DorisSourcePlugin extends SeaTunnelConnectorPlugin {

    public DorisSourcePlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Doris source connector",
                DorisSourcePlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(DATABASE);
        props.add(TABLE);
        props.add(DORIS_READ_FIELD);
        props.add(DORIS_FILTER_QUERY);
        props.add(DORIS_BATCH_SIZE);
        props.add(DORIS_REQUEST_CONNECT_TIMEOUT_MS);
        props.add(DORIS_REQUEST_QUERY_TIMEOUT_S);
        props.add(DORIS_REQUEST_READ_TIMEOUT_MS);
        props.add(DORIS_REQUEST_RETRIES);
        props.add(DORIS_EXEC_MEM_LIMIT);
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
        }
        if (dataSource.getQueryPort() != null) {
            conf.putPOJO(QUERY_PORT.getName(), dataSource.getQueryPort());
        }
        return conf;
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_DORIS;
    }
}
