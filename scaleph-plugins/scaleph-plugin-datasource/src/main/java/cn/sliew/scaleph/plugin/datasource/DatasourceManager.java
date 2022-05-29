package cn.sliew.scaleph.plugin.datasource;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
        final DatasourcePlugin dataSourcePlugin = optional.orElseThrow(
            () -> new IllegalStateException("unknown plugin info for " + pluginInfo));
        return dataSourcePlugin.getSupportedProperties();
    }

    public <T> DatasourcePlugin<T> newDatasourcePlugin(PluginInfo pluginInfo,
                                                       Map<String, Object> props,
                                                       Map<String, Object> additionalProperties) {
        return null;
    }
}
