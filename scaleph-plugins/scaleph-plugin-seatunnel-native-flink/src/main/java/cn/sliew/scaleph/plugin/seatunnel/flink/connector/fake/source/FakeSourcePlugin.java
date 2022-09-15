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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.fake.source;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.enums.JobStepTypeEnum;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeatunnelNativeFlinkPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.common.CommonProperties;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.service.AutoService;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static cn.sliew.scaleph.common.enums.SeatunnelNativeFlinkPluginEnum.FAKE_SOURCE;

@AutoService(SeatunnelNativeFlinkPlugin.class)
public class FakeSourcePlugin extends SeatunnelNativeFlinkPlugin {

    private final static String MOCK_DATA_SCHEMA_CONFIG = "mock_config";
    private final static String MOCK_DATA_SCHEMA_NAME = "name";
    private final static String MOCK_DATA_SCHEMA_TYPE = "type";
    private final static String MOCK_DATA_SCHEMA_MINVALUE = "minValue";
    private final static String MOCK_DATA_SCHEMA_MAXVALUE = "maxValue";
    private final static String MOCK_DATA_SCHEMA_VALUE_SEED = "valueSeed";

    public FakeSourcePlugin() {
        this.pluginInfo = new PluginInfo(FAKE_SOURCE.getValue(), "fake source connector", FakeSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(CommonProperties.RESULT_TABLE_NAME);
        props.add(CommonProperties.FIELD_NAME);
        props.add(FakeProperties.MOCK_DATA_SCHEMA);
        props.add(FakeProperties.MOCK_DATA_SIZE);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public JobStepTypeEnum getStepType() {
        return JobStepTypeEnum.SOURCE;
    }

    /**
     * @return
     */
    @Override
    public ObjectNode createConf() {
        ObjectNode objectNode = JacksonUtil.createObjectNode();
        for (PropertyDescriptor descriptor : getSupportedProperties()) {
            if (properties.contains(descriptor)) {
                if (FakeProperties.MOCK_DATA_SCHEMA.getName().equals(descriptor.getName())) {
                    parseMockDataSchema(objectNode, properties.getValue(descriptor));
                } else {
                    objectNode.put(descriptor.getName(), properties.getValue(descriptor));
                }
            }
        }
        return objectNode;
    }

    private void parseMockDataSchema(ObjectNode node, String dataSchema) {
        if (StringUtils.isNotEmpty(dataSchema)) {
            JSONArray jsonArray = JSONUtil.parseArray(dataSchema);
            Iterable<JSONObject> iterable = jsonArray.jsonIter();
            Iterator<JSONObject> iterator = iterable.iterator();
            ArrayNode arrayNode = JacksonUtil.createArrayNode();
            while (iterator.hasNext()) {
                JSONObject obj = iterator.next();
                if (obj.containsKey(MOCK_DATA_SCHEMA_NAME) && obj.containsKey(MOCK_DATA_SCHEMA_TYPE)) {
                    String type = obj.getJSONObject(MOCK_DATA_SCHEMA_TYPE).getStr("value");
                    ObjectNode objNode = JacksonUtil.createObjectNode();
                    objNode.put(MOCK_DATA_SCHEMA_NAME, obj.getStr(MOCK_DATA_SCHEMA_NAME));
                    objNode.put(MOCK_DATA_SCHEMA_TYPE, type);
                    switch (type) {
                        case "int":
                        case "bigint":
                            if (obj.containsKey(MOCK_DATA_SCHEMA_MAXVALUE)) {
                                ObjectNode mockConfigNode = JacksonUtil.createObjectNode();
                                ArrayNode arrayRange = JacksonUtil.createArrayNode();
                                if (obj.containsKey(MOCK_DATA_SCHEMA_MINVALUE)) {
                                    arrayRange.add(obj.getInt(MOCK_DATA_SCHEMA_MINVALUE));
                                } else {
                                    arrayRange.add(0);
                                }
                                arrayRange.add(obj.getInt(MOCK_DATA_SCHEMA_MAXVALUE));
                                mockConfigNode.set("int_range", arrayRange);
                                objNode.set(MOCK_DATA_SCHEMA_CONFIG, mockConfigNode);
                            }
                            break;
                        case "float":
                            if (obj.containsKey(MOCK_DATA_SCHEMA_MAXVALUE)) {
                                ObjectNode mockConfigNode = JacksonUtil.createObjectNode();
                                ArrayNode arrayRange = JacksonUtil.createArrayNode();
                                if (obj.containsKey(MOCK_DATA_SCHEMA_MINVALUE)) {
                                    arrayRange.add(obj.getFloat(MOCK_DATA_SCHEMA_MINVALUE));
                                } else {
                                    arrayRange.add(0.0f);
                                }
                                arrayRange.add(obj.getFloat(MOCK_DATA_SCHEMA_MAXVALUE));
                                mockConfigNode.set("float_range", arrayRange);
                                objNode.set(MOCK_DATA_SCHEMA_CONFIG, mockConfigNode);
                            }
                            break;
                        case "double":
                            if (obj.containsKey(MOCK_DATA_SCHEMA_MAXVALUE)) {
                                ObjectNode mockConfigNode = JacksonUtil.createObjectNode();
                                ArrayNode arrayRange = JacksonUtil.createArrayNode();
                                if (obj.containsKey(MOCK_DATA_SCHEMA_MINVALUE)) {
                                    arrayRange.add(obj.getDouble(MOCK_DATA_SCHEMA_MINVALUE));
                                } else {
                                    arrayRange.add(0.0);
                                }
                                arrayRange.add(obj.getDouble(MOCK_DATA_SCHEMA_MAXVALUE));
                                mockConfigNode.set("double_range", arrayRange);
                                objNode.set(MOCK_DATA_SCHEMA_CONFIG, mockConfigNode);
                            }
                            break;
                        case "varchar":
                            if (obj.containsKey(MOCK_DATA_SCHEMA_VALUE_SEED)) {
                                ObjectNode mockConfigNode = JacksonUtil.createObjectNode();
                                ArrayNode arrayRange = JacksonUtil.createArrayNode();
                                String seedStr = obj.getStr(MOCK_DATA_SCHEMA_VALUE_SEED);
                                if (StringUtils.isNotEmpty(seedStr)) {
                                    String[] seeds = seedStr.split(",");
                                    if (seeds.length > 0) {
                                        for (String seed : seeds) {
                                            arrayRange.add(seed);
                                        }
                                        mockConfigNode.set("string_seed", arrayRange);
                                        mockConfigNode.set("size_range", JacksonUtil.createArrayNode().add(1).add(1));
                                        objNode.set(MOCK_DATA_SCHEMA_CONFIG, mockConfigNode);
                                    }
                                }
                            }
                            break;
                        case "boolean":
                        case "timestamp":
                        default:
                            break;
                    }
                    arrayNode.add(objNode);
                } else {
                    throw new IllegalArgumentException("illegal argument in " + FakeProperties.MOCK_DATA_SCHEMA.getName() + "parameter");
                }

            }
            if (arrayNode.size() > 0) {
                node.set(FakeProperties.MOCK_DATA_SCHEMA.getName(), arrayNode);
            }
        }

    }

}
