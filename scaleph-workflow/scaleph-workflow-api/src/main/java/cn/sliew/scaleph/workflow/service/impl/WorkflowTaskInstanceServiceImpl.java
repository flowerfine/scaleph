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

import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowTaskInstance;
import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowTaskInstanceVO;
import cn.sliew.scaleph.dao.mapper.master.workflow.WorkflowTaskInstanceMapper;
import cn.sliew.scaleph.workflow.service.WorkflowTaskInstanceService;
import cn.sliew.scaleph.workflow.service.convert.WorkflowTaskInstanceVOConvert;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskInstanceDTO;
import cn.sliew.scaleph.workflow.service.param.WorkflowTaskInstanceListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WorkflowTaskInstanceServiceImpl implements WorkflowTaskInstanceService {

    @Autowired
    private WorkflowTaskInstanceMapper workflowTaskInstanceMapper;

    @Override
    public Page<WorkflowTaskInstanceDTO> list(WorkflowTaskInstanceListParam param) {
        Page<WorkflowTaskInstance> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<WorkflowTaskInstanceVO> taskInstanceVOPage = workflowTaskInstanceMapper.list(page, param.getWorkflowTaskDefinitionId());
        Page<WorkflowTaskInstanceDTO> result = new Page<>(taskInstanceVOPage.getCurrent(), taskInstanceVOPage.getSize(), taskInstanceVOPage.getTotal());
        List<WorkflowTaskInstanceDTO> workflowTaskInstanceDTOS = WorkflowTaskInstanceVOConvert.INSTANCE.toDto(taskInstanceVOPage.getRecords());
        result.setRecords(workflowTaskInstanceDTOS);
        return result;
    }

    @Override
    public WorkflowTaskInstanceDTO get(Long id) {
        WorkflowTaskInstanceVO vo = workflowTaskInstanceMapper.get(id);
        checkState(vo != null, () -> "workflow task instance not exists for id: " + id);
        return WorkflowTaskInstanceVOConvert.INSTANCE.toDto(vo);
    }
}
