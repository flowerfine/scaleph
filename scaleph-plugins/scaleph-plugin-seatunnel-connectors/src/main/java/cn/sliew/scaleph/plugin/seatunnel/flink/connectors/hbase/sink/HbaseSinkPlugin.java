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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.hbase.sink;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.hbase.HbaseProperties.TABLE;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.hbase.HbaseProperties.ZOOOKEEPER_QUORUM;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.hbase.sink.HbaseSinkProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class HbaseSinkPlugin extends SeaTunnelConnectorPlugin {

    public HbaseSinkPlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Output data to Hbase",
                HbaseSinkPlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(ZOOOKEEPER_QUORUM);
        props.add(TABLE);
        props.add(ROWKEY_COLUMN);
        props.add(FAMILY_NAME);
        props.add(ROWKEY_DELIMITER);
        props.add(VERSION_COLUMN);
        props.add(NULL_MODE);
        props.add(WAL_WRITE);
        props.add(WRITE_BUFFER_SIZE);
        props.add(ENCODING);
        props.add(HBASE_EXTRA_CONFIG);
        props.add(CommonProperties.PARALLELISM);
        props.add(CommonProperties.SOURCE_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_HBASE;
    }
}
