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
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.jackson.JsonMerger;
import cn.sliew.scaleph.common.util.SpringApplicationContextUtil;
import cn.sliew.scaleph.dag.service.DagConfigStepService;
import cn.sliew.scaleph.dag.service.DagInstanceComplexService;
import cn.sliew.scaleph.dag.service.DagStepService;
import cn.sliew.scaleph.dag.service.dto.DagConfigStepDTO;
import cn.sliew.scaleph.dag.service.dto.DagInstanceDTO;
import cn.sliew.scaleph.dag.service.dto.DagStepDTO;
import cn.sliew.scaleph.queue.MessageListener;
import cn.sliew.scaleph.workflow.engine.Engine;
import cn.sliew.scaleph.workflow.engine.EngineBuilder;
import cn.sliew.scaleph.workflow.engine.action.Action;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionContextBuilder;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.ParallelFlow;
import cn.sliew.scaleph.workflow.engine.workflow.WorkFlow;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskDefinitionMeta;
import cn.sliew.scaleph.workflow.simple.statemachine.WorkflowTaskInstanceStateMachine;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.annotation.RInject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.util.Date;
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
                stateMachine.onSuccess(dagStepService.selectOne(event.getWorkflowTaskInstanceId()));
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
        private DagInstanceComplexService dagInstanceComplexService;
        @Autowired
        private DagConfigStepService dagConfigStepService;
        @Autowired
        private DagStepService dagStepService;

        public DeployRunner(WorkflowTaskInstanceEventDTO event) {
            this.event = event;
        }

        @Override
        public void run() {

            DagStepDTO dagStepUpdateParam = new DagStepDTO();
            dagStepUpdateParam.setId(event.getWorkflowTaskInstanceId());
            dagStepUpdateParam.setStatus(event.getNextState().getValue());
            dagStepUpdateParam.setStartTime(new Date());
            dagStepService.update(dagStepUpdateParam);

            DagStepDTO stepDTO = dagStepService.selectOne(event.getWorkflowTaskInstanceId());
            DagInstanceDTO dagInstanceDTO = dagInstanceComplexService.selectSimpleOne(stepDTO.getDagInstanceId());
            DagConfigStepDTO configStepDTO = dagConfigStepService.selectOne(stepDTO.getDagConfigStep().getId());
            WorkflowTaskDefinitionMeta workflowTaskDefinitionMeta = JacksonUtil.toObject(configStepDTO.getStepMeta(), WorkflowTaskDefinitionMeta.class);
            try {
                Class<?> clazz = ClassUtils.forName(workflowTaskDefinitionMeta.getHandler(), ClassUtils.getDefaultClassLoader());
                Action action = (Action) SpringApplicationContextUtil.getBean(clazz);
                WorkFlow workFlow = ParallelFlow.newParallelFlow()
                        .name(configStepDTO.getStepName())
                        .execute(action)
                        .build();
                ActionContext actionContext = buildActionContext(dagInstanceDTO, stepDTO);
                engine.run(workFlow, actionContext, new ActionListener<ActionResult>() {
                    @Override
                    public void onResponse(ActionResult result) {
                        log.debug("workflow task {} run success!", configStepDTO.getStepName());
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        log.error("workflow task {} run failure!", configStepDTO.getStepName(), e);
                    }
                });
            } catch (ClassNotFoundException e) {
                Rethrower.throwAs(e);
            }
        }

        private ActionContext buildActionContext(DagInstanceDTO dagInstanceDTO, DagStepDTO stepDTO) {
            JsonNode dagInstanceInputs = dagInstanceDTO.getInputs();
            JsonNode dagStepInputs = stepDTO.getInputs();
            JsonNode mergedInputs = JsonMerger.doMerge(dagInstanceInputs, dagStepInputs);
            return ActionContextBuilder.newBuilder()
                    .withWorkflowDefinitionId(dagInstanceDTO.getDagConfig().getId())
                    .withWorkflowInstanceId(stepDTO.getDagInstanceId())
                    .withWorkflowTaskDefinitionId(stepDTO.getDagConfigStep().getId())
                    .withWorkflowTaskInstanceId(stepDTO.getId())
                    .withParams(JacksonUtil.toMap(mergedInputs))
                    .validateAndBuild();
        }
    }
}
