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
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskInstanceDTO;
import cn.sliew.scaleph.workflow.statemachine.WorkflowInstanceStateMachine;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RExecutorFuture;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void onEvent(WorkflowInstanceEventDTO event) {
        WorkflowInstanceDTO workflowInstanceDTO = event.getWorkflowInstanceDTO();
        WorkflowDefinitionDTO workflowDefinitionDTO = workflowInstanceDTO.getWorkflowDefinition();
        try {
            doDeploy(workflowInstanceDTO, workflowDefinitionDTO);
        } catch (Exception e) {
            onFailure(workflowInstanceDTO.getId(), e);
        }
    }

    private void doDeploy(WorkflowInstanceDTO workflowInstanceDTO, WorkflowDefinitionDTO workflowDefinitionDTO) {
        RScheduledExecutorService executorService = redissonClient.getExecutorService("WorkflowTaskInstanceDeploy");
        List<WorkflowTaskDefinitionDTO> workflowTaskDefinitionDTOS = workflowTaskDefinitionService.list(workflowDefinitionDTO.getId());
        // fixme 应该是找到 root 节点，批量启动 root 节点
        List<RExecutorFuture<WorkflowTaskInstanceDTO>> futures = new ArrayList<>(workflowTaskDefinitionDTOS.size());
        for (WorkflowTaskDefinitionDTO workflowTaskDefinitionDTO : workflowTaskDefinitionDTOS) {
            RExecutorFuture<WorkflowTaskInstanceDTO> future = executorService.submit(() -> workflowTaskInstanceService.deploy(workflowTaskDefinitionDTO.getId()));
            futures.add(future);
        }
        CompletableFuture<Void> allFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        allFuture.whenComplete(((unused, throwable) -> {
            if (throwable != null) {
                onFailure(workflowInstanceDTO.getId(), throwable);
            }
        }));
    }

    private void onFailure(Long workflowInstanceId, Throwable throwable) {
        stateMachine.onFailure(workflowInstanceService.get(workflowInstanceId), throwable);
    }
}
