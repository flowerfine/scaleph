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
import cn.sliew.scaleph.workflow.service.WorkflowInstanceService;
import cn.sliew.scaleph.workflow.service.dto.WorkflowInstanceDTO;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkflowInstanceStateMachine implements InitializingBean {

    private WorkflowInstanceService workflowInstanceService;
    private StateMachine<WorkflowInstanceState, WorkflowInstanceEvent, WorkflowInstanceDTO> stateMachine;

    @Override
    public void afterPropertiesSet() throws Exception {
        StateMachineBuilder<WorkflowInstanceState, WorkflowInstanceEvent, WorkflowInstanceDTO> builder = StateMachineBuilderFactory.create();

        builder.externalTransition()
                .from(WorkflowInstanceState.PENDING)
                .to(WorkflowInstanceState.RUNNING)
                .on(WorkflowInstanceEvent.COMMAND_DEPLOY)
                .perform(doDeploy());
        builder.externalTransition()
                .from(WorkflowInstanceState.PENDING)
                .to(WorkflowInstanceState.SUSPEND)
                .on(WorkflowInstanceEvent.COMMAND_SUSPEND)
                .perform(doSuspend());
        builder.externalTransition()
                .from(WorkflowInstanceState.PENDING)
                .to(WorkflowInstanceState.TERMINATED)
                .on(WorkflowInstanceEvent.COMMAND_SHUTDOWN)
                .perform(doShutdown());

        builder.externalTransition()
                .from(WorkflowInstanceState.RUNNING)
                .to(WorkflowInstanceState.SUCCESS)
                .on(WorkflowInstanceEvent.PROCESS_SUCCESS)
                .perform(onSuccess());
        builder.externalTransition()
                .from(WorkflowInstanceState.RUNNING)
                .to(WorkflowInstanceState.FAILURE)
                .on(WorkflowInstanceEvent.PROCESS_FAILURE)
                .perform(onFailure());
        builder.externalTransition()
                .from(WorkflowInstanceState.RUNNING)
                .to(WorkflowInstanceState.SUSPEND)
                .on(WorkflowInstanceEvent.COMMAND_SUSPEND)
                .perform(doSuspend());
        builder.externalTransition()
                .from(WorkflowInstanceState.RUNNING)
                .to(WorkflowInstanceState.TERMINATED)
                .on(WorkflowInstanceEvent.COMMAND_SHUTDOWN)
                .perform(doShutdown());

        builder.externalTransition()
                .from(WorkflowInstanceState.SUSPEND)
                .to(WorkflowInstanceState.RUNNING)
                .on(WorkflowInstanceEvent.COMMAND_RESUME)
                .perform(doResume());
        builder.externalTransition()
                .from(WorkflowInstanceState.SUSPEND)
                .to(WorkflowInstanceState.TERMINATED)
                .on(WorkflowInstanceEvent.COMMAND_SHUTDOWN)
                .perform(doShutdown());

        this.stateMachine = builder.build("WorkflowInstanceStateMachine");
    }

    private Action<WorkflowInstanceState, WorkflowInstanceEvent, WorkflowInstanceDTO> doDeploy() {
        return new Action<WorkflowInstanceState, WorkflowInstanceEvent, WorkflowInstanceDTO>() {
            @Override
            public void execute(WorkflowInstanceState fromState, WorkflowInstanceState toState, WorkflowInstanceEvent eventEnum, WorkflowInstanceDTO workflowInstanceDTO) {
                log.info("执行部署任务");
            }
        };
    }

    private Action<WorkflowInstanceState, WorkflowInstanceEvent, WorkflowInstanceDTO> doShutdown() {
        return new Action<WorkflowInstanceState, WorkflowInstanceEvent, WorkflowInstanceDTO>() {
            @Override
            public void execute(WorkflowInstanceState fromState, WorkflowInstanceState toState, WorkflowInstanceEvent eventEnum, WorkflowInstanceDTO workflowInstanceDTO) {
                log.info("执行停止任务");
            }
        };
    }

    private Action<WorkflowInstanceState, WorkflowInstanceEvent, WorkflowInstanceDTO> doSuspend() {
        return new Action<WorkflowInstanceState, WorkflowInstanceEvent, WorkflowInstanceDTO>() {
            @Override
            public void execute(WorkflowInstanceState fromState, WorkflowInstanceState toState, WorkflowInstanceEvent eventEnum, WorkflowInstanceDTO workflowInstanceDTO) {
                log.info("执行暂停任务");
            }
        };
    }

    private Action<WorkflowInstanceState, WorkflowInstanceEvent, WorkflowInstanceDTO> doResume() {
        return new Action<WorkflowInstanceState, WorkflowInstanceEvent, WorkflowInstanceDTO>() {
            @Override
            public void execute(WorkflowInstanceState fromState, WorkflowInstanceState toState, WorkflowInstanceEvent eventEnum, WorkflowInstanceDTO workflowInstanceDTO) {
                log.info("执行恢复任务");
            }
        };
    }

    private Action<WorkflowInstanceState, WorkflowInstanceEvent, WorkflowInstanceDTO> onSuccess() {
        return new Action<WorkflowInstanceState, WorkflowInstanceEvent, WorkflowInstanceDTO>() {
            @Override
            public void execute(WorkflowInstanceState fromState, WorkflowInstanceState toState, WorkflowInstanceEvent eventEnum, WorkflowInstanceDTO workflowInstanceDTO) {
                log.info("任务成功");
            }
        };
    }

    private Action<WorkflowInstanceState, WorkflowInstanceEvent, WorkflowInstanceDTO> onFailure() {
        return new Action<WorkflowInstanceState, WorkflowInstanceEvent, WorkflowInstanceDTO>() {
            @Override
            public void execute(WorkflowInstanceState fromState, WorkflowInstanceState toState, WorkflowInstanceEvent eventEnum, WorkflowInstanceDTO workflowInstanceDTO) {
                log.info("任务失败");
            }
        };
    }

    public void deploy(WorkflowInstanceDTO workflowInstanceDTO) {
        stateMachine.fireEvent(workflowInstanceDTO.getState(), WorkflowInstanceEvent.COMMAND_DEPLOY, workflowInstanceDTO);
    }

    public void shutdown(WorkflowInstanceDTO workflowInstanceDTO) {
        stateMachine.fireEvent(workflowInstanceDTO.getState(), WorkflowInstanceEvent.COMMAND_SHUTDOWN, workflowInstanceDTO);
    }

    public void suspend(WorkflowInstanceDTO workflowInstanceDTO) {
        stateMachine.fireEvent(workflowInstanceDTO.getState(), WorkflowInstanceEvent.COMMAND_SUSPEND, workflowInstanceDTO);
    }

    public void resume(WorkflowInstanceDTO workflowInstanceDTO) {
        stateMachine.fireEvent(workflowInstanceDTO.getState(), WorkflowInstanceEvent.COMMAND_RESUME, workflowInstanceDTO);
    }
}
