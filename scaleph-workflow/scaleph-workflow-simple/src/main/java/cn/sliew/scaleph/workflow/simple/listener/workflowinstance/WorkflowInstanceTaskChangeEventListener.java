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

import cn.sliew.carp.framework.dag.service.DagConfigComplexService;
import cn.sliew.carp.framework.dag.service.dto.DagConfigStepDTO;
import cn.sliew.carp.framework.dag.service.dto.DagInstanceDTO;
import cn.sliew.carp.framework.dag.service.dto.DagStepDTO;
import cn.sliew.scaleph.common.dict.workflow.WorkflowInstanceState;
import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskInstanceStage;
import cn.sliew.scaleph.queue.MessageListener;
import cn.sliew.scaleph.workflow.simple.statemachine.WorkflowInstanceStateMachine;
import cn.sliew.scaleph.workflow.simple.statemachine.WorkflowTaskInstanceStateMachine;
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
    private DagConfigComplexService dagConfigComplexService;
    @Autowired
    private WorkflowTaskInstanceStateMachine taskInstanceStateMachine;

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
            DagInstanceDTO dagInstanceDTO = dagInstanceComplexService.selectSimpleOne(workflowInstanceId);
            if (WorkflowInstanceState.FAILURE.getValue().equals(dagInstanceDTO.getStatus())) {
                return;
            }

            Graph<DagConfigStepDTO> dag = dagConfigComplexService.getDag(dagInstanceDTO.getDagConfig().getId());
            Graph<DagStepDTO> stepDag = dagInstanceComplexService.getDag(workflowInstanceId, dag);

            // 检测所有任务的状态，如果有一个失败，则失败
            boolean isAnyFailure = false;
            String anyFailureMessage = null;
            for (DagStepDTO dagStepDTO : stepDag.nodes()) {
                if (WorkflowTaskInstanceStage.FAILURE.getValue().equals(dagStepDTO.getStatus())) {
                    isAnyFailure = true;
//                    anyFailureMessage = dagStepDTO.getMessage();
                    break;
                }
            }
            if (isAnyFailure) {
                onFailure(workflowInstanceId, new Exception(anyFailureMessage));
                return;
            }

            // 检测所有任务，如果都执行成功，则成功。在检测过程中，尝试启动所有后置节点
            int successTaskCount = 0;
            for (DagStepDTO dagStepDTO : stepDag.nodes()) {
                if (WorkflowTaskInstanceStage.SUCCESS.getValue().equals(dagStepDTO.getStatus())) {
                    successTaskCount++;
                    // 如果节点成功，尝试启动后继节点
                    Set<DagStepDTO> successors = stepDag.successors(dagStepDTO);
                    successors.stream().forEach(successor -> tryDeploySuccessor(stepDag, successor));
                }
            }

            if (successTaskCount == dag.nodes().size()) {
                stateMachine.onSuccess(dagInstanceComplexService.selectSimpleOne(workflowInstanceId));
            }
        }

        private void tryDeploySuccessor(Graph<DagStepDTO> stepDag, DagStepDTO dagStepDTO) {
            // 已经执行过
            if (dagStepDTO.getStatus() != null && WorkflowTaskInstanceStage.PENDING.getValue().equals(dagStepDTO.getStatus()) == false) {
                return;
            }
            // 判断前驱节点是否全部成功
            Set<DagStepDTO> predecessors = stepDag.predecessors(dagStepDTO);
            for (DagStepDTO predecessor : predecessors) {
                // 前驱节点未执行
                if (WorkflowTaskInstanceStage.PENDING.getValue().equals(predecessor.getStatus())) {
                    return;
                }
                // 前驱节点未成功
                if (WorkflowTaskInstanceStage.SUCCESS.getValue().equals(predecessor.getStatus()) == false) {
                    return;
                }
            }
            taskInstanceStateMachine.deploy(dagStepDTO);
        }
    }
}
