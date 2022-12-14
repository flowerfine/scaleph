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

import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowDefinition;
import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowDefinitionVO;
import cn.sliew.scaleph.dao.mapper.master.workflow.WorkflowDefinitionMapper;
import cn.sliew.scaleph.workflow.service.WorkflowDefinitionService;
import cn.sliew.scaleph.workflow.service.convert.WorkflowDefinitionVOConvert;
import cn.sliew.scaleph.workflow.service.dto.WorkflowDefinitionDTO;
import cn.sliew.scaleph.workflow.service.param.WorkflowDefinitionListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WorkflowDefinitionServiceImpl implements WorkflowDefinitionService {

    @Autowired
    private WorkflowDefinitionMapper workflowDefinitionMapper;

    @Override
    public Page<WorkflowDefinitionDTO> list(WorkflowDefinitionListParam param) {
        Page<WorkflowDefinition> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<WorkflowDefinitionVO> workflowDefinitionPage = workflowDefinitionMapper.list(page, param.getType(), param.getName());
        Page<WorkflowDefinitionDTO> result = new Page<>(workflowDefinitionPage.getCurrent(), workflowDefinitionPage.getSize(), workflowDefinitionPage.getTotal());
        List<WorkflowDefinitionDTO> workflowDefinitionDTOS = WorkflowDefinitionVOConvert.INSTANCE.toDto(workflowDefinitionPage.getRecords());
        result.setRecords(workflowDefinitionDTOS);
        return result;
    }

    @Override
    public WorkflowDefinitionDTO get(Long id) {
        WorkflowDefinitionVO record = workflowDefinitionMapper.get(id);
        checkState(record != null, () -> "workflow definition not exists for id: " + id);
        return WorkflowDefinitionVOConvert.INSTANCE.toDto(record);
    }
}
