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

import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.scaleph.common.util.SpringApplicationContextUtil;
import cn.sliew.scaleph.queue.MessageListener;
import cn.sliew.scaleph.workflow.engine.Engine;
import cn.sliew.scaleph.workflow.engine.EngineBuilder;
import cn.sliew.scaleph.workflow.engine.action.Action;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionContextBuilder;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.ParallelFlow;
import cn.sliew.scaleph.workflow.engine.workflow.WorkFlow;
import cn.sliew.scaleph.workflow.service.WorkflowDefinitionService;
import cn.sliew.scaleph.workflow.service.WorkflowTaskInstanceService;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskDefinitionDTO2;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskInstanceDTO;
import cn.sliew.scaleph.workflow.simple.statemachine.WorkflowTaskInstanceStateMachine;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.annotation.RInject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@MessageListener(topic = WorkflowTaskInstanceDeployEventListener.TOPIC, consumerGroup = WorkflowTaskInstanceStateMachine.CONSUMER_GROUP)
public class WorkflowTaskInstanceDeployEventListener extends AbstractWorkflowTaskInstanceEventListener {

    public static final String TOPIC = "TOPIC_WORKFLOW_TASK_INSTANCE_COMMAND_DEPLOY";

    public static Engine engine = EngineBuilder.newInstance().build();

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

    @Slf4j
    public static class DeployRunner implements Runnable, Serializable {

        private WorkflowTaskInstanceEventDTO event;

        @RInject
        private String taskId;
        @Autowired
        private WorkflowDefinitionService workflowDefinitionService;
        @Autowired
        private WorkflowTaskInstanceService workflowTaskInstanceService;

        public DeployRunner(WorkflowTaskInstanceEventDTO event) {
            this.event = event;
        }

        @Override
        public void run() {
            workflowTaskInstanceService.updateTaskId(event.getWorkflowTaskInstanceId(), taskId);
            workflowTaskInstanceService.updateState(event.getWorkflowTaskInstanceId(), event.getState(), event.getNextState(), null);

            WorkflowTaskInstanceDTO workflowTaskInstanceDTO = workflowTaskInstanceService.get(event.getWorkflowTaskInstanceId());
            WorkflowTaskDefinitionDTO2 taskDefinition = workflowDefinitionService.getTaskDefinition(workflowTaskInstanceDTO.getStepId());

            try {
                Class<?> clazz = ClassUtils.forName(taskDefinition.getStepMeta().getHandler(), ClassUtils.getDefaultClassLoader());
                Action action = (Action) SpringApplicationContextUtil.getBean(clazz);
                WorkFlow workFlow = ParallelFlow.newParallelFlow()
                        .name(taskDefinition.getStepName())
                        .execute(action)
                        .build();
                ActionContext actionContext = buildActionContext(workflowTaskInstanceDTO);
                engine.run(workFlow, actionContext, new ActionListener<ActionResult>() {
                    @Override
                    public void onResponse(ActionResult result) {
                        log.debug("workflow task {} run success!", taskDefinition.getStepName());
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        log.error("workflow task {} run failure!", taskDefinition.getStepName(), e);
                    }
                });
            } catch (ClassNotFoundException e) {
                Rethrower.throwAs(e);
            }
        }

        private ActionContext buildActionContext(WorkflowTaskInstanceDTO workflowTaskInstanceDTO) {
            return ActionContextBuilder.newBuilder()
                    .withWorkflowDefinitionId(workflowTaskInstanceDTO.getWorkflowInstanceDTO().getWorkflowDefinition().getId())
                    .withWorkflowInstanceId(workflowTaskInstanceDTO.getWorkflowInstanceDTO().getId())
                    .withWorkflowTaskDefinitionId(workflowTaskInstanceDTO.getStepId())
                    .withWorkflowTaskInstanceId(workflowTaskInstanceDTO.getId())
                    .validateAndBuild();
        }
    }
}
