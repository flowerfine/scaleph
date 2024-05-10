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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.fake.source;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.fake.source.FakeProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class FakeSourcePlugin extends SeaTunnelConnectorPlugin {

    public FakeSourcePlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Fake Source Plugin, output random records.",
                FakeSourcePlugin.class.getName());

        List<PropertyDescriptor> props = new ArrayList<>();
        props.add(TABLES_CONFIGS);
        props.add(SCHEMA);
        props.add(ROW_NUM);
        props.add(ROWS);
        props.add(SPLIT_NUM);
        props.add(SPLIT_READ_INTERVAL);
        props.add(MAP_SIZE);
        props.add(ARRAY_SIZE);
        props.add(BYTES_SIZE);
        props.add(STRING_SIZE);

        props.add(STRING_FAKE_MODE);
        props.add(STRING_TEMPLATE);
        props.add(TINYINT_FAKE_MODE);
        props.add(TINYINT_MIN);
        props.add(TINYINT_MAX);
        props.add(TINYINT_TEMPLATE);
        props.add(SMALLINT_FAKE_MODE);
        props.add(SMALLINT_MIN);
        props.add(SMALLINT_MAX);
        props.add(SMALLINT_TEMPLATE);
        props.add(INT_FAKE_MODE);
        props.add(INT_MIN);
        props.add(INT_MAX);
        props.add(INT_TEMPLATE);
        props.add(BIGINT_FAKE_MODE);
        props.add(BIGINT_MIN);
        props.add(BIGINT_MAX);
        props.add(BIGINT_TEMPLATE);
        props.add(FLOAT_FAKE_MODE);
        props.add(FLOAT_MIN);
        props.add(FLOAT_MAX);
        props.add(FLOAT_TEMPLATE);
        props.add(DOUBLE_FAKE_MODE);
        props.add(DOUBLE_MIN);
        props.add(DOUBLE_MAX);
        props.add(DOUBLE_TEMPLATE);

        props.add(DATE_YEAR_TEMPLATE);
        props.add(DATE_MONTH_TEMPLATE);
        props.add(DATE_DAY_TEMPLATE);
        props.add(TIME_HOUR_TEMPLATE);
        props.add(TIME_MINUTE_TEMPLATE);
        props.add(TIME_SECOND_TEMPLATE);
        props.add(CommonProperties.PARALLELISM);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_FAKE;
    }

}
