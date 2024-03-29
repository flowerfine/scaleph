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

package cn.sliew.scaleph.workflow.statemachine;

import cn.sliew.scaleph.common.dict.workflow.WorkflowInstanceEvent;
import cn.sliew.scaleph.common.dict.workflow.WorkflowInstanceState;
import cn.sliew.scaleph.workflow.listener.workflowinstance.*;
import cn.sliew.scaleph.workflow.queue.Queue;
import cn.sliew.scaleph.workflow.queue.QueueFactory;
import cn.sliew.scaleph.workflow.service.dto.WorkflowInstanceDTO;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WorkflowInstanceStateMachine implements InitializingBean {

    public static final String CONSUMER_GROUP = "WorkflowInstanceStateMachine";
    public static final String EXECUTOR = "WorkflowInstanceExecute";

    @Autowired
    private QueueFactory queueFactory;
    @Autowired
    private WorkflowInstanceDeployEventListener workflowInstanceDeployEventListener;
    @Autowired
    private WorkflowInstanceShutdownEventListener workflowInstanceShutdownEventListener;
    @Autowired
    private WorkflowInstanceSuspendEventListener workflowInstanceSuspendEventListener;
    @Autowired
    private WorkflowInstanceResumeEventListener workflowInstanceResumeEventListener;
    @Autowired
    private WorkflowInstanceSuccessEventListener workflowInstanceSuccessEventListener;
    @Autowired
    private WorkflowInstanceFailureEventListener workflowInstanceFailureEventListener;

    private StateMachine<WorkflowInstanceState, WorkflowInstanceEvent, Pair<Long, Throwable>> stateMachine;
    private Map<WorkflowInstanceEvent, Queue<WorkflowInstanceEventDTO>> queueMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        StateMachineBuilder<WorkflowInstanceState, WorkflowInstanceEvent, Pair<Long, Throwable>> builder = StateMachineBuilderFactory.create();

        builder.externalTransition()
                .from(WorkflowInstanceState.PENDING)
                .to(WorkflowInstanceState.RUNNING)
                .on(WorkflowInstanceEvent.COMMAND_DEPLOY)
                .perform(doPerform());

        builder.externalTransition()
                .from(WorkflowInstanceState.RUNNING)
                .to(WorkflowInstanceState.SUCCESS)
                .on(WorkflowInstanceEvent.PROCESS_SUCCESS)
                .perform(doPerform());
        builder.externalTransition()
                .from(WorkflowInstanceState.RUNNING)
                .to(WorkflowInstanceState.FAILURE)
                .on(WorkflowInstanceEvent.PROCESS_FAILURE)
                .perform(doPerform());
        builder.externalTransition()
                .from(WorkflowInstanceState.RUNNING)
                .to(WorkflowInstanceState.SUSPEND)
                .on(WorkflowInstanceEvent.COMMAND_SUSPEND)
                .perform(doPerform());
        builder.externalTransition()
                .from(WorkflowInstanceState.RUNNING)
                .to(WorkflowInstanceState.TERMINATED)
                .on(WorkflowInstanceEvent.COMMAND_SHUTDOWN)
                .perform(doPerform());

        builder.externalTransition()
                .from(WorkflowInstanceState.SUSPEND)
                .to(WorkflowInstanceState.RUNNING)
                .on(WorkflowInstanceEvent.COMMAND_RESUME)
                .perform(doPerform());
        builder.externalTransition()
                .from(WorkflowInstanceState.SUSPEND)
                .to(WorkflowInstanceState.TERMINATED)
                .on(WorkflowInstanceEvent.COMMAND_SHUTDOWN)
                .perform(doPerform());

        this.stateMachine = builder.build(CONSUMER_GROUP);
        this.queueMap = new HashMap<>();

        Queue deployQueue = queueFactory.newInstance("WorkflowInstanceEvent#" + WorkflowInstanceEvent.COMMAND_DEPLOY.getValue());
        deployQueue.register(CONSUMER_GROUP, workflowInstanceDeployEventListener);
        queueMap.put(WorkflowInstanceEvent.COMMAND_DEPLOY, deployQueue);

        Queue shutDownQueue = queueFactory.newInstance("WorkflowInstanceEvent#" + WorkflowInstanceEvent.COMMAND_SHUTDOWN.getValue());
        shutDownQueue.register(CONSUMER_GROUP, workflowInstanceShutdownEventListener);
        queueMap.put(WorkflowInstanceEvent.COMMAND_SHUTDOWN, shutDownQueue);

        Queue suspendQueue = queueFactory.newInstance("WorkflowInstanceEvent#" + WorkflowInstanceEvent.COMMAND_SUSPEND.getValue());
        suspendQueue.register(CONSUMER_GROUP, workflowInstanceSuspendEventListener);
        queueMap.put(WorkflowInstanceEvent.COMMAND_SUSPEND, suspendQueue);

        Queue resumeQueue = queueFactory.newInstance("WorkflowInstanceEvent#" + WorkflowInstanceEvent.COMMAND_RESUME.getValue());
        resumeQueue.register(CONSUMER_GROUP, workflowInstanceResumeEventListener);
        queueMap.put(WorkflowInstanceEvent.COMMAND_RESUME, resumeQueue);

        Queue successQueue = queueFactory.newInstance("WorkflowInstanceEvent#" + WorkflowInstanceEvent.PROCESS_SUCCESS.getValue());
        successQueue.register(CONSUMER_GROUP, workflowInstanceSuccessEventListener);
        queueMap.put(WorkflowInstanceEvent.PROCESS_SUCCESS, successQueue);

        Queue failureQueue = queueFactory.newInstance("WorkflowInstanceEvent#" + WorkflowInstanceEvent.PROCESS_FAILURE.getValue());
        failureQueue.register(CONSUMER_GROUP, workflowInstanceFailureEventListener);
        queueMap.put(WorkflowInstanceEvent.PROCESS_FAILURE, failureQueue);
    }

    private Action<WorkflowInstanceState, WorkflowInstanceEvent, Pair<Long, Throwable>> doPerform() {
        return (fromState, toState, eventEnum, pair) -> {
            Queue<WorkflowInstanceEventDTO> queue = queueMap.get(eventEnum);
            if (queue != null) {
                queue.push(new WorkflowInstanceEventDTO(queue.getName(), fromState, toState, eventEnum, pair.getLeft(), pair.getRight()));
            } else {
                log.error("queue not found, event:{}", eventEnum.getValue());
            }
        };
    }

    public void deploy(WorkflowInstanceDTO workflowInstanceDTO) {
        stateMachine.fireEvent(workflowInstanceDTO.getState(), WorkflowInstanceEvent.COMMAND_DEPLOY, Pair.of(workflowInstanceDTO.getId(), null));
    }

    public void shutdown(WorkflowInstanceDTO workflowInstanceDTO) {
        stateMachine.fireEvent(workflowInstanceDTO.getState(), WorkflowInstanceEvent.COMMAND_SHUTDOWN, Pair.of(workflowInstanceDTO.getId(), null));
    }

    public void suspend(WorkflowInstanceDTO workflowInstanceDTO) {
        stateMachine.fireEvent(workflowInstanceDTO.getState(), WorkflowInstanceEvent.COMMAND_SUSPEND, Pair.of(workflowInstanceDTO.getId(), null));
    }

    public void resume(WorkflowInstanceDTO workflowInstanceDTO) {
        stateMachine.fireEvent(workflowInstanceDTO.getState(), WorkflowInstanceEvent.COMMAND_RESUME, Pair.of(workflowInstanceDTO.getId(), null));
    }

    public void onSuccess(WorkflowInstanceDTO workflowInstanceDTO) {
        stateMachine.fireEvent(workflowInstanceDTO.getState(), WorkflowInstanceEvent.PROCESS_SUCCESS, Pair.of(workflowInstanceDTO.getId(), null));
    }

    public void onFailure(WorkflowInstanceDTO workflowInstanceDTO, Throwable throwable) {
        stateMachine.fireEvent(workflowInstanceDTO.getState(), WorkflowInstanceEvent.PROCESS_FAILURE, Pair.of(workflowInstanceDTO.getId(), throwable));
    }
}
