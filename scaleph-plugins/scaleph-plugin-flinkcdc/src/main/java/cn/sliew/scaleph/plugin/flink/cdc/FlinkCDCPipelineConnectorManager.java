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

package cn.sliew.scaleph.plugin.flink.cdc;

import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCPluginName;
import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCPluginType;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.core.PluginSPILoader;
import cn.sliew.scaleph.plugin.framework.exception.PluginException;

import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class FlinkCDCPipelineConnectorManager {

    private PluginSPILoader<FlinkCDCPipilineConnectorPlugin> pluginPluginSPILoader = new PluginSPILoader<>(FlinkCDCPipilineConnectorPlugin.class, FlinkCDCPipilineConnectorPlugin.class.getClassLoader());

    public Set<FlinkCDCPipilineConnectorPlugin> getAvailableConnectors(FlinkCDCPluginType pluginType) {
        return pluginPluginSPILoader.getServices().values().stream()
                .filter(connector -> connector.getPluginType() == pluginType)
                .collect(Collectors.toSet());
    }

    public FlinkCDCPipilineConnectorPlugin getConnector(PluginInfo pluginInfo) throws PluginException {
        final Optional<FlinkCDCPipilineConnectorPlugin> optional = pluginPluginSPILoader.getPlugin(pluginInfo);
        return optional.orElseThrow(() -> new PluginException("FlinkCDCPipilineConnectorPlugin", "unknown plugin info for " + pluginInfo));
    }

    public FlinkCDCPipilineConnectorPlugin getConnector(FlinkCDCPluginType pluginType, FlinkCDCPluginName pluginName) throws PluginException {
        return pluginPluginSPILoader.getServices().values().stream()
                .filter(connector -> connector.getPluginType() == pluginType)
                .filter(connector -> connector.getPluginName() == pluginName)
                .findAny().orElseThrow(() -> new PluginException("FlinkCDCPipilineConnectorPlugin"));
    }

    public FlinkCDCPipilineConnectorPlugin newConnector(String name, Properties props) {
        return pluginPluginSPILoader.newInstance(name, props);
    }

}
