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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.iotdb.source;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.iotdb.IoTDBProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.iotdb.source.IoTDBSourceProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class IoTDBSourcePlugin extends SeaTunnelConnectorPlugin {

    public IoTDBSourcePlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Read external data through IoTDB.",
                IoTDBSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(NODE_URLS);
        props.add(USERNAME);
        props.add(PASSWORD);
        props.add(SQL);
        props.add(FIELDS);
        props.add(FETCH_SIZE);
        props.add(THRIFT_DEFAULT_BUFFER_SIZE);
        props.add(THRIFT_MAX_FRAME_SIZE);
        props.add(ENABLE_CACHE_LOADER);
        props.add(VERSION);
        props.add(NUM_PARTITIONS);
        props.add(LOWER_BOUND);
        props.add(UPPER_BOUND);
        props.add(CommonProperties.PARALLELISM);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_IOTDB;
    }
}
