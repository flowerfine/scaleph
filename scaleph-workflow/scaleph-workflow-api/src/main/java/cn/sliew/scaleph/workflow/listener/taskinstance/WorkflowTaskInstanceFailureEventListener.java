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

import cn.sliew.scaleph.workflow.service.WorkflowTaskInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.annotation.RInject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class WorkflowTaskInstanceFailureEventListener extends AbstractWorkflowTaskInstanceEventListener {
    
    @Override
    protected CompletableFuture handleEventAsync(WorkflowTaskInstanceEventDTO event) {
        return executorService.submit(new FailureRunner(event.getWorkflowTaskInstanceId(), event.getThrowable())).toCompletableFuture();
    }

    public static class FailureRunner implements Runnable, Serializable {

        private Long workflowTaskInstanceId;
        private Optional<Throwable> throwable;

        @RInject
        private String taskId;
        @Autowired
        private WorkflowTaskInstanceService workflowTaskInstanceService;

        public FailureRunner(Long workflowTaskInstanceId, Optional<Throwable> throwable) {
            this.workflowTaskInstanceId = workflowTaskInstanceId;
            this.throwable = throwable;
        }

        @Override
        public void run() {
            workflowTaskInstanceService.updateFailure(workflowTaskInstanceId, throwable.orElse(null));
            log.info("执行子任务失败啦, workflowTaskInstanceId: {}, taskId: {}", workflowTaskInstanceId, taskId, throwable);
        }
    }

}
