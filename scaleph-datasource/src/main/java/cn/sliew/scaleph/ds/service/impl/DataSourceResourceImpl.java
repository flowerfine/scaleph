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

package cn.sliew.scaleph.ds.service.impl;

import cn.sliew.carp.framework.common.dict.datasource.DataSourceType;
import cn.sliew.carp.framework.common.model.PageResult;
import cn.sliew.carp.module.datasource.service.CarpDsInfoService;
import cn.sliew.carp.module.datasource.service.dto.DsInfoDTO;
import cn.sliew.carp.module.datasource.service.param.DsInfoListParam;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.ds.service.DataSourceResource;
import cn.sliew.scaleph.resource.service.enums.ResourceType;
import cn.sliew.scaleph.resource.service.param.ResourceListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataSourceResourceImpl implements DataSourceResource {

    @Autowired
    private CarpDsInfoService carpDsInfoService;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.DATASOURCE;
    }

    @Override
    public Page<DsInfoDTO> list(ResourceListParam param) {
        DsInfoListParam target = BeanUtil.copy(param, new DsInfoListParam());
        target.setDsType(DataSourceType.of(param.getLabel()));
        PageResult<DsInfoDTO> pageResult = carpDsInfoService.list(target);
        Page<DsInfoDTO> page = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        page.setRecords(pageResult.getRecords());
        return page;
    }

    @Override
    public DsInfoDTO getRaw(Long id) {
        return carpDsInfoService.selectOne(id, true);
    }
}
