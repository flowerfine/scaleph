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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.hdfs.sink;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.FileProperties.PATH;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.FileSinkProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.hdfs.HDFSProperties.FS_DEFAULT_FS;

@AutoService(SeaTunnelConnectorPlugin.class)
public class HDFSFileSinkPlugin extends SeaTunnelConnectorPlugin {

    public HDFSFileSinkPlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Write data to HDFS",
                HDFSFileSinkPlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(FS_DEFAULT_FS);
        props.add(PATH);
        props.add(FILE_FORMAT);
        props.add(FILE_NAME_EXPRESSION);
        props.add(FILENAME_TIME_FORMAT);
        props.add(FIELD_DELIMITER);
        props.add(ROW_DELIMITER);
        props.add(PARTITION_BY);
        props.add(PARTITION_DIR_EXPRESSION);
        props.add(IS_PARTITION_FIELD_WRITE_IN_FILE);
        props.add(SINK_COLUMNS);
        props.add(IS_ENABLE_TRANSACTION);
        props.add(CommonProperties.PARALLELISM);
        props.add(CommonProperties.SOURCE_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_HDFS_FILE;
    }
}
