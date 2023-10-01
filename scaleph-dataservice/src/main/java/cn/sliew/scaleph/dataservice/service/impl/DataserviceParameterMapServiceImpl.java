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

package cn.sliew.scaleph.dataservice.service.impl;

import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.entity.master.dataservice.DataserviceParameterMap;
import cn.sliew.scaleph.dao.mapper.master.dataservice.DataserviceParameterMapMapper;
import cn.sliew.scaleph.dataservice.service.DataserviceParameterMapService;
import cn.sliew.scaleph.dataservice.service.convert.DataserviceParameterMapConvert;
import cn.sliew.scaleph.dataservice.service.dto.DataserviceParameterMapDTO;
import cn.sliew.scaleph.dataservice.service.param.DataserviceParameterMapAddParam;
import cn.sliew.scaleph.dataservice.service.param.DataserviceParameterMapListParam;
import cn.sliew.scaleph.dataservice.service.param.DataserviceParameterMapUpdateParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class DataserviceParameterMapServiceImpl implements DataserviceParameterMapService {

    @Autowired
    private DataserviceParameterMapMapper dataserviceParameterMapMapper;

    @Override
    public Page<DataserviceParameterMapDTO> list(DataserviceParameterMapListParam param) {
        Page<DataserviceParameterMap> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<DataserviceParameterMap> queryWrapper = Wrappers.lambdaQuery(DataserviceParameterMap.class)
                .eq(DataserviceParameterMap::getProjectId, param.getProjectId())
                .like(StringUtils.hasText(param.getName()), DataserviceParameterMap::getName, param.getName());

        Page<DataserviceParameterMap> dataserviceParameterMapPage = dataserviceParameterMapMapper.selectPage(page, queryWrapper);
        Page<DataserviceParameterMapDTO> result = new Page<>(dataserviceParameterMapPage.getCurrent(), dataserviceParameterMapPage.getSize(), dataserviceParameterMapPage.getTotal());
        List<DataserviceParameterMapDTO> dataserviceParameterMapDTOS = DataserviceParameterMapConvert.INSTANCE.toDto(dataserviceParameterMapPage.getRecords());
        result.setRecords(dataserviceParameterMapDTOS);
        return result;
    }

    @Override
    public DataserviceParameterMapDTO selectOne(Long id) {
        DataserviceParameterMap record = dataserviceParameterMapMapper.selectById(id);
        checkState(record != null, () -> "data service parameter map not exists for id: " + id);
        return DataserviceParameterMapConvert.INSTANCE.toDto(record);
    }

    @Override
    public int insert(DataserviceParameterMapAddParam param) {
        DataserviceParameterMap record = BeanUtil.copy(param, new DataserviceParameterMap());
        return dataserviceParameterMapMapper.insert(record);
    }

    @Override
    public int update(DataserviceParameterMapUpdateParam param) {
        DataserviceParameterMap record = BeanUtil.copy(param, new DataserviceParameterMap());
        return dataserviceParameterMapMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return dataserviceParameterMapMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return dataserviceParameterMapMapper.deleteBatchIds(ids);
    }
}
