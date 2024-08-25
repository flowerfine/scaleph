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

package cn.sliew.scaleph.workflow.simple.listener.taskinstance;

import cn.sliew.carp.framework.dag.service.dto.DagInstanceDTO;
import cn.sliew.carp.framework.dag.service.dto.DagStepDTO;
import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskInstanceStage;
import cn.sliew.scaleph.queue.MessageListener;
import cn.sliew.scaleph.workflow.simple.statemachine.WorkflowTaskInstanceStateMachine;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@MessageListener(topic = WorkflowTaskInstanceFailureEventListener.TOPIC, consumerGroup = WorkflowTaskInstanceStateMachine.CONSUMER_GROUP)
public class WorkflowTaskInstanceFailureEventListener extends AbstractWorkflowTaskInstanceEventListener {

    public static final String TOPIC = "TOPIC_WORKFLOW_TASK_INSTANCE_PROCESS_FAILURE";

    @Override
    protected CompletableFuture handleEventAsync(WorkflowTaskInstanceEventDTO event) {
        return CompletableFuture.runAsync(new FailureRunner(event.getWorkflowTaskInstanceId(), event.getThrowable())).toCompletableFuture();
    }

    private class FailureRunner implements Runnable, Serializable {

        private Long workflowTaskInstanceId;
        private Optional<Throwable> throwable;

        public FailureRunner(Long workflowTaskInstanceId, Throwable throwable) {
            this.workflowTaskInstanceId = workflowTaskInstanceId;
            this.throwable = Optional.ofNullable(throwable);
        }

        @Override
        public void run() {
            DagStepDTO dagStepUpdateParam = new DagStepDTO();
            dagStepUpdateParam.setId(workflowTaskInstanceId);
            dagStepUpdateParam.setStatus(WorkflowTaskInstanceStage.FAILURE.getValue());
            dagStepUpdateParam.setEndTime(new Date());
            dagStepService.update(dagStepUpdateParam);

            DagStepDTO stepDTO = dagStepService.get(workflowTaskInstanceId);
            DagInstanceDTO instanceDTO = dagInstanceComplexService.selectSimpleOne(stepDTO.getDagInstanceId());
            workflowInstanceStateMachine.onTaskChange(instanceDTO);
        }
    }

}
