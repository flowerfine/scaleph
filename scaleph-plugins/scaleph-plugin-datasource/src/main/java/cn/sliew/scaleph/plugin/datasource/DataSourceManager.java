package cn.sliew.scaleph.plugin.datasource;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class DataSourceManager {

    public Set<PluginInfo> getAvailableDataSources() {
        return Collections.emptySet();
    }

    public List<PropertyDescriptor> getSupportedProperties(PluginInfo pluginInfo) {
        return Collections.emptyList();
    }

    public <T> DataSourcePlugin<T> newDataSourcePlugin(PluginInfo pluginInfo, Properties properties, Properties additionalProperties) {
        return null;
    }
}
