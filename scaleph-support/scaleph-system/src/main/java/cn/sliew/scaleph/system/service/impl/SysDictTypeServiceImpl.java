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

import cn.sliew.scaleph.common.dict.DictType;
import cn.sliew.scaleph.dao.mapper.master.system.SysDictTypeMapper;
import cn.sliew.scaleph.system.service.SysDictTypeService;
import cn.sliew.scaleph.system.service.param.SysDictTypeParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public Page<DictType> listByPage(SysDictTypeParam param) {
        List<DictType> dictTypes = selectAll();
        Page<DictType> result = new Page<>(dictTypes.size(), param.getCurrent(), param.getPageSize());

        List<DictType> filteredDictTypes = dictTypes.stream().filter(dictType -> {
            if (StringUtils.hasText(param.getCode())) {
                return dictType.getCode().contains(param.getCode());
            }
            return true;
        }).filter(dictType -> {
            if (StringUtils.hasText(param.getName())) {
                return dictType.getName().contains(param.getName());
            }
            return true;
        }).collect(Collectors.toList());

        Long from = (param.getCurrent() - 1) * param.getPageSize();
        Long to = from + param.getPageSize();
        if (from >= filteredDictTypes.size()) {
            result.setRecords(Collections.emptyList());
            return result;
        }
        result.setRecords(filteredDictTypes.subList(from.intValue(), to.intValue()));
        return result;
    }

    @Override
    public List<DictType> selectAll() {
        return EnumUtils.getEnumList(DictType.class);
    }
}
