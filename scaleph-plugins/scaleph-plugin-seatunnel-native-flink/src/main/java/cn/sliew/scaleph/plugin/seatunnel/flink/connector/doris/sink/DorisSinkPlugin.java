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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.doris.sink;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.enums.JobStepTypeEnum;
import cn.sliew.scaleph.common.param.PropertyUtil;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeatunnelNativeFlinkPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.common.CommonProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.sliew.scaleph.common.enums.SeatunnelNativeFlinkPluginEnum.DORIS_SINK;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connector.doris.sink.DorisSinkProperties.*;

@AutoService(SeatunnelNativeFlinkPlugin.class)
public class DorisSinkPlugin extends SeatunnelNativeFlinkPlugin {

    public DorisSinkPlugin() {
        this.pluginInfo = new PluginInfo(DORIS_SINK.getValue(), "doris sink connector", DorisSinkPlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(TABLE);
        props.add(BATCH_SIZE);
        props.add(INTERVAL);
        props.add(MAX_RETRIES);
        props.add(DORIS_CONF);
        props.add(PARALLELISM);
        props.add(FENODES);
        props.add(USER);
        props.add(PASSWORD);
        props.add(DATABASE);

        props.add(CommonProperties.SOURCE_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public JobStepTypeEnum getStepType() {
        return JobStepTypeEnum.SINK;
    }

    @Override
    public ObjectNode createConf() {
        ObjectNode objectNode = JacksonUtil.createObjectNode();
        for (PropertyDescriptor descriptor : getSupportedProperties()) {
            if (properties.contains(descriptor)) {
                if (DORIS_CONF.getName().equals(descriptor.getName())) {
                    Map<String, Object> map = PropertyUtil.formatPropFromStr(properties.getValue(descriptor), "\n", "=");
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        objectNode.put("doris." + entry.getKey(), String.valueOf(entry.getValue()));
                    }
                } else {
                    objectNode.put(descriptor.getName(), properties.getValue(descriptor));
                }
            }
        }
        return objectNode;
    }
}
