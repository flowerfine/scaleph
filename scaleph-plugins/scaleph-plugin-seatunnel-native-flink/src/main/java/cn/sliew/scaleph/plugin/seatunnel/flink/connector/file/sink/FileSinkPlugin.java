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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.file.sink;

import cn.sliew.scaleph.common.enums.JobStepTypeEnum;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeatunnelNativeFlinkPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.common.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.common.enums.SeatunnelNativeFlinkPluginEnum.FILE_SINK;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connector.file.sink.FileSinkProperties.*;

@AutoService(SeatunnelNativeFlinkPlugin.class)
public class FileSinkPlugin extends SeatunnelNativeFlinkPlugin {

    public FileSinkPlugin() {
        this.pluginInfo = new PluginInfo(FILE_SINK.getValue(), "file sink connector", FileSinkPlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(FORMAT);
        props.add(PATH);
        props.add(PATH_TIME_FORMAT);
        props.add(WRITE_MODE);
        props.add(ROLLOVER_INTERVAL);
        props.add(MAX_PART_SIZE);
        props.add(PREFIX);
        props.add(SUFFIX);
        props.add(PARALLELISM);

        props.add(CommonProperties.SOURCE_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public JobStepTypeEnum getStepType() {
        return JobStepTypeEnum.SINK;
    }

}
