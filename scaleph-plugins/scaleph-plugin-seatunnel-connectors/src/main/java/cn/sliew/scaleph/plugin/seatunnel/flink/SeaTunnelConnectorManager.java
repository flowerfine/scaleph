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

package cn.sliew.scaleph.plugin.seatunnel.flink;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.core.PluginSPILoader;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.*;
import cn.sliew.scaleph.system.dict.seatunnel.SeaTunnelPluginType;

import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class SeaTunnelConnectorManager {

    private static List<PropertyDescriptor> envProperties;

    static {
        List<PropertyDescriptor> supportedProperties = new ArrayList<>();
        supportedProperties.add(JobNameProperties.JOB_NAME);
        supportedProperties.addAll(CommonProperties.SUPPORTED_PROPERTIES);
        supportedProperties.addAll(CheckpointProperties.SUPPORTED_PROPERTIES);
        supportedProperties.addAll(EnvironmentProperties.SUPPORTED_PROPERTIES);
        supportedProperties.addAll(FaultToleranceProperties.SUPPORTED_PROPERTIES);
        supportedProperties.addAll(IdleStateRetentionProperties.SUPPORTED_PROPERTIES);
        envProperties = Collections.unmodifiableList(supportedProperties);
    }

    private PluginSPILoader<SeaTunnelConnectorPlugin> pluginPluginSPILoader = new PluginSPILoader<>(SeaTunnelConnectorPlugin.class, SeaTunnelConnectorPlugin.class.getClassLoader());

    public List<PropertyDescriptor> getSupportedEnvProperties() {
        return envProperties;
    }

    public Set<PluginInfo> getAvailableConnectors(SeaTunnelPluginType pluginType) {
        return pluginPluginSPILoader.getServices().values().stream()
                .filter(connector -> connector.getPluginType() == pluginType)
                .map(SeaTunnelConnectorPlugin::getPluginInfo)
                .collect(Collectors.toSet());
    }

    public SeaTunnelConnectorPlugin getConnector(String name) {
        PluginInfo pluginInfo = new PluginInfo(name, null, null, null);
        final Optional<SeaTunnelConnectorPlugin> optional = pluginPluginSPILoader.getPlugin(pluginInfo);
        return optional.orElseThrow(() -> new IllegalStateException("unknown plugin info for " + pluginInfo));
    }

    public SeaTunnelConnectorPlugin newConnector(String name, Properties props) {
        return pluginPluginSPILoader.newInstance(name, props);
    }

    public URL findConnectorJarPath(Path connectorsRootDir, SeaTunnelPluginMapping mapping) {
        return null;
    }

}
