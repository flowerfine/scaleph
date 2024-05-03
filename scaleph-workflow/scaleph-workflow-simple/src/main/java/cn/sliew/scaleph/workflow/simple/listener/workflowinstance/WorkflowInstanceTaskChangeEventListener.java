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

package cn.sliew.scaleph.workflow.simple.listener.workflowinstance;

import cn.sliew.scaleph.common.dict.workflow.WorkflowInstanceState;
import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskInstanceStage;
import cn.sliew.scaleph.queue.MessageListener;
import cn.sliew.scaleph.workflow.service.WorkflowDefinitionService;
import cn.sliew.scaleph.workflow.service.WorkflowTaskInstanceService;
import cn.sliew.scaleph.workflow.service.dto.WorkflowInstanceDTO;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskDefinitionDTO;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskInstanceDTO;
import cn.sliew.scaleph.workflow.simple.statemachine.WorkflowInstanceStateMachine;
import com.google.common.graph.Graph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
@MessageListener(topic = WorkflowInstanceTaskChangeEventListener.TOPIC, consumerGroup = WorkflowInstanceStateMachine.CONSUMER_GROUP)
public class WorkflowInstanceTaskChangeEventListener extends AbstractWorkflowInstanceEventListener {

    public static final String TOPIC = "TOPIC_WORKFLOW_INSTANCE_PROCESS_TASK_CHANGE";

    @Autowired
    private WorkflowDefinitionService workflowDefinitionService;
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

            Graph<WorkflowTaskDefinitionDTO> dag = workflowDefinitionService.getDag(workflowInstanceDTO.getWorkflowDefinition().getId());
            Graph<WorkflowTaskInstanceDTO> workflowTaskInstanceGraph = workflowTaskInstanceService.getDag(workflowInstanceId, dag);

            // 检测所有任务的状态，如果有一个失败，则失败
            boolean isAnyFailure = false;
            String anyFailureMessage = null;
            for (WorkflowTaskInstanceDTO workflowTaskInstanceDTO : workflowTaskInstanceGraph.nodes()) {
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
            for (WorkflowTaskInstanceDTO workflowTaskInstanceDTO : workflowTaskInstanceGraph.nodes()) {
                if (workflowTaskInstanceDTO.getStage() == WorkflowTaskInstanceStage.SUCCESS) {
                    successTaskCount++;
                    // 如果节点成功，尝试启动后继节点
                    Set<WorkflowTaskInstanceDTO> successors = workflowTaskInstanceGraph.successors(workflowTaskInstanceDTO);
                    successors.stream().forEach(taskInstanceDTO -> tryDeploySuccessor(workflowTaskInstanceGraph, taskInstanceDTO));
                }
            }

            if (successTaskCount == dag.nodes().size()) {
                stateMachine.onSuccess(workflowInstanceService.get(workflowInstanceId));
            }
        }

        private void tryDeploySuccessor(Graph<WorkflowTaskInstanceDTO> workflowTaskInstanceGraph, WorkflowTaskInstanceDTO workflowTaskInstanceDTO) {
            // 已经执行过
            if (workflowTaskInstanceDTO.getStage() != WorkflowTaskInstanceStage.PENDING) {
                return;
            }
            // 判断前驱节点是否全部成功
            Set<WorkflowTaskInstanceDTO> predecessors = workflowTaskInstanceGraph.predecessors(workflowTaskInstanceDTO);
            for (WorkflowTaskInstanceDTO predecessor : predecessors) {
                // 前驱节点未执行
                if (predecessor.getStage() == WorkflowTaskInstanceStage.PENDING) {
                    return;
                }
                // 前驱节点未成功
                if (predecessor.getStage() != WorkflowTaskInstanceStage.SUCCESS) {
                    return;
                }
            }
            workflowTaskInstanceService.deploy(workflowTaskInstanceDTO.getId());
        }
    }
}
