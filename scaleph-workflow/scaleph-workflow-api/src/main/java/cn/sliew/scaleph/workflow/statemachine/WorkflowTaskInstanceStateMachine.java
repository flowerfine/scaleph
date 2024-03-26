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

import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskInstanceEvent;
import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskInstanceStage;
import cn.sliew.scaleph.workflow.listener.taskinstance.WorkflowTaskInstanceDeployEventListener;
import cn.sliew.scaleph.workflow.listener.taskinstance.WorkflowTaskInstanceEventDTO;
import cn.sliew.scaleph.workflow.listener.taskinstance.WorkflowTaskInstanceFailureEventListener;
import cn.sliew.scaleph.workflow.listener.taskinstance.WorkflowTaskInstanceSuccessEventListener;
import cn.sliew.scaleph.workflow.queue.Queue;
import cn.sliew.scaleph.workflow.queue.QueueFactory;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskInstanceDTO;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WorkflowTaskInstanceStateMachine implements InitializingBean {

    @Autowired
    private QueueFactory queueFactory;
    @Autowired
    private WorkflowTaskInstanceDeployEventListener workflowTaskInstanceDeployEventListener;
    @Autowired
    private WorkflowTaskInstanceSuccessEventListener workflowTaskInstanceSuccessEventListener;
    @Autowired
    private WorkflowTaskInstanceFailureEventListener workflowTaskInstanceFailureEventListener;


    private StateMachine<WorkflowTaskInstanceStage, WorkflowTaskInstanceEvent, WorkflowTaskInstanceDTO> stateMachine;
    private Map<WorkflowTaskInstanceEvent, Queue<WorkflowTaskInstanceEventDTO>> queueMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        StateMachineBuilder<WorkflowTaskInstanceStage, WorkflowTaskInstanceEvent, WorkflowTaskInstanceDTO> builder = StateMachineBuilderFactory.create();

        builder.externalTransition()
                .from(WorkflowTaskInstanceStage.PENDING)
                .to(WorkflowTaskInstanceStage.RUNNING)
                .on(WorkflowTaskInstanceEvent.COMMAND_DEPLOY)
                .perform(doPerform());

        builder.externalTransition()
                .from(WorkflowTaskInstanceStage.RUNNING)
                .to(WorkflowTaskInstanceStage.SUCCESS)
                .on(WorkflowTaskInstanceEvent.PROCESS_SUCCESS)
                .perform(doPerform());
        builder.externalTransition()
                .from(WorkflowTaskInstanceStage.RUNNING)
                .to(WorkflowTaskInstanceStage.FAILURE)
                .on(WorkflowTaskInstanceEvent.PROCESS_FAILURE)
                .perform(doPerform());
        builder.externalTransition()
                .from(WorkflowTaskInstanceStage.RUNNING)
                .to(WorkflowTaskInstanceStage.SUSPEND)
                .on(WorkflowTaskInstanceEvent.COMMAND_SUSPEND)
                .perform(doPerform());
        builder.externalTransition()
                .from(WorkflowTaskInstanceStage.RUNNING)
                .to(WorkflowTaskInstanceStage.TERMINATED)
                .on(WorkflowTaskInstanceEvent.COMMAND_SHUTDOWN)
                .perform(doPerform());

        builder.externalTransition()
                .from(WorkflowTaskInstanceStage.SUSPEND)
                .to(WorkflowTaskInstanceStage.RUNNING)
                .on(WorkflowTaskInstanceEvent.COMMAND_RESUME)
                .perform(doPerform());
        builder.externalTransition()
                .from(WorkflowTaskInstanceStage.SUSPEND)
                .to(WorkflowTaskInstanceStage.TERMINATED)
                .on(WorkflowTaskInstanceEvent.COMMAND_SHUTDOWN)
                .perform(doPerform());

        this.stateMachine = builder.build("WorkflowTaskInstanceStateMachine");
        this.queueMap = new HashMap<>();

        Queue deployQueue = queueFactory.newInstance(WorkflowTaskInstanceEvent.COMMAND_DEPLOY.getValue());
        deployQueue.register("WorkflowTaskInstanceStateMachine", workflowTaskInstanceDeployEventListener);
        queueMap.put(WorkflowTaskInstanceEvent.COMMAND_DEPLOY, deployQueue);

        Queue successQueue = queueFactory.newInstance(WorkflowTaskInstanceEvent.PROCESS_SUCCESS.getValue());
        successQueue.register("WorkflowTaskInstanceStateMachine", workflowTaskInstanceSuccessEventListener);
        queueMap.put(WorkflowTaskInstanceEvent.PROCESS_SUCCESS, successQueue);

        Queue failureQueue = queueFactory.newInstance(WorkflowTaskInstanceEvent.PROCESS_FAILURE.getValue());
        failureQueue.register("WorkflowTaskInstanceStateMachine", workflowTaskInstanceFailureEventListener);
        queueMap.put(WorkflowTaskInstanceEvent.PROCESS_FAILURE, failureQueue);
    }

    private Action<WorkflowTaskInstanceStage, WorkflowTaskInstanceEvent, WorkflowTaskInstanceDTO> doPerform() {
        return (fromState, toState, eventEnum, workflowTaskInstanceDTO) -> {
            Queue<WorkflowTaskInstanceEventDTO> queue = queueMap.get(eventEnum);
            if (queue != null) {
                queue.push(new WorkflowTaskInstanceEventDTO(eventEnum.getValue(), fromState, toState, eventEnum, workflowTaskInstanceDTO));
            } else {
                log.error("queue not found, event: {}", eventEnum.getValue());
            }
        };
    }

    public void deploy(WorkflowTaskInstanceDTO workflowTaskInstanceDTO) {
        stateMachine.fireEvent(workflowTaskInstanceDTO.getStage(), WorkflowTaskInstanceEvent.COMMAND_DEPLOY, workflowTaskInstanceDTO);
    }

    public void shutdown(WorkflowTaskInstanceDTO workflowTaskInstanceDTO) {
        stateMachine.fireEvent(workflowTaskInstanceDTO.getStage(), WorkflowTaskInstanceEvent.COMMAND_SHUTDOWN, workflowTaskInstanceDTO);
    }

    public void suspend(WorkflowTaskInstanceDTO workflowTaskInstanceDTO) {
        stateMachine.fireEvent(workflowTaskInstanceDTO.getStage(), WorkflowTaskInstanceEvent.COMMAND_SUSPEND, workflowTaskInstanceDTO);
    }

    public void resume(WorkflowTaskInstanceDTO workflowTaskInstanceDTO) {
        stateMachine.fireEvent(workflowTaskInstanceDTO.getStage(), WorkflowTaskInstanceEvent.COMMAND_RESUME, workflowTaskInstanceDTO);
    }

    public void onSuccess(WorkflowTaskInstanceDTO workflowTaskInstanceDTO) {
        stateMachine.fireEvent(workflowTaskInstanceDTO.getStage(), WorkflowTaskInstanceEvent.PROCESS_SUCCESS, workflowTaskInstanceDTO);
    }

    public void onFailure(WorkflowTaskInstanceDTO workflowTaskInstanceDTO, Throwable throwable) {
        stateMachine.fireEvent(workflowTaskInstanceDTO.getStage(), WorkflowTaskInstanceEvent.PROCESS_FAILURE, workflowTaskInstanceDTO);
    }
}
