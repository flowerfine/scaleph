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

package cn.sliew.scaleph.workflow.listener.workflowinstance;

import cn.sliew.scaleph.workflow.service.WorkflowInstanceService;
import cn.sliew.scaleph.workflow.service.WorkflowTaskDefinitionService;
import cn.sliew.scaleph.workflow.service.WorkflowTaskInstanceService;
import cn.sliew.scaleph.workflow.service.dto.WorkflowDefinitionDTO;
import cn.sliew.scaleph.workflow.service.dto.WorkflowInstanceDTO;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskDefinitionDTO;
import cn.sliew.scaleph.workflow.statemachine.WorkflowInstanceStateMachine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class WorkflowInstanceDeployEventListener implements WorkflowInstanceEventListener {

    @Autowired
    private WorkflowInstanceService workflowInstanceService;
    @Autowired
    private WorkflowTaskDefinitionService workflowTaskDefinitionService;
    @Autowired
    private WorkflowTaskInstanceService workflowTaskInstanceService;
    @Autowired
    private WorkflowInstanceStateMachine stateMachine;

    @Override
    public void onEvent(WorkflowInstanceEventDTO event) {
        WorkflowInstanceDTO workflowInstanceDTO = event.getWorkflowInstanceDTO();
        WorkflowDefinitionDTO workflowDefinitionDTO = workflowInstanceDTO.getWorkflowDefinition();
        try {
            // fixme 获取所有 task 的执行结果，执行成功，则发送执行成功事件，否则发送执行失败事件
            doDeploy(workflowDefinitionDTO);
            onSuccess(workflowInstanceDTO.getId());
        } catch (Exception e) {
            onFailure(workflowInstanceDTO.getId(), e);
        }
    }

    private void doDeploy(WorkflowDefinitionDTO workflowDefinitionDTO) {
        List<WorkflowTaskDefinitionDTO> workflowTaskDefinitionDTOS = workflowTaskDefinitionService.list(workflowDefinitionDTO.getId());
        for (WorkflowTaskDefinitionDTO workflowTaskDefinitionDTO : workflowTaskDefinitionDTOS) {
            workflowTaskInstanceService.deploy(workflowTaskDefinitionDTO.getId());
        }
    }

    private void onFailure(Long workflowInstanceId, Exception e) {
        stateMachine.onFailure(workflowInstanceService.get(workflowInstanceId), e);
    }

    private void onSuccess(Long workflowInstanceId) {
        stateMachine.onSuccess(workflowInstanceService.get(workflowInstanceId), e);
    }
}
