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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.hdfs.source;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.hdfs.source.HdfsFileSourceProperties.FS_DEFAULT_FS;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.hdfs.source.HdfsFileSourceProperties.PATH;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.hdfs.source.HdfsFileSourceProperties.SCHEMA;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.hdfs.source.HdfsFileSourceProperties.TYPE;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.service.AutoService;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoService(SeaTunnelConnectorPlugin.class)
public class HdfsFileSourcePlugin extends SeaTunnelConnectorPlugin {

    public HdfsFileSourcePlugin() {
        this.pluginInfo = new PluginInfo(getPluginName().getLabel(),
            "Read data from hdfs FileSystem",
            HdfsFileSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(PATH);
        props.add(TYPE);
        props.add(FS_DEFAULT_FS);
        props.add(SCHEMA);
        props.add(CommonProperties.FIELD_NAME);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public ObjectNode createConf() {
        ObjectNode objectNode = JacksonUtil.createObjectNode();
        for (PropertyDescriptor descriptor : getSupportedProperties()) {
            if (properties.contains(descriptor)) {
                if (SCHEMA.getName().equals(descriptor.getName())) {
                    Config config = ConfigFactory.parseString(properties.getValue(descriptor));
                    ConfigRenderOptions options = ConfigRenderOptions.concise();
                    String schema = config.root().render(options);
                    ObjectNode jsonNodes = (ObjectNode) JacksonUtil.toJsonNode(schema);
                    ObjectNode filedNode = JacksonUtil.createObjectNode();
                    JsonNode filed = filedNode.set("filed", jsonNodes);
                    ObjectNode schemaNode = JacksonUtil.createObjectNode();
                    schemaNode.set("schema", filed);
                    objectNode.set(descriptor.getName(), schemaNode);
                } else {
                    objectNode.put(descriptor.getName(), properties.getValue(descriptor));
                }
            }
        }
        return objectNode;
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_HDFS_FILE;
    }

}
