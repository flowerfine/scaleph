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

package cn.sliew.scaleph.application.oam.service.impl;

import cn.sliew.scaleph.application.oam.model.definition.WorkloadDefinition;
import cn.sliew.scaleph.application.oam.service.OamWorkloadDefinitionService;
import cn.sliew.scaleph.application.oam.service.convert.OamWorkloadDefinitionConvert;
import cn.sliew.scaleph.dao.entity.master.oam.OamWorkloadDefinition;
import cn.sliew.scaleph.dao.mapper.master.oam.OamWorkloadDefinitionMapper;
import cn.sliew.scaleph.system.model.PaginationParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OamWorkloadDefinitionServiceImpl implements OamWorkloadDefinitionService {

    @Autowired
    private OamWorkloadDefinitionMapper oamWorkloadDefinitionMapper;

    @Override
    public Page<WorkloadDefinition> listByPage(PaginationParam param) {
        Page<OamWorkloadDefinition> list = oamWorkloadDefinitionMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(OamWorkloadDefinition.class).orderByAsc(OamWorkloadDefinition::getId)
        );
        Page<WorkloadDefinition> result = new Page<>(list.getCurrent(), list.getSize(), list.getTotal());
        result.setRecords(OamWorkloadDefinitionConvert.INSTANCE.toDto(list.getRecords()));
        return result;
    }

    @Override
    public List<WorkloadDefinition> listAll() {
        LambdaQueryWrapper<OamWorkloadDefinition> queryWrapper = Wrappers.lambdaQuery(OamWorkloadDefinition.class)
                .orderByAsc(OamWorkloadDefinition::getId);
        List<OamWorkloadDefinition> oamWorkloadDefinitions = oamWorkloadDefinitionMapper.selectList(queryWrapper);
        return OamWorkloadDefinitionConvert.INSTANCE.toDto(oamWorkloadDefinitions);
    }

    @Override
    public WorkloadDefinition selectOne(Long id) {
        OamWorkloadDefinition record = oamWorkloadDefinitionMapper.selectById(id);
        return OamWorkloadDefinitionConvert.INSTANCE.toDto(record);
    }
}
