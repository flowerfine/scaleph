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
import cn.sliew.scaleph.dao.entity.master.dataservice.DataserviceResultMap;
import cn.sliew.scaleph.dao.mapper.master.dataservice.DataserviceResultMapMapper;
import cn.sliew.scaleph.dataservice.service.DataserviceResultMapService;
import cn.sliew.scaleph.dataservice.service.convert.DataserviceResultMapConvert;
import cn.sliew.scaleph.dataservice.service.dto.DataserviceResultMapDTO;
import cn.sliew.scaleph.dataservice.service.param.DataserviceResultMapAddParam;
import cn.sliew.scaleph.dataservice.service.param.DataserviceResultMapListParam;
import cn.sliew.scaleph.dataservice.service.param.DataserviceResultMapUpdateParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class DataserviceResultMapServiceImpl implements DataserviceResultMapService {

    @Autowired
    private DataserviceResultMapMapper dataserviceResultMapMapper;

    @Override
    public Page<DataserviceResultMapDTO> list(DataserviceResultMapListParam param) {
        Page<DataserviceResultMap> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<DataserviceResultMap> queryWrapper = Wrappers.lambdaQuery(DataserviceResultMap.class)
                .eq(DataserviceResultMap::getProjectId, param.getProjectId())
                .like(StringUtils.hasText(param.getName()), DataserviceResultMap::getName, param.getName());

        Page<DataserviceResultMap> dataserviceResultMapPage = dataserviceResultMapMapper.selectPage(page, queryWrapper);
        Page<DataserviceResultMapDTO> result = new Page<>(dataserviceResultMapPage.getCurrent(), dataserviceResultMapPage.getSize(), dataserviceResultMapPage.getTotal());
        List<DataserviceResultMapDTO> dataserviceResultMapDTOS = DataserviceResultMapConvert.INSTANCE.toDto(dataserviceResultMapPage.getRecords());
        result.setRecords(dataserviceResultMapDTOS);
        return result;
    }

    @Override
    public DataserviceResultMapDTO selectOne(Long id) {
        DataserviceResultMap record = dataserviceResultMapMapper.selectById(id);
        checkState(record != null, () -> "data service result map not exists for id: " + id);
        return DataserviceResultMapConvert.INSTANCE.toDto(record);
    }

    @Override
    public int insert(DataserviceResultMapAddParam param) {
        DataserviceResultMap record = BeanUtil.copy(param, new DataserviceResultMap());
        return dataserviceResultMapMapper.insert(record);
    }

    @Override
    public int update(DataserviceResultMapUpdateParam param) {
        DataserviceResultMap record = BeanUtil.copy(param, new DataserviceResultMap());
        return dataserviceResultMapMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return dataserviceResultMapMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return dataserviceResultMapMapper.deleteBatchIds(ids);
    }
}
