package cn.sliew.scaleph.plugin.datasource;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.core.PluginSPILoader;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;

import java.util.*;

public class DataSourceManager {

    private PluginSPILoader<DataSourcePlugin> pluginPluginSPILoader = new PluginSPILoader<>(DataSourcePlugin.class, DataSourcePlugin.class.getClassLoader());

    public Set<PluginInfo> getAvailableDataSources() {
        return pluginPluginSPILoader.availableServices();
    }

    public List<PropertyDescriptor> getSupportedProperties(PluginInfo pluginInfo) {
        final Optional<DataSourcePlugin> optional = pluginPluginSPILoader.getPlugin(pluginInfo);
        final DataSourcePlugin dataSourcePlugin = optional.orElseThrow(() -> new IllegalStateException("unknown plugin info for " + pluginInfo));
        return dataSourcePlugin.getSupportedProperties();
    }

    public <T> DataSourcePlugin<T> newDataSourcePlugin(PluginInfo pluginInfo, Properties properties, Properties additionalProperties) {
        return null;
    }
}
