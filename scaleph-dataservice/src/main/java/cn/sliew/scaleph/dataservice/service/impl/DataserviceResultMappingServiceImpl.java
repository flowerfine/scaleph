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
import cn.sliew.scaleph.dao.entity.master.dataservice.DataserviceResultMapping;
import cn.sliew.scaleph.dao.mapper.master.dataservice.DataserviceResultMappingMapper;
import cn.sliew.scaleph.dataservice.service.DataserviceResultMappingService;
import cn.sliew.scaleph.dataservice.service.convert.DataserviceResultMappingConvert;
import cn.sliew.scaleph.dataservice.service.dto.DataserviceResultMappingDTO;
import cn.sliew.scaleph.dataservice.service.param.DataserviceResultMappingAddParam;
import cn.sliew.scaleph.dataservice.service.param.DataserviceResultMappingUpdateParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class DataserviceResultMappingServiceImpl implements DataserviceResultMappingService {

    @Autowired
    private DataserviceResultMappingMapper dataserviceResultMappingMapper;

    @Override
    public List<DataserviceResultMappingDTO> list(Long resultMapId) {
        LambdaQueryWrapper<DataserviceResultMapping> queryWrapper = Wrappers.lambdaQuery(DataserviceResultMapping.class)
                .eq(DataserviceResultMapping::getResultMapId, resultMapId);
        List<DataserviceResultMapping> dataserviceResultMappings = dataserviceResultMappingMapper.selectList(queryWrapper);
        return DataserviceResultMappingConvert.INSTANCE.toDto(dataserviceResultMappings);
    }

    @Override
    public DataserviceResultMappingDTO selectOne(Long id) {
        DataserviceResultMapping record = dataserviceResultMappingMapper.selectById(id);
        checkState(record != null, () -> "data service result mapping not exists for id: " + id);
        return DataserviceResultMappingConvert.INSTANCE.toDto(record);
    }

    @Override
    public int insert(DataserviceResultMappingAddParam param) {
        DataserviceResultMapping record = BeanUtil.copy(param, new DataserviceResultMapping());
        return dataserviceResultMappingMapper.insert(record);
    }

    @Override
    public int update(DataserviceResultMappingUpdateParam param) {
        DataserviceResultMapping record = BeanUtil.copy(param, new DataserviceResultMapping());
        return dataserviceResultMappingMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return dataserviceResultMappingMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return dataserviceResultMappingMapper.deleteBatchIds(ids);
    }
}
