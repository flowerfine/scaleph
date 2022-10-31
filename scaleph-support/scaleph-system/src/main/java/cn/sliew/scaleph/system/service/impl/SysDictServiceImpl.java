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

import cn.sliew.scaleph.cache.CachingConfig;
import cn.sliew.scaleph.common.dict.DictInstance;
import cn.sliew.scaleph.common.dict.DictType;
import cn.sliew.scaleph.dao.entity.master.system.SysDict;
import cn.sliew.scaleph.dao.mapper.master.system.SysDictMapper;
import cn.sliew.scaleph.system.cache.DictCache;
import cn.sliew.scaleph.system.service.SysDictService;
import cn.sliew.scaleph.system.service.convert.SysDictConvert;
import cn.sliew.scaleph.system.service.dto.SysDictDTO;
import cn.sliew.scaleph.system.service.param.SysDictParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据字典表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-07-24
 */
@Service
@CacheConfig(cacheNames = CachingConfig.UnBoundedCaches.CACHE_DICT, cacheManager = "unBoundedCacheManager")
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;

    @Override
    public int insert(SysDictDTO sysDictDTO) {
        SysDict sysDict = SysDictConvert.INSTANCE.toDo(sysDictDTO);
        int result = this.sysDictMapper.insert(sysDict);
        DictCache.updateCache(sysDictDTO);
        return result;
    }

    @Override
    public int update(SysDictDTO sysDictDTO) {
        SysDict sysDict = SysDictConvert.INSTANCE.toDo(sysDictDTO);
        int result = this.sysDictMapper.updateById(sysDict);
        DictCache.updateCache(sysDictDTO);
        return result;
    }

    @Override
    public int deleteById(Long id) {
        SysDict sysDict = this.sysDictMapper.selectById(id);
        int result = this.sysDictMapper.deleteById(id);
        DictCache.evictCache(sysDict.getKey());
        return result;
    }

    @Override
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        List<SysDict> list = this.sysDictMapper.selectBatchIds(map.values());
        int result = this.sysDictMapper.deleteBatchIds(map.values());
        for (SysDict sysDict : list) {
            DictCache.evictCache(sysDict.getKey());
        }
        return result;
    }

    @Override
    public int deleteByType(String dictCodeType) {
        int result = this.sysDictMapper.delete(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getDictTypeCode, dictCodeType));
        DictCache.evictCacheByType(dictCodeType);
        return result;
    }

    @Override
    public SysDictDTO selectOne(Long id) {
        SysDict sysDict = this.sysDictMapper.selectById(id);
        SysDictDTO dto = SysDictConvert.INSTANCE.toDto(sysDict);
        DictCache.updateCache(dto);
        return dto;
    }

    @Override
    public List<SysDictDTO> selectByType(String dictTypeCode) {
        List<SysDict> list = this.sysDictMapper.selectList(new QueryWrapper<SysDict>()
                .lambda()
                .eq(SysDict::getDictTypeCode, dictTypeCode)
                .orderByAsc(SysDict::getDictCode));
        List<SysDictDTO> dtoList = SysDictConvert.INSTANCE.toDto(list);
        DictCache.updateCache(dtoList);
        return dtoList;
    }

    @Override
    public List<DictInstance> selectByType2(DictType type) {
        return EnumUtils.getEnumList(type.getInstanceClass());
    }

    @Override
    public List<SysDictDTO> selectAll() {
        List<SysDict> list = this.sysDictMapper.selectList(null);
        return SysDictConvert.INSTANCE.toDto(list);
    }

    @Override
    public Page<SysDictDTO> listByPage(SysDictParam param) {
        Page<SysDictDTO> result = new Page<>();
        Page<SysDict> list = this.sysDictMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                new LambdaQueryWrapper<SysDict>()
                        .like(StringUtils.hasText(param.getDictTypeCode()), SysDict::getDictTypeCode,
                                param.getDictTypeCode())
                        .like(StringUtils.hasText(param.getDictCode()), SysDict::getDictCode,
                                param.getDictCode())
                        .like(StringUtils.hasText(param.getDictValue()), SysDict::getDictValue,
                                param.getDictValue())
                        .eq(StringUtils.hasText(param.getIsValid()), SysDict::getIsValid,
                                param.getIsValid())
        );
        List<SysDictDTO> dtoList = SysDictConvert.INSTANCE.toDto(list.getRecords());
        DictCache.updateCache(dtoList);
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }
}
