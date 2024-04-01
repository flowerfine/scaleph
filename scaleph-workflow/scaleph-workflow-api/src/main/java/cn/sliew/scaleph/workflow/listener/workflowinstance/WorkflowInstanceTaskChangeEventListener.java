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

import cn.sliew.scaleph.common.dict.workflow.WorkflowInstanceState;
import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskInstanceStage;
import cn.sliew.scaleph.workflow.service.WorkflowTaskDefinitionService;
import cn.sliew.scaleph.workflow.service.WorkflowTaskInstanceService;
import cn.sliew.scaleph.workflow.service.dto.WorkflowInstanceDTO;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskDefinitionDTO;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskInstanceDTO;
import com.google.common.graph.Graph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WorkflowInstanceTaskChangeEventListener extends AbstractWorkflowInstanceEventListener {

    @Autowired
    private WorkflowTaskDefinitionService workflowTaskDefinitionService;
    @Autowired
    private WorkflowTaskInstanceService workflowTaskInstanceService;

    @Override
    protected CompletableFuture handleEventAsync(WorkflowInstanceEventDTO event) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(new TaskChangeRunner(event.getWorkflowInstanceId()));
        future.whenComplete(((unused, throwable) -> {
            if (throwable != null) {
                onFailure(event.getWorkflowInstanceId(), throwable);
            }
        }));
        return future;
    }

    private class TaskChangeRunner implements Runnable, Serializable {

        private Long workflowInstanceId;

        public TaskChangeRunner(Long workflowInstanceId) {
            this.workflowInstanceId = workflowInstanceId;
        }

        @Override
        public void run() {

            WorkflowInstanceDTO workflowInstanceDTO = workflowInstanceService.get(workflowInstanceId);
            if (workflowInstanceDTO.getState() == WorkflowInstanceState.FAILURE) {
                return;
            }
            Graph<WorkflowTaskDefinitionDTO> dag = workflowTaskDefinitionService.getDag(workflowInstanceDTO.getWorkflowDefinition().getId());
            Map<Long, WorkflowTaskDefinitionDTO> dagNodeMap = toDagNodeMap(dag);

            List<WorkflowTaskInstanceDTO> workflowTaskInstanceDTOS = workflowTaskInstanceService.list(workflowInstanceId);
            Map<Long, WorkflowTaskInstanceDTO> workflowTaskDefinitionMap = toWorkflowTaskDefinitionMap(workflowTaskInstanceDTOS);

            // 检测所有任务的状态，如果有一个失败，则失败
            boolean isAnyFailure = false;
            String anyFailureMessage = null;
            for (WorkflowTaskInstanceDTO workflowTaskInstanceDTO : workflowTaskInstanceDTOS) {
                if (workflowTaskInstanceDTO.getStage() == WorkflowTaskInstanceStage.FAILURE) {
                    isAnyFailure = true;
                    anyFailureMessage = workflowTaskInstanceDTO.getMessage();
                    break;
                }
            }
            if (isAnyFailure) {
                onFailure(workflowInstanceId, new Exception(anyFailureMessage));
                return;
            }

            // 检测所有任务，如果都执行成功，则成功。在检测过程中，尝试启动所有后置节点
            int successTaskCount = 0;
            for (WorkflowTaskInstanceDTO workflowTaskInstanceDTO : workflowTaskInstanceDTOS) {
                if (workflowTaskInstanceDTO.getStage() == WorkflowTaskInstanceStage.SUCCESS) {
                    successTaskCount++;
                    // 如何前置节点都成功了，则尝试启动后继节点
                    WorkflowTaskDefinitionDTO node = dagNodeMap.get(workflowTaskInstanceDTO.getWorkflowTaskDefinition().getId());
                    Set<WorkflowTaskDefinitionDTO> successors = dag.successors(node);
                    successors.stream().forEach(workflowTaskDefinitionDTO -> tryDeploySuccessor(dag, workflowTaskDefinitionMap, workflowTaskDefinitionDTO));
                }
            }

            if (successTaskCount == dag.nodes().size()) {
                stateMachine.onSuccess(workflowInstanceService.get(workflowInstanceId));
            }
        }

        private Map<Long, WorkflowTaskInstanceDTO> toWorkflowTaskDefinitionMap(List<WorkflowTaskInstanceDTO> workflowTaskInstanceDTOS) {
            return workflowTaskInstanceDTOS.stream()
                    .collect(Collectors.toMap(
                            workflowTaskInstanceDTO -> workflowTaskInstanceDTO.getWorkflowTaskDefinition().getId(),
                            Function.identity()
                    ));
        }

        private Map<Long, WorkflowTaskDefinitionDTO> toDagNodeMap(Graph<WorkflowTaskDefinitionDTO> dag) {
            return dag.nodes().stream()
                    .collect(Collectors.toMap(
                            workflowTaskDefinitionDTO -> workflowTaskDefinitionDTO.getId(),
                            Function.identity()
                    ));
        }

        private void tryDeploySuccessor(Graph<WorkflowTaskDefinitionDTO> dag, Map<Long, WorkflowTaskInstanceDTO> workflowTaskDefinitionMap, WorkflowTaskDefinitionDTO workflowTaskDefinitionDTO) {
            // 已经执行过
            if (workflowTaskDefinitionMap.containsKey(workflowTaskDefinitionDTO.getId())) {
                return;
            }
            // 判断前驱节点是否全部成功
            Set<WorkflowTaskDefinitionDTO> predecessors = dag.predecessors(workflowTaskDefinitionDTO);
            for (WorkflowTaskDefinitionDTO predecessor : predecessors) {
                // 前驱节点未执行
                if (workflowTaskDefinitionMap.containsKey(predecessor.getId()) == false) {
                    return;
                }
                // 前驱节点未成功
                WorkflowTaskInstanceDTO predecessorInstance = workflowTaskDefinitionMap.get(predecessor.getId());
                if (predecessorInstance.getStage() != WorkflowTaskInstanceStage.SUCCESS) {
                    return;
                }
            }
            workflowTaskInstanceService.deploy(workflowTaskDefinitionDTO.getId(), workflowInstanceId);
        }
    }
}
