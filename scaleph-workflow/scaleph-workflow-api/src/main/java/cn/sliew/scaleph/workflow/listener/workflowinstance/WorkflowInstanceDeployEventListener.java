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

import cn.sliew.scaleph.workflow.service.WorkflowTaskDefinitionService;
import cn.sliew.scaleph.workflow.service.WorkflowTaskInstanceService;
import cn.sliew.scaleph.workflow.service.dto.WorkflowDefinitionDTO;
import cn.sliew.scaleph.workflow.service.dto.WorkflowInstanceDTO;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskDefinitionDTO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.annotation.RInject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class WorkflowInstanceDeployEventListener extends AbstractWorkflowInstanceEventListener {

    @Autowired
    private WorkflowTaskDefinitionService workflowTaskDefinitionService;

    @Override
    protected CompletableFuture handleEventAsync(Long workflowInstanceId) {
        WorkflowInstanceDTO workflowInstanceDTO = workflowInstanceService.get(workflowInstanceId);
        WorkflowDefinitionDTO workflowDefinitionDTO = workflowInstanceDTO.getWorkflowDefinition();
        return doDeploy(workflowDefinitionDTO);
    }

    private CompletableFuture doDeploy(WorkflowDefinitionDTO workflowDefinitionDTO) {
        List<WorkflowTaskDefinitionDTO> workflowTaskDefinitionDTOS = workflowTaskDefinitionService.list(workflowDefinitionDTO.getId());
        // fixme 应该是找到 root 节点，批量启动 root 节点
        List<CompletableFuture> futures = new ArrayList<>(workflowTaskDefinitionDTOS.size());
        for (WorkflowTaskDefinitionDTO workflowTaskDefinitionDTO : workflowTaskDefinitionDTOS) {
            CompletableFuture future = (CompletableFuture) executorService.submit(new DeployRunner(workflowTaskDefinitionDTO.getId()));
            futures.add(future);
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
    }

    /**
     * 必须实现 Serializable 接口，无法使用 lambda
     */
    public static class DeployRunner implements Runnable, Serializable {

        private Long workflowTaskDefinitionId;

        @RInject
        private String taskId;
        @Autowired
        private WorkflowTaskInstanceService workflowTaskInstanceService;

        public DeployRunner(Long workflowTaskDefinitionId) {
            this.workflowTaskDefinitionId = workflowTaskDefinitionId;
        }

        @Override
        public void run() {
            workflowTaskInstanceService.deploy(workflowTaskDefinitionId);
        }
    }
}
