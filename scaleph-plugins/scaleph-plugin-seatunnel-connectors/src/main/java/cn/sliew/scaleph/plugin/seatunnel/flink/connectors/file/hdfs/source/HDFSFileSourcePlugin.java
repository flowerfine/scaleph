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

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.ds.modal.AbstractDataSource;
import cn.sliew.scaleph.ds.modal.file.HDFSDataSource;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import cn.sliew.scaleph.plugin.seatunnel.flink.resource.ResourceProperties;
import cn.sliew.scaleph.plugin.seatunnel.flink.resource.ResourceProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.service.AutoService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.FileProperties.PATH;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.FileSourceProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.hdfs.HDFSProperties.FS_DEFAULT_FS;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.hdfs.HDFSProperties.HDFS_SITE_PATH;

@AutoService(SeaTunnelConnectorPlugin.class)
public class HDFSFileSourcePlugin extends SeaTunnelConnectorPlugin {

    public HDFSFileSourcePlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Read data from HDFS",
                HDFSFileSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(PATH);
        props.add(TYPE);
        props.add(SCHEMA);
        props.add(DELIMITER);
        props.add(PARSE_PARTITION_FROM_PATH);
        props.add(DATE_FORMAT);
        props.add(TIME_FORMAT);
        props.add(DATETIME_FORMAT);
        props.add(CommonProperties.PARALLELISM);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public List<ResourceProperty> getRequiredResources() {
        return Collections.singletonList(ResourceProperties.DATASOURCE_RESOURCE);
    }

    @Override
    public ObjectNode createConf() {
        ObjectNode conf = super.createConf();
        JsonNode jsonNode = properties.get(ResourceProperties.DATASOURCE);
        HDFSDataSource dataSource = (HDFSDataSource) AbstractDataSource.fromDsInfo((ObjectNode) jsonNode);
        conf.put(FS_DEFAULT_FS.getName(), dataSource.getFsDefaultFS());
        if (StringUtils.hasText(dataSource.getHdfsSitePath())) {
            conf.put(HDFS_SITE_PATH.getName(), dataSource.getHdfsSitePath());
        }
        return conf;
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_HDFS_FILE;
    }

}
