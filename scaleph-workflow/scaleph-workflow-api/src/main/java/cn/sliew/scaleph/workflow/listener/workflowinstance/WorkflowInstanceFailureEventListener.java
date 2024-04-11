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

import cn.sliew.scaleph.queue.MessageListener;
import cn.sliew.scaleph.workflow.service.WorkflowInstanceService;
import cn.sliew.scaleph.workflow.statemachine.WorkflowInstanceStateMachine;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@MessageListener(topic = WorkflowInstanceFailureEventListener.TOPIC, consumerGroup = WorkflowInstanceStateMachine.CONSUMER_GROUP)
public class WorkflowInstanceFailureEventListener extends AbstractWorkflowInstanceEventListener {

    public static final String TOPIC = "TOPIC_WORKFLOW_INSTANCE_PROCESS_FAILURE";

    @Autowired
    private WorkflowInstanceService workflowInstanceService;

    @Override
    protected CompletableFuture handleEventAsync(WorkflowInstanceEventDTO event) {
        return CompletableFuture.runAsync(new FailureRunner(event.getWorkflowInstanceId(), event.getThrowable()));
    }

    private class FailureRunner implements Runnable, Serializable {

        private Long workflowInstanceId;
        private Optional<Throwable> throwable;

        public FailureRunner(Long workflowInstanceId, Optional<Throwable> throwable) {
            this.workflowInstanceId = workflowInstanceId;
            this.throwable = throwable;
        }

        @Override
        public void run() {
            workflowInstanceService.updateFailure(workflowInstanceId, throwable.orElse(null));
        }
    }
}
