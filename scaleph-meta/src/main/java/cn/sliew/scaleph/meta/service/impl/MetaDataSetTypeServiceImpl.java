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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.meta.MetaDataSet;
import cn.sliew.scaleph.dao.entity.master.meta.MetaDataSetType;
import cn.sliew.scaleph.dao.mapper.master.meta.MetaDataSetMapper;
import cn.sliew.scaleph.dao.mapper.master.meta.MetaDataSetTypeMapper;
import cn.sliew.scaleph.meta.service.MetaDataSetTypeService;
import cn.sliew.scaleph.meta.service.convert.MetaDataSetTypeConvert;
import cn.sliew.scaleph.meta.service.dto.MetaDataSetTypeDTO;
import cn.sliew.scaleph.meta.service.param.MetaDataSetTypeParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MetaDataSetTypeServiceImpl implements MetaDataSetTypeService {

    @Autowired
    private MetaDataSetTypeMapper metaDataSetTypeMapper;
    @Autowired
    private MetaDataSetMapper metaDataSetMapper;

    @Override
    public int insert(MetaDataSetTypeDTO metaDataSetTypeDTO) {
        MetaDataSetType type = MetaDataSetTypeConvert.INSTANCE.toDo(metaDataSetTypeDTO);
        return this.metaDataSetTypeMapper.insert(type);
    }

    @Override
    public int update(MetaDataSetTypeDTO metaDataSetTypeDTO) {
        MetaDataSetType type = MetaDataSetTypeConvert.INSTANCE.toDo(metaDataSetTypeDTO);
        return this.metaDataSetTypeMapper.updateById(type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteById(Long id) {
        this.metaDataSetMapper.delete(
            new LambdaQueryWrapper<MetaDataSet>()
                .eq(MetaDataSet::getDataSetTypeId, id)
        );
        return this.metaDataSetTypeMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        this.metaDataSetMapper.delete(
            new LambdaQueryWrapper<MetaDataSet>()
                .in(MetaDataSet::getDataSetTypeId, map.values())
        );
        return this.metaDataSetTypeMapper.deleteBatchIds(map.values());
    }

    @Override
    public Page<MetaDataSetTypeDTO> listByPage(MetaDataSetTypeParam param) {
        Page<MetaDataSetTypeDTO> result = new Page<>();
        Page<MetaDataSetType> list = this.metaDataSetTypeMapper.selectPage(
            new Page<>(param.getCurrent(), param.getPageSize()),
            new LambdaQueryWrapper<MetaDataSetType>()
                .like(StrUtil.isNotEmpty(param.getDataSetTypeCode()),
                    MetaDataSetType::getDataSetTypeCode, param.getDataSetTypeCode())
                .like(StrUtil.isNotEmpty(param.getDataSetTypeName()),
                    MetaDataSetType::getDataSetTypeName, param.getDataSetTypeName())
        );
        List<MetaDataSetTypeDTO> dtoList = MetaDataSetTypeConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }
}
