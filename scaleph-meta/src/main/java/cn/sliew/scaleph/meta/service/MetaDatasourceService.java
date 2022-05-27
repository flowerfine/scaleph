package cn.sliew.scaleph.meta.service;

import cn.sliew.scaleph.meta.service.dto.MetaDatasourceDTO;
import cn.sliew.scaleph.meta.service.param.MetaDatasourceParam;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MetaDatasourceService {

    Set<PluginInfo> getAvailableDataSources();

    List<PropertyDescriptor> getSupportedProperties(PluginInfo pluginInfo);

    int insert(MetaDatasourceDTO metaDatasourceDTO);

    int update(MetaDatasourceDTO metaDatasourceDTO);

    int deleteById(Long id);

    int deleteBatch(Map<Integer, ? extends Serializable> map);

    MetaDatasourceDTO selectOne(Long id);

    Page<MetaDatasourceDTO> selectPage(MetaDatasourceParam param);

}
