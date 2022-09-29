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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.iceberg.source;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.iceberg.source.IcebergSourceProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class IcebergSourcePlugin extends SeaTunnelConnectorPlugin {

    public IcebergSourcePlugin() {
        this.pluginInfo = new PluginInfo(getPluginName().getLabel(),
                "Apache Iceberg is an open table format for huge analytic datasets",
                IcebergSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(CATALOG_TYPE);
        props.add(CATALOG_NAME);
        props.add(NAMESPACE);
        props.add(TABLE);
        props.add(URI);
        props.add(WAREHOUSE);
        props.add(CASE_SENSITIVE);
        props.add(FIELDS);
        props.add(USE_SNAPSHOT_ID);
        props.add(START_SNAPSHOT_ID);
        props.add(END_SNAPSHOT_ID);
        props.add(START_SNAPSHOT_TIMESTAMP);
        props.add(USE_SNAPSHOT_TIMESTAMP);
        props.add(STREAM_SCAN_STRATEGY);
        props.add(CommonProperties.FIELD_NAME);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_ICEBERG;
    }
}
