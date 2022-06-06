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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.jdbc.sink;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.enums.JobStepTypeEnum;
import cn.sliew.scaleph.plugin.framework.core.AbstractPlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeatunnelNativeFlinkConnector;
import cn.sliew.scaleph.plugin.seatunnel.flink.common.CommonProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connector.jdbc.JdbcProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connector.jdbc.sink.JdbcSinkProperties.BATCH_SIZE;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connector.jdbc.sink.JdbcSinkProperties.QUERY;

public class JdbcSinkConnector extends AbstractPlugin implements SeatunnelNativeFlinkConnector {

    private static final List<PropertyDescriptor> supportedProperties;

    static {
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(URL);
        props.add(DRIVER);
        props.add(USERNAME);
        props.add(PASSWORD);
        props.add(QUERY);
        props.add(BATCH_SIZE);
        props.add(PARALLELISM);

        props.add(CommonProperties.SOURCE_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    private final PluginInfo pluginInfo;

    public JdbcSinkConnector() {
        this.pluginInfo = new PluginInfo("JdbcSink", "jdbc sink connector", "2.1.1", JdbcSinkConnector.class.getName());
    }

    @Override
    public ObjectNode createConf() {
        ObjectNode objectNode = JacksonUtil.createObjectNode();
        for (PropertyDescriptor descriptor : supportedProperties) {
            if (properties.contains(descriptor)) {
                objectNode.put(descriptor.getName(), properties.getValue(descriptor));
            }
        }
        return objectNode;
    }

    @Override
    public JobStepTypeEnum getStepType() {
        return JobStepTypeEnum.SINK;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return pluginInfo;
    }

    @Override
    public List<PropertyDescriptor> getSupportedProperties() {
        return supportedProperties;
    }

    @Override
    public List<PropertyDescriptor> additionalResources() {
        return Collections.emptyList();
    }
}
