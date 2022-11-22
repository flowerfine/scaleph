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

import cn.sliew.scaleph.common.dict.workflow.WorkflowInstanceState;
import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowInstance;
import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowInstanceVO;
import cn.sliew.scaleph.dao.mapper.master.workflow.WorkflowInstanceMapper;
import cn.sliew.scaleph.workflow.service.WorkflowInstanceService;
import cn.sliew.scaleph.workflow.service.convert.WorkflowInstanceVOConvert;
import cn.sliew.scaleph.workflow.service.dto.WorkflowInstanceDTO;
import cn.sliew.scaleph.workflow.service.param.WorkflowInstanceListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WorkflowInstanceServiceImpl implements WorkflowInstanceService {

    @Autowired
    private WorkflowInstanceMapper workflowInstanceMapper;

    @Override
    public Page<WorkflowInstanceDTO> list(WorkflowInstanceListParam param) {
        Page<WorkflowInstance> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<WorkflowInstanceVO> workflowInstancePage = workflowInstanceMapper.list(page, param.getWorkflowDefinitionId(), param.getState());
        Page<WorkflowInstanceDTO> result = new Page<>(workflowInstancePage.getCurrent(), workflowInstancePage.getSize(), workflowInstancePage.getTotal());
        List<WorkflowInstanceDTO> workflowDefinitionDTOS = WorkflowInstanceVOConvert.INSTANCE.toDto(workflowInstancePage.getRecords());
        result.setRecords(workflowDefinitionDTOS);
        return result;
    }

    @Override
    public WorkflowInstanceDTO get(Long id) {
        WorkflowInstanceVO vo = workflowInstanceMapper.get(id);
        checkState(vo != null, () -> "workflow instance not exists for id: " + id);
        return WorkflowInstanceVOConvert.INSTANCE.toDto(vo);
    }

    @Override
    public WorkflowInstanceDTO start(Long workflowDefinitionId) {
        WorkflowInstance record = new WorkflowInstance();
        record.setWorkflowDefinitionId(workflowDefinitionId);
        record.setState(WorkflowInstanceState.PENDING);
        workflowInstanceMapper.insert(record);
        return get(record.getId());
    }

    @Override
    public void suspend(Long id) {

    }

    @Override
    public void terminate(Long id) {

    }
}
