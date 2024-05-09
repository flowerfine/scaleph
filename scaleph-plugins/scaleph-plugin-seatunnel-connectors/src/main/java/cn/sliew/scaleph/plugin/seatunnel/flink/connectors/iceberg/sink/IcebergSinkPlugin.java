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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.iceberg.sink;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.SaveModeProperties.DATA_SAVE_MODE;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.SaveModeProperties.SCHEMA_SAVE_MODE;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.iceberg.IcebergProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.iceberg.sink.IcebergSinkProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class IcebergSinkPlugin extends SeaTunnelConnectorPlugin {

    public IcebergSinkPlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Apache Iceberg is an open table format for huge analytic datasets",
                IcebergSinkPlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(CATALOG_NAME);
        props.add(NAMESPACE);
        props.add(TABLE);
        props.add(ICEBERG_CATALOG_CONFIG);
        props.add(ICEBERG_HADOOP_CONF_PATH);
        props.add(HADOOP_CONFIG);
        props.add(CASE_SENSITIVE);
        props.add(ICEBERG_TABEL_WRITE_PROPS);
        props.add(ICEBERG_TABEL_AUTO_CREATE_PROPS);
        props.add(ICEBERG_TABEL_PRIMARY_KEYS);
        props.add(ICEBERG_TABEL_PARTITION_KEYS);
        props.add(ICEBERG_TABEL_SCHEMA_EVOLUTION_ENABLED);
        props.add(ICEBERG_TABEL_UPSERT_MODE_ENABLED);
        props.add(ICEBERG_TABEL_COMMIT_BRANCH);
        props.add(SCHEMA_SAVE_MODE);
        props.add(DATA_SAVE_MODE);
        props.add(CommonProperties.PARALLELISM);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_ICEBERG;
    }
}
