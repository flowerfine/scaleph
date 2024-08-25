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

package cn.sliew.scaleph.workflow.simple.listener.workflowinstance;

import cn.sliew.carp.framework.dag.service.DagConfigComplexService;
import cn.sliew.carp.framework.dag.service.DagInstanceComplexService;
import cn.sliew.carp.framework.dag.service.DagInstanceService;
import cn.sliew.carp.framework.dag.service.dto.DagConfigStepDTO;
import cn.sliew.carp.framework.dag.service.dto.DagInstanceDTO;
import cn.sliew.carp.framework.dag.service.dto.DagStepDTO;
import cn.sliew.scaleph.queue.MessageListener;
import cn.sliew.scaleph.workflow.manager.WorkflowTaskInstanceManager;
import cn.sliew.scaleph.workflow.simple.statemachine.WorkflowInstanceStateMachine;
import com.google.common.graph.Graph;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.annotation.RInject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
@MessageListener(topic = WorkflowInstanceDeployEventListener.TOPIC, consumerGroup = WorkflowInstanceStateMachine.CONSUMER_GROUP)
public class WorkflowInstanceDeployEventListener extends AbstractWorkflowInstanceEventListener {

    public static final String TOPIC = "TOPIC_WORKFLOW_INSTANCE_COMMAND_DEPLOY";

    @Override
    protected CompletableFuture handleEventAsync(WorkflowInstanceEventDTO event) {
        CompletableFuture<?> future = executorService.submit(new DeployRunner(event)).toCompletableFuture();
        future.whenCompleteAsync((unused, throwable) -> {
            if (throwable != null) {
                onFailure(event.getWorkflowInstanceId(), throwable);
            }
        });
        return future;
    }

    /**
     * 必须实现 Serializable 接口，无法使用 lambda
     */
    public static class DeployRunner implements Runnable, Serializable {

        private WorkflowInstanceEventDTO event;

        @RInject
        private String taskId;
        @Autowired
        private DagConfigComplexService dagConfigComplexService;
        @Autowired
        private DagInstanceComplexService dagInstanceComplexService;
        @Autowired
        private DagInstanceService dagInstanceService;
        @Autowired
        private WorkflowInstanceStateMachine stateMachine;
        @Autowired
        private WorkflowTaskInstanceManager workflowTaskInstanceManager;

        public DeployRunner(WorkflowInstanceEventDTO event) {
            this.event = event;
        }

        @Override
        public void run() {
            DagInstanceDTO dagInstanceUpdateParam = new DagInstanceDTO();
            dagInstanceUpdateParam.setId(event.getWorkflowInstanceId());
            dagInstanceUpdateParam.setStatus(event.getNextState().getValue());
            dagInstanceService.update(dagInstanceUpdateParam);

            DagInstanceDTO dagInstanceDTO = dagInstanceComplexService.selectSimpleOne(event.getWorkflowInstanceId());

            // 找到 root 节点，批量启动 root 节点
            Graph<DagConfigStepDTO> dag = dagConfigComplexService.getDag(dagInstanceDTO.getDagConfig().getId());

            // 无节点，直接成功
            if (dag.nodes().size() == 0) {
                stateMachine.onSuccess(dagInstanceDTO);
                return;
            }
            Graph<DagStepDTO> dagStepGraph = dagInstanceComplexService.getDag(event.getWorkflowInstanceId(), dag);

            Set<DagStepDTO> nodes = dagStepGraph.nodes();
            for (DagStepDTO dagStep : nodes) {
                // root 节点
                if (dagStepGraph.inDegree(dagStep) == 0) {
                    workflowTaskInstanceManager.deploy(dagStep.getId());
                }
            }
        }
    }
}
