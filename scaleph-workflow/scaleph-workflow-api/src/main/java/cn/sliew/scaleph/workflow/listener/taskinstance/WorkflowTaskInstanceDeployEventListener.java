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

package cn.sliew.scaleph.workflow.listener.taskinstance;

import cn.sliew.scaleph.queue.MessageListener;
import cn.sliew.scaleph.workflow.service.WorkflowTaskInstanceService;
import cn.sliew.scaleph.workflow.statemachine.WorkflowTaskInstanceStateMachine;
import org.apache.commons.lang3.RandomUtils;
import org.redisson.api.annotation.RInject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@MessageListener(topic = WorkflowTaskInstanceDeployEventListener.TOPIC, consumerGroup = WorkflowTaskInstanceStateMachine.CONSUMER_GROUP)
public class WorkflowTaskInstanceDeployEventListener extends AbstractWorkflowTaskInstanceEventListener {

    public static final String TOPIC = "TOPIC_WORKFLOW_TASK_INSTANCE_COMMAND_DEPLOY";

    @Override
    protected CompletableFuture handleEventAsync(WorkflowTaskInstanceEventDTO event) {
        CompletableFuture<?> future = executorService.submit(new DeployRunner(event)).toCompletableFuture();
        future.whenCompleteAsync((unused, throwable) -> {
            if (throwable != null) {
                onFailure(event.getWorkflowTaskInstanceId(), throwable);
            } else {
                stateMachine.onSuccess(workflowTaskInstanceService.get(event.getWorkflowTaskInstanceId()));
            }
        });
        return future;
    }

    public static class DeployRunner implements Runnable, Serializable {

        private WorkflowTaskInstanceEventDTO event;

        @RInject
        private String taskId;
        @Autowired
        private WorkflowTaskInstanceService workflowTaskInstanceService;

        public DeployRunner(WorkflowTaskInstanceEventDTO event) {
            this.event = event;
        }

        @Override
        public void run() {
            try {
                workflowTaskInstanceService.updateTaskId(event.getWorkflowTaskInstanceId(), taskId);
                workflowTaskInstanceService.updateState(event.getWorkflowTaskInstanceId(), event.getState(), event.getNextState(), null);
                TimeUnit.SECONDS.sleep(RandomUtils.nextLong(1, 30));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
