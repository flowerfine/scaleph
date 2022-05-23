package cn.sliew.scaleph.meta.service.impl;

import cn.sliew.scaleph.dao.entity.master.meta.MetaDatasource;
import cn.sliew.scaleph.dao.mapper.master.meta.MetaDatasourceMapper;
import cn.sliew.scaleph.meta.service.convert.MetaDataSourceConvert;
import cn.sliew.scaleph.meta.service.dto.MetaDatasourceDTO;
import cn.sliew.scaleph.plugin.datasource.DataSourceManager;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.meta.service.MetaDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MetaDataSourceServiceImpl implements MetaDataSourceService {

    private DataSourceManager dataSourceManager = new DataSourceManager();

    @Autowired
    private MetaDatasourceMapper metaDatasourceMapper;

    @Override
    public Set<PluginInfo> getAvailableDataSources() {
        return dataSourceManager.getAvailableDataSources();
    }

    @Override
    public List<PropertyDescriptor> getSupportedProperties(PluginInfo pluginInfo) {
        return dataSourceManager.getSupportedProperties(pluginInfo);
    }

    /**
     * fixme validate properties and encode password
     */
    @Override
    public int insert(MetaDatasourceDTO metaDatasourceDTO) {
        final PluginInfo pluginInfo = new PluginInfo(metaDatasourceDTO.getName(), metaDatasourceDTO.getVersion(), null, null);
        final List<PropertyDescriptor> supportedProperties = getSupportedProperties(pluginInfo);
        // 验证参数

        final MetaDatasource metaDatasource = MetaDataSourceConvert.INSTANCE.toDo(metaDatasourceDTO);
        return metaDatasourceMapper.insert(metaDatasource);
    }
}
