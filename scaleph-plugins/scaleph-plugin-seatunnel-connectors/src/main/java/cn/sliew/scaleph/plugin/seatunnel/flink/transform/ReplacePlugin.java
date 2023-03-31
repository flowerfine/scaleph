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

package cn.sliew.scaleph.plugin.seatunnel.flink.transform;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoService(SeaTunnelConnectorPlugin.class)
public class ReplacePlugin extends SeaTunnelConnectorPlugin {

    public ReplacePlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Examines string value in a given field and replaces substring of the string value that matches the given string literal or regexes with the given replacement.",
                ReplacePlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(ReplaceProperties.REPLACE_FIELD);
        props.add(ReplaceProperties.PATTERN);
        props.add(ReplaceProperties.REPLACEMENT);
        props.add(ReplaceProperties.IS_REGEX);
        props.add(ReplaceProperties.REPLACE_FIRST);
        props.add(CommonProperties.SOURCE_TABLE_NAME);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        this.supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.TRANSFORM_REPLACE;
    }
}
