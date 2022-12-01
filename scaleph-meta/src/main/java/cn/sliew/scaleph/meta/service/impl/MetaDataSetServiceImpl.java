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

import cn.sliew.scaleph.dao.entity.master.meta.MetaDataSet;
import cn.sliew.scaleph.dao.mapper.master.meta.MetaDataSetMapper;
import cn.sliew.scaleph.meta.service.MetaDataSetService;
import cn.sliew.scaleph.meta.service.convert.MetaDataSetConvert;
import cn.sliew.scaleph.meta.service.dto.MetaDataSetDTO;
import cn.sliew.scaleph.meta.service.param.MetaDataSetParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaDataSetServiceImpl implements MetaDataSetService {

    @Autowired
    private MetaDataSetMapper metaDataSetMapper;

    @Override
    public int insert(MetaDataSetDTO metaDataSetDTO) {
        MetaDataSet set = MetaDataSetConvert.INSTANCE.toDo(metaDataSetDTO);
        return this.metaDataSetMapper.insert(set);
    }

    @Override
    public int update(MetaDataSetDTO metaDataSetDTO) {
        MetaDataSet set = MetaDataSetConvert.INSTANCE.toDo(metaDataSetDTO);
        return this.metaDataSetMapper.updateById(set);
    }

    @Override
    public int deleteById(Long id) {
        return this.metaDataSetMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return this.metaDataSetMapper.deleteBatchIds(ids);
    }

    @Override
    public Page<MetaDataSetDTO> listByPage(MetaDataSetParam param) {
        Page<MetaDataSetDTO> result = new Page<>();
        Page<MetaDataSet> list = this.metaDataSetMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                param.getDataSetTypeCode(),
                param.getDataSetCode(),
                param.getDataSetValue());
        List<MetaDataSetDTO> dtoList = MetaDataSetConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public List<MetaDataSetDTO> listByType(Long dataSetTypeId) {
        List<MetaDataSet> list = this.metaDataSetMapper.selectList(
                new LambdaQueryWrapper<MetaDataSet>()
                        .eq(MetaDataSet::getDataSetTypeId, dataSetTypeId)
        );
        return MetaDataSetConvert.INSTANCE.toDto(list);
    }
}
