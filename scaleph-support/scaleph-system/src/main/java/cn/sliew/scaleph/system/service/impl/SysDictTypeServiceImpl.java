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

package cn.sliew.scaleph.system.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.system.SysDictType;
import cn.sliew.scaleph.dao.mapper.master.system.SysDictTypeMapper;
import cn.sliew.scaleph.system.cache.DictTypeCache;
import cn.sliew.scaleph.system.service.SysDictService;
import cn.sliew.scaleph.system.service.SysDictTypeService;
import cn.sliew.scaleph.system.service.convert.SysDictTypeConvert;
import cn.sliew.scaleph.system.service.dto.SysDictTypeDTO;
import cn.sliew.scaleph.system.service.param.SysDictTypeParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 数据字典类型 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-07-24
 */
@Service
public class SysDictTypeServiceImpl implements SysDictTypeService {

    @Autowired
    private SysDictTypeMapper sysDictTypeMapper;
    @Autowired
    private SysDictService sysDictService;

    @Override
    public int insert(SysDictTypeDTO sysDictTypeDTO) {
        SysDictType sysDictType = SysDictTypeConvert.INSTANCE.toDo(sysDictTypeDTO);
        int result = this.sysDictTypeMapper.insert(sysDictType);
        DictTypeCache.updateCache(sysDictTypeDTO);
        return result;
    }

    @Override
    public int update(SysDictTypeDTO sysDictTypeDTO) {
        SysDictType sysDictType = SysDictTypeConvert.INSTANCE.toDo(sysDictTypeDTO);
        int result = this.sysDictTypeMapper.updateById(sysDictType);
        DictTypeCache.updateCache(sysDictTypeDTO);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteById(Long id) {
        SysDictType sysDictType = this.sysDictTypeMapper.selectById(id);
        int result = this.sysDictTypeMapper.deleteById(id);
        this.sysDictService.deleteByType(sysDictType.getDictTypeCode());
        DictTypeCache.evictCache(sysDictType.getDictTypeCode());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        List<SysDictType> list = this.sysDictTypeMapper.selectBatchIds(map.values());
        int result = this.sysDictTypeMapper.deleteBatchIds(map.values());
        for (SysDictType sysDictType : list) {
            this.sysDictService.deleteByType(sysDictType.getDictTypeCode());
            DictTypeCache.evictCache(sysDictType.getDictTypeCode());
        }
        return result;
    }

    @Override
    public SysDictTypeDTO selectOne(Long id) {
        SysDictType sysDictType = this.sysDictTypeMapper.selectById(id);
        SysDictTypeDTO dto = SysDictTypeConvert.INSTANCE.toDto(sysDictType);
        DictTypeCache.updateCache(dto);
        return dto;
    }

    @Override
    public SysDictTypeDTO selectOne(String dictTypeCode) {
        SysDictType sysDictType = this.sysDictTypeMapper.selectByDictTypeCode(dictTypeCode);
        SysDictTypeDTO dto = SysDictTypeConvert.INSTANCE.toDto(sysDictType);
        DictTypeCache.updateCache(dto);
        return dto;
    }

    @Override
    public Page<SysDictTypeDTO> listByPage(SysDictTypeParam param) {
        Page<SysDictTypeDTO> result = new Page<>();
        Page<SysDictType> list = this.sysDictTypeMapper.selectPage(
            new Page<>(param.getCurrent(), param.getPageSize()),
            new QueryWrapper<SysDictType>()
                .lambda()
                .like(StrUtil.isNotEmpty(param.getDictTypeCode()), SysDictType::getDictTypeCode,
                    param.getDictTypeCode())
                .like(StrUtil.isNotEmpty(param.getDictTypeName()), SysDictType::getDictTypeName,
                    param.getDictTypeName())
        );
        List<SysDictTypeDTO> dtoList = SysDictTypeConvert.INSTANCE.toDto(list.getRecords());
        DictTypeCache.updateCache(dtoList);
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public List<SysDictTypeDTO> selectAll() {
        List<SysDictType> sysDictTypeList = this.sysDictTypeMapper.selectList(null);
        return SysDictTypeConvert.INSTANCE.toDto(sysDictTypeList);
    }
}
