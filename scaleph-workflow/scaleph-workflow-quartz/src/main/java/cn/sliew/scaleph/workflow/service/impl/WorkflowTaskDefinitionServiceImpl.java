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

package cn.sliew.scaleph.workflow.service.impl;

import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowTaskDefinition;
import cn.sliew.scaleph.dao.mapper.master.workflow.WorkflowTaskDefinitionMapper;
import cn.sliew.scaleph.workflow.service.WorkflowTaskDefinitionService;
import cn.sliew.scaleph.workflow.service.convert.WorkflowTaskDefinitionConvert;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskDefinitionDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowTaskDefinitionServiceImpl implements WorkflowTaskDefinitionService {

    @Autowired
    private WorkflowTaskDefinitionMapper workflowTaskDefinitionMapper;

    @Override
    public List<WorkflowTaskDefinitionDTO> list(Long workflowDefinitionId) {
        LambdaQueryWrapper<WorkflowTaskDefinition> queryWrapper = Wrappers.lambdaQuery(WorkflowTaskDefinition.class)
                .eq(WorkflowTaskDefinition::getWorkflowDefinitionId, workflowDefinitionId);
        List<WorkflowTaskDefinition> workflowTaskDefinitions = workflowTaskDefinitionMapper.selectList(queryWrapper);
        return WorkflowTaskDefinitionConvert.INSTANCE.toDto(workflowTaskDefinitions);
    }
    
}
