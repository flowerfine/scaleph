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

import cn.sliew.scaleph.application.oam.model.definition.PolicyDefinition;
import cn.sliew.scaleph.application.oam.service.OamPolicyDefinitionService;
import cn.sliew.scaleph.application.oam.service.convert.OamPolicyDefinitionConvert;
import cn.sliew.scaleph.dao.entity.master.oam.OamPolicyDefinition;
import cn.sliew.scaleph.dao.mapper.master.oam.OamPolicyDefinitionMapper;
import cn.sliew.scaleph.system.model.PaginationParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OamPolicyDefinitionServiceImpl implements OamPolicyDefinitionService {

    @Autowired
    private OamPolicyDefinitionMapper oamPolicyDefinitionMapper;

    @Override
    public Page<PolicyDefinition> listByPage(PaginationParam param) {
        Page<OamPolicyDefinition> list = oamPolicyDefinitionMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(OamPolicyDefinition.class).orderByAsc(OamPolicyDefinition::getId)
        );
        Page<PolicyDefinition> result = new Page<>(list.getCurrent(), list.getSize(), list.getTotal());
        result.setRecords(OamPolicyDefinitionConvert.INSTANCE.toDto(list.getRecords()));
        return result;
    }

    @Override
    public List<PolicyDefinition> listAll() {
        LambdaQueryWrapper<OamPolicyDefinition> queryWrapper = Wrappers.lambdaQuery(OamPolicyDefinition.class)
                .orderByAsc(OamPolicyDefinition::getId);
        List<OamPolicyDefinition> oamPolicyDefinitions = oamPolicyDefinitionMapper.selectList(queryWrapper);
        return OamPolicyDefinitionConvert.INSTANCE.toDto(oamPolicyDefinitions);
    }

    @Override
    public PolicyDefinition selectOne(Long id) {
        OamPolicyDefinition record = oamPolicyDefinitionMapper.selectById(id);
        return OamPolicyDefinitionConvert.INSTANCE.toDto(record);
    }
}
