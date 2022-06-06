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

package cn.sliew.scaleph.plugin.datasource;

import java.util.*;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.core.PluginSPILoader;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;

public class DatasourceManager {

    private PluginSPILoader<DatasourcePlugin> pluginPluginSPILoader =
        new PluginSPILoader<>(DatasourcePlugin.class, DatasourcePlugin.class.getClassLoader());

    public Set<PluginInfo> getAvailableDataSources() {
        return pluginPluginSPILoader.availableServices();
    }

    public List<PropertyDescriptor> getSupportedProperties(PluginInfo pluginInfo) {
        final Optional<DatasourcePlugin> optional = pluginPluginSPILoader.getPlugin(pluginInfo);
        final DatasourcePlugin dataSourcePlugin = optional.orElseThrow(() -> new IllegalStateException("unknown plugin info for " + pluginInfo));
        return dataSourcePlugin.getSupportedProperties();
    }

    public <T> DatasourcePlugin<T> newDatasourcePlugin(String name, Map<String, Object> props,
                                                       Map<String, Object> additionalProps) {
        Properties properties = new Properties();
        properties.putAll(props);
        final DatasourcePlugin datasourcePlugin = pluginPluginSPILoader.newInstance(name, properties);
        Properties additionalProperties = new Properties();
        additionalProperties.putAll(additionalProps);
        datasourcePlugin.setAdditionalProperties(additionalProperties);
        datasourcePlugin.start();
        return datasourcePlugin;
    }
}
