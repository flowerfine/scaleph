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

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskInstanceEvent;
import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskInstanceStage;
import cn.sliew.scaleph.queue.Message;
import cn.sliew.scaleph.queue.Queue;
import cn.sliew.scaleph.queue.QueueFactory;
import cn.sliew.scaleph.queue.util.FuryUtil;
import cn.sliew.scaleph.workflow.listener.taskinstance.WorkflowTaskInstanceDeployEventListener;
import cn.sliew.scaleph.workflow.listener.taskinstance.WorkflowTaskInstanceEventDTO;
import cn.sliew.scaleph.workflow.listener.taskinstance.WorkflowTaskInstanceFailureEventListener;
import cn.sliew.scaleph.workflow.listener.taskinstance.WorkflowTaskInstanceSuccessEventListener;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskInstanceDTO;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkflowTaskInstanceStateMachine implements InitializingBean {

    public static final String CONSUMER_GROUP = "WorkflowTaskInstanceStateMachine";
    public static final String EXECUTOR = "WorkflowTaskInstanceExecute";

    @Autowired
    private QueueFactory queueFactory;

    private StateMachine<WorkflowTaskInstanceStage, WorkflowTaskInstanceEvent, Pair<Long, Throwable>> stateMachine;

    @Override
    public void afterPropertiesSet() throws Exception {
        StateMachineBuilder<WorkflowTaskInstanceStage, WorkflowTaskInstanceEvent, Pair<Long, Throwable>> builder = StateMachineBuilderFactory.create();

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

        this.stateMachine = builder.build(CONSUMER_GROUP);
    }

    private Action<WorkflowTaskInstanceStage, WorkflowTaskInstanceEvent, Pair<Long, Throwable>> doPerform() {
        return (fromState, toState, eventEnum, pair) -> {
            Queue queue = queueFactory.get(getTopic(eventEnum));
            WorkflowTaskInstanceEventDTO eventDTO = new WorkflowTaskInstanceEventDTO(fromState, toState, eventEnum, pair.getLeft(), pair.getRight());
            Message message = Message.builder()
                    .topic(queue.getName())
                    .body(FuryUtil.serialize(eventDTO))
                    .build();
            queue.push(message);
        };
    }

    private String getTopic(WorkflowTaskInstanceEvent event) {
        switch (event) {
            case COMMAND_DEPLOY:
                return WorkflowTaskInstanceDeployEventListener.TOPIC;
            case PROCESS_SUCCESS:
                return WorkflowTaskInstanceSuccessEventListener.TOPIC;
            case PROCESS_FAILURE:
                return WorkflowTaskInstanceFailureEventListener.TOPIC;
            default:
                throw new IllegalStateException("unknown workflow task instance event: " + JacksonUtil.toJsonString(event));
        }
    }

    public void deploy(WorkflowTaskInstanceDTO workflowTaskInstanceDTO) {
        stateMachine.fireEvent(workflowTaskInstanceDTO.getStage(), WorkflowTaskInstanceEvent.COMMAND_DEPLOY, Pair.of(workflowTaskInstanceDTO.getId(), null));
    }

    public void shutdown(WorkflowTaskInstanceDTO workflowTaskInstanceDTO) {
        stateMachine.fireEvent(workflowTaskInstanceDTO.getStage(), WorkflowTaskInstanceEvent.COMMAND_SHUTDOWN, Pair.of(workflowTaskInstanceDTO.getId(), null));
    }

    public void suspend(WorkflowTaskInstanceDTO workflowTaskInstanceDTO) {
        stateMachine.fireEvent(workflowTaskInstanceDTO.getStage(), WorkflowTaskInstanceEvent.COMMAND_SUSPEND, Pair.of(workflowTaskInstanceDTO.getId(), null));
    }

    public void resume(WorkflowTaskInstanceDTO workflowTaskInstanceDTO) {
        stateMachine.fireEvent(workflowTaskInstanceDTO.getStage(), WorkflowTaskInstanceEvent.COMMAND_RESUME, Pair.of(workflowTaskInstanceDTO.getId(), null));
    }

    public void onSuccess(WorkflowTaskInstanceDTO workflowTaskInstanceDTO) {
        stateMachine.fireEvent(workflowTaskInstanceDTO.getStage(), WorkflowTaskInstanceEvent.PROCESS_SUCCESS, Pair.of(workflowTaskInstanceDTO.getId(), null));
    }

    public void onFailure(WorkflowTaskInstanceDTO workflowTaskInstanceDTO, Throwable throwable) {
        stateMachine.fireEvent(workflowTaskInstanceDTO.getStage(), WorkflowTaskInstanceEvent.PROCESS_FAILURE, Pair.of(workflowTaskInstanceDTO.getId(), throwable));
    }
}
