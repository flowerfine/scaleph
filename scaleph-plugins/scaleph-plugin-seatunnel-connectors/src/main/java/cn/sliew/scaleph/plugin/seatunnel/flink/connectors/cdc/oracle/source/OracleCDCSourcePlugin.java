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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.cdc.oracle.source;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.cdc.CDCSourceProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class OracleCDCSourcePlugin extends SeaTunnelConnectorPlugin {

    public OracleCDCSourcePlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "The Oracle CDC connector allows for reading snapshot data and incremental data from Oracle database",
                OracleCDCSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(BASE_URL);
        props.add(USERNAME);
        props.add(PASSWORD);
        props.add(DATABASE);
        props.add(OracleCDCSourceProperties.SCHEMA);
        props.add(OracleCDCSourceProperties.USE_SELECT_COUNT);
        props.add(OracleCDCSourceProperties.SKIP_ANALYZE);
        props.add(TABLE);
        props.add(TABLE_CONFIG);
        props.add(STARTUP_MODE);
        props.add(STARTUP_TIMESTAMP);
        props.add(STARTUP_SPECIFIC_OFFSET_FILE);
        props.add(STARTUP_SPECIFIC_OFFSET_POS);
        props.add(STOP_MODE);
        props.add(STOP_TIMESTAMP);
        props.add(STOP_SPECIFIC_OFFSET_FILE);
        props.add(STOP_SPECIFIC_OFFSET_POS);
        props.add(INCREMENTAL_PARALLELISM);
        props.add(SNAPSHOT_SPLIT_SIZE);
        props.add(SNAPSHOT_FETCH_SIZE);
        props.add(SERVER_TIME_ZONE);
        props.add(CONNECT_TIMEOUT);
        props.add(CONNECT_MAX_RETRIES);
        props.add(CONNECT_POOL_SIZE);
        props.add(CHUNK_KEY_EVEN_DISTRIBUTION_FACTOR_UPPER_BOUND);
        props.add(CHUNK_KEY_EVEN_DISTRIBUTION_FACTOR_LOWER_BOUND);
        props.add(SAMPLE_SHARDING_THRESHOLD);
        props.add(INVERSE_SHARDING_RATE);
        props.add(EXACTLY_ONCE);
        props.add(DEBEZIUM);
        props.add(FORMAT);
        props.add(CommonProperties.PARALLELISM);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_ORACLE_CDC;
    }
}
