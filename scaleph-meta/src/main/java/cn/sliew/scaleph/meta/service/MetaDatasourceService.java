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

package cn.sliew.scaleph.meta.service;

import cn.sliew.scaleph.meta.service.dto.MetaDatasourceDTO;
import cn.sliew.scaleph.meta.service.param.MetaDatasourceParam;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.resource.service.ResourceDescriptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MetaDatasourceService {

    Set<PluginInfo> getAvailableDataSources();

    List<PropertyDescriptor> getSupportedProperties(String name);

    int insert(MetaDatasourceDTO metaDatasourceDTO);

    int update(MetaDatasourceDTO metaDatasourceDTO);

    int deleteById(Long id);

    int deleteBatch(Map<Integer, ? extends Serializable> map);

    MetaDatasourceDTO selectOne(Serializable id, boolean encrypt);

    Page<MetaDatasourceDTO> selectPage(MetaDatasourceParam param);

    List<MetaDatasourceDTO> listByType(String type);

    void encryptProps(MetaDatasourceDTO metaDatasourceDTO, boolean encrypt);

    boolean testConnection(MetaDatasourceDTO metaDatasourceDTO);

}
