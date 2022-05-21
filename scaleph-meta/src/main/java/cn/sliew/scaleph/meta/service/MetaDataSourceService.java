package cn.sliew.scaleph.meta.service;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;

import java.util.List;
import java.util.Set;

public interface MetaDataSourceService {

    Set<PluginInfo> getAvailableDataSources();

    List<PropertyDescriptor> getSupportedProperties(PluginInfo pluginInfo);
}
