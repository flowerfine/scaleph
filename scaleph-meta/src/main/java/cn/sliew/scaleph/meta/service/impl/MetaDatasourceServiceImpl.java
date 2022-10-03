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

package cn.sliew.scaleph.meta.service.impl;

import cn.sliew.scaleph.common.codec.CodecUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.exception.Rethrower;
import cn.sliew.scaleph.common.param.PropertyUtil;
import cn.sliew.scaleph.dao.entity.master.meta.MetaDatasource;
import cn.sliew.scaleph.dao.mapper.master.meta.MetaDatasourceMapper;
import cn.sliew.scaleph.meta.service.MetaDatasourceService;
import cn.sliew.scaleph.meta.service.convert.MetaDataSourceConvert;
import cn.sliew.scaleph.meta.service.dto.MetaDatasourceDTO;
import cn.sliew.scaleph.meta.service.param.MetaDatasourceParam;
import cn.sliew.scaleph.plugin.datasource.DatasourceManager;
import cn.sliew.scaleph.plugin.datasource.DatasourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.Property;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class MetaDatasourceServiceImpl implements MetaDatasourceService {

    private DatasourceManager datasourceManager = new DatasourceManager();

    @Autowired
    private MetaDatasourceMapper metaDatasourceMapper;

    @Override
    public Set<PluginInfo> getAvailableDataSources() {
        return datasourceManager.getAvailableDataSources();
    }

    @Override
    public List<PropertyDescriptor> getSupportedProperties(String name) {
        Set<PluginInfo> pluginInfoSet = getAvailableDataSources();
        String className = null;
        for (PluginInfo pluginInfo : pluginInfoSet) {
            if (pluginInfo.getName().equalsIgnoreCase(name)) {
                name = pluginInfo.getName();
                className = pluginInfo.getClassname();
            }
        }
        PluginInfo pluginInfo = new PluginInfo(name, null, className);
        return datasourceManager.getSupportedProperties(pluginInfo);
    }

    @Override
    public int insert(MetaDatasourceDTO metaDatasourceDTO) {
        if (validateProps(metaDatasourceDTO)) {
            this.encryptProps(metaDatasourceDTO, true);
            final MetaDatasource metaDatasource =
                MetaDataSourceConvert.INSTANCE.toDo(metaDatasourceDTO);
            return metaDatasourceMapper.insert(metaDatasource);
        }
        return 0;
    }

    @Override
    public int update(MetaDatasourceDTO metaDatasourceDTO) {
        if (validateProps(metaDatasourceDTO)) {
            this.encryptProps(metaDatasourceDTO, true);
            final MetaDatasource metaDatasource =
                MetaDataSourceConvert.INSTANCE.toDo(metaDatasourceDTO);
            return metaDatasourceMapper.updateById(metaDatasource);
        }
        return 0;
    }

    /**
     * todo check is datasource is used
     */

    @Override
    public int deleteById(Long id) {
        return metaDatasourceMapper.deleteById(id);
    }

    /**
     * todo check is datasource is used
     */
    @Override
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        return metaDatasourceMapper.deleteBatchIds(map.values());
    }

    @Override
    public MetaDatasourceDTO selectOne(Serializable id, boolean encrypt) {
        final MetaDatasource datasource = metaDatasourceMapper.selectById(id);
        MetaDatasourceDTO datasourceDTO = MetaDataSourceConvert.INSTANCE.toDto(datasource);
        encryptProps(datasourceDTO, encrypt);
        return datasourceDTO;
    }

    @Override
    public Page<MetaDatasourceDTO> selectPage(MetaDatasourceParam param) {
        Page<MetaDatasource> list = metaDatasourceMapper.selectPage(
            new Page<>(param.getCurrent(), param.getPageSize()),
            Wrappers.lambdaQuery(MetaDatasource.class)
                .like(StringUtils.hasText(param.getDatasourceName()),
                    MetaDatasource::getDatasourceName,
                    param.getDatasourceName())
                .eq(StringUtils.hasText(param.getDatasourceType()),
                    MetaDatasource::getDatasourceType,
                    param.getDatasourceType())
        );
        Page<MetaDatasourceDTO> result =
            new Page<>(list.getCurrent(), list.getSize(), list.getTotal());
        List<MetaDatasourceDTO> dtoList = MetaDataSourceConvert.INSTANCE.toDto(list.getRecords());
//        dtoList.forEach(this::cleanSensitiveParam);
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public List<MetaDatasourceDTO> listByType(String type) {
        List<MetaDatasource> list = this.metaDatasourceMapper.selectList(
            Wrappers.lambdaQuery(MetaDatasource.class)
                .eq(MetaDatasource::getDatasourceType, type)
        );
        return MetaDataSourceConvert.INSTANCE.toDto(list);
    }

    public void encryptProps(MetaDatasourceDTO metaDatasourceDTO, boolean encrypt) {
        String pluginName = metaDatasourceDTO.getDatasourceType().getValue();
        Map<String, Object> propMap = metaDatasourceDTO.getProps();
        List<PropertyDescriptor> propDescList = getSupportedProperties(pluginName);
        for (PropertyDescriptor prop : propDescList) {
            if (propMap.containsKey(prop.getName()) == false
                || StringUtils.isEmpty(propMap.get(prop.getName()))) {
                continue;
            }
            EnumSet<Property> propEnumSet = prop.getProperties();
            if (propEnumSet.contains(Property.Sensitive)) {
                String value = (String) propMap.get(prop.getName());
                if (encrypt && !isEncryptedStr(value)) {
                    String encodeValue =
                        Constants.CODEC_STR_PREFIX + CodecUtil.encodeToBase64(value);
                    propMap.put(prop.getName(), encodeValue);
                } else if (!encrypt && isEncryptedStr(value)) {
                    String decodeValue = CodecUtil
                        .decodeFromBase64(value.substring(Constants.CODEC_STR_PREFIX.length()));
                    propMap.put(prop.getName(), decodeValue);
                }
            }
        }
    }

    @Override
    public boolean testConnection(MetaDatasourceDTO metaDatasourceDTO) {
        boolean result = false;
        this.encryptProps(metaDatasourceDTO, false);
        Set<PluginInfo> pluginInfoSet = getAvailableDataSources();
        try {
            for (PluginInfo pluginInfo : pluginInfoSet) {
                if (pluginInfo.getName()
                    .equalsIgnoreCase(metaDatasourceDTO.getDatasourceType().getValue())) {
                    Class clazz = Class.forName(pluginInfo.getClassname());
                    DatasourcePlugin datasource = (DatasourcePlugin) clazz.newInstance();
                    datasource.setAdditionalProperties(
                        PropertyUtil.mapToProperties(metaDatasourceDTO.getAdditionalProps()));
                    datasource.configure(PropertyContext.fromMap(metaDatasourceDTO.getProps()));
                    datasource.start();
                    result = datasource.testConnection();
                    datasource.shutdown();
                }
            }
        } catch (Exception e) {
            Rethrower.throwAs(e);
        }
        return result;
    }

    /**
     * @param str str
     * @return true/false
     */
    private boolean isEncryptedStr(String str) {
        return str.startsWith(Constants.CODEC_STR_PREFIX);
    }

    private boolean validateProps(MetaDatasourceDTO metaDatasourceDTO) {
        String pluginName = metaDatasourceDTO.getDatasourceType().getValue();
        Set<PluginInfo> pluginInfoSet = getAvailableDataSources();
        try {
            for (PluginInfo pluginInfo : pluginInfoSet) {
                if (pluginInfo.getName().equalsIgnoreCase(pluginName)) {
                    Class clazz = Class.forName(pluginInfo.getClassname());
                    DatasourcePlugin datasource = (DatasourcePlugin) clazz.newInstance();
                    datasource.configure(PropertyContext.fromMap(metaDatasourceDTO.getProps()));
                    return true;
                }
            }
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException |
            IllegalArgumentException e) {
            Rethrower.throwAs(e);
        }
        return false;
    }

    private void cleanSensitiveParam(MetaDatasourceDTO metaDatasourceDTO) {
        String pluginName = metaDatasourceDTO.getDatasourceType().getValue();
        Map<String, Object> propMap = metaDatasourceDTO.getProps();
        List<PropertyDescriptor> propDescList = getSupportedProperties(pluginName);
        for (PropertyDescriptor prop : propDescList) {
            EnumSet<Property> propEnumSet = prop.getProperties();
            if (propEnumSet.contains(Property.Sensitive)) {
                propMap.remove(prop.getName());
            }
        }
        metaDatasourceDTO.setPropsStr(propMap);
    }
}
