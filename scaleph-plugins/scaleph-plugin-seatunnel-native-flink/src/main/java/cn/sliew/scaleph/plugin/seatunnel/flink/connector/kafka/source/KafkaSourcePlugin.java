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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.kafka.source;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.enums.JobStepTypeEnum;
import cn.sliew.scaleph.common.param.PropertyUtil;
import cn.sliew.scaleph.meta.service.MetaDatasourceService;
import cn.sliew.scaleph.meta.service.dto.MetaDatasourceDTO;
import cn.sliew.scaleph.plugin.datasource.kafka.KafkaProperties;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeatunnelNativeFlinkPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.common.CommonProperties;
import cn.sliew.scaleph.system.service.vo.DictVO;
import cn.sliew.scaleph.system.util.SpringApplicationContextUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.sliew.scaleph.common.enums.SeatunnelNativeFlinkPluginEnum.KAFKA_SOURCE;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connector.kafka.source.KafkaSourceProperties.*;

@AutoService(SeatunnelNativeFlinkPlugin.class)
public class KafkaSourcePlugin extends SeatunnelNativeFlinkPlugin {

    public KafkaSourcePlugin() {
        this.pluginInfo = new PluginInfo(KAFKA_SOURCE.getValue(), "kafka source connector", KafkaSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(TOPICS);
        props.add(CONSUMER_GROUP_ID);
        props.add(CONSUMER_BOOTSTRAP_SERVERS);
        props.add(FORMAT_TYPE);
        props.add(FORMAT_CONF);
        props.add(SCHEMA);
        props.add(ROWTIME_FIELD);
        props.add(WATERMARK);
        props.add(OFFSET_RESET);
        props.add(OFFSET_RESET_SPECIFIC);
        props.add(CONSUMER_CONF);

        props.add(CommonProperties.RESULT_TABLE_NAME);
        props.add(CommonProperties.FIELD_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public JobStepTypeEnum getStepType() {
        return JobStepTypeEnum.SOURCE;
    }

    @Override
    public ObjectNode createConf() {
        ObjectNode objectNode = JacksonUtil.createObjectNode();
        for (PropertyDescriptor descriptor : getSupportedProperties()) {
            if (properties.contains(descriptor)) {
                if (CONSUMER_BOOTSTRAP_SERVERS.getName().equals(descriptor.getName())) {
                    DictVO dictVO = JacksonUtil.parseJsonString(properties.getValue(descriptor), DictVO.class);
                    MetaDatasourceService metaDatasourceService = SpringApplicationContextUtil.getBean(MetaDatasourceService.class);
                    MetaDatasourceDTO metaDatasource = metaDatasourceService.selectOne(dictVO.getValue(), false);
                    String bootStrapServers = metaDatasource.getProps().get(KafkaProperties.BOOTSTRAP_SERVERS.getName()).toString();
                    objectNode.put(CONSUMER_BOOTSTRAP_SERVERS.getName().replace("_", "."), bootStrapServers);
                } else if (CONSUMER_CONF.getName().equals(descriptor.getName())) {
                    Map<String, Object> map = PropertyUtil.formatPropFromStr(properties.getValue(descriptor), "\n", "=");
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        objectNode.put("consumer." + entry.getKey(), String.valueOf(entry.getValue()));
                    }
                } else if (FORMAT_CONF.getName().equals(descriptor.getName())) {
                    Map<String, Object> map = PropertyUtil.formatPropFromStr(properties.getValue(descriptor), "\n", "=");
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        objectNode.put(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                } else if (descriptor.getName().contains("_")) {
                    objectNode.put(descriptor.getName().replace("_", "."), properties.getValue(descriptor));
                } else {
                    objectNode.put(descriptor.getName(), properties.getValue(descriptor));
                }
            }
        }
        return objectNode;
    }
}
