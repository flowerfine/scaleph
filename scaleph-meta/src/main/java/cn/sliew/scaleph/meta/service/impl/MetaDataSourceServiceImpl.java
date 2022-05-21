package cn.sliew.scaleph.meta.service.impl;

import cn.sliew.scaleph.plugin.datasource.DataSourceManager;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.meta.service.MetaDataSourceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MetaDataSourceServiceImpl implements MetaDataSourceService {

    private DataSourceManager dataSourceManager = new DataSourceManager();

    @Override
    public Set<PluginInfo> getAvailableDataSources() {
        return dataSourceManager.getAvailableDataSources();
    }

    @Override
    public List<PropertyDescriptor> getSupportedProperties(PluginInfo pluginInfo) {
        return dataSourceManager.getSupportedProperties(pluginInfo);
    }
}
