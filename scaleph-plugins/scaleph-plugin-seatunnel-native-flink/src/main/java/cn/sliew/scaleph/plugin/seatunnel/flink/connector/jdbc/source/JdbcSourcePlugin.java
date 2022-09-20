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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.jdbc.source;

import cn.sliew.scaleph.common.enums.JobStepTypeEnum;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeatunnelNativeFlinkPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.common.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.common.enums.SeatunnelNativeFlinkPluginEnum.JDBC_SOURCE;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connector.jdbc.JdbcProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connector.jdbc.source.JdbcSourceProperties.*;

@AutoService(SeatunnelNativeFlinkPlugin.class)
public class JdbcSourcePlugin extends SeatunnelNativeFlinkPlugin {

    public JdbcSourcePlugin() {
        this.pluginInfo = new PluginInfo(JDBC_SOURCE.getValue(), "jdbc source connector", JdbcSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(URL);
        props.add(DRIVER);
        props.add(USERNAME);
        props.add(PASSWORD);
        props.add(QUERY);
        props.add(FETCH_SIZE);
        props.add(PARALLELISM);
        props.add(PARTITION_COLUMN);
        props.add(PARTITION_UPPER_BOUND);
        props.add(PARTITION_LOWER_BOUND);

        props.add(CommonProperties.RESULT_TABLE_NAME);
        props.add(CommonProperties.FIELD_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public JobStepTypeEnum getStepType() {
        return JobStepTypeEnum.SOURCE;
    }

}
