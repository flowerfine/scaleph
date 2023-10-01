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
import cn.sliew.scaleph.dao.entity.master.dataservice.DataserviceParameterMapping;
import cn.sliew.scaleph.dao.mapper.master.dataservice.DataserviceParameterMappingMapper;
import cn.sliew.scaleph.dataservice.service.DataserviceParameterMappingService;
import cn.sliew.scaleph.dataservice.service.convert.DataserviceParameterMappingConvert;
import cn.sliew.scaleph.dataservice.service.dto.DataserviceParameterMappingDTO;
import cn.sliew.scaleph.dataservice.service.param.DataserviceParameterMappingAddParam;
import cn.sliew.scaleph.dataservice.service.param.DataserviceParameterMappingUpdateParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class DataserviceParameterMappingServiceImpl implements DataserviceParameterMappingService {

    @Autowired
    private DataserviceParameterMappingMapper dataserviceParameterMappingMapper;

    @Override
    public List<DataserviceParameterMappingDTO> list(Long parameterMapId) {
        LambdaQueryWrapper<DataserviceParameterMapping> queryWrapper = Wrappers.lambdaQuery(DataserviceParameterMapping.class)
                .eq(DataserviceParameterMapping::getParameterMapId, parameterMapId);
        List<DataserviceParameterMapping> dataserviceParameterMappings = dataserviceParameterMappingMapper.selectList(queryWrapper);
        return DataserviceParameterMappingConvert.INSTANCE.toDto(dataserviceParameterMappings);
    }

    @Override
    public DataserviceParameterMappingDTO selectOne(Long id) {
        DataserviceParameterMapping record = dataserviceParameterMappingMapper.selectById(id);
        checkState(record != null, () -> "data service parameter mapping not exists for id: " + id);
        return DataserviceParameterMappingConvert.INSTANCE.toDto(record);
    }

    @Override
    public int insert(DataserviceParameterMappingAddParam param) {
        DataserviceParameterMapping record = BeanUtil.copy(param, new DataserviceParameterMapping());
        return dataserviceParameterMappingMapper.insert(record);
    }

    @Override
    public int update(DataserviceParameterMappingUpdateParam param) {
        DataserviceParameterMapping record = BeanUtil.copy(param, new DataserviceParameterMapping());
        return dataserviceParameterMappingMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return dataserviceParameterMappingMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return dataserviceParameterMappingMapper.deleteBatchIds(ids);
    }
}
