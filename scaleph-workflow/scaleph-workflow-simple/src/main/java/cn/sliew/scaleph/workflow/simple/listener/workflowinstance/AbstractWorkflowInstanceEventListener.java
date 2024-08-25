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

import cn.sliew.carp.framework.dag.service.DagInstanceComplexService;
import cn.sliew.scaleph.workflow.simple.statemachine.WorkflowInstanceStateMachine;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.redisson.api.WorkerOptions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class AbstractWorkflowInstanceEventListener implements WorkflowInstanceEventListener, InitializingBean, BeanFactoryAware {

    private BeanFactory beanFactory;
    protected RScheduledExecutorService executorService;

    @Autowired
    protected DagInstanceComplexService dagInstanceComplexService;
    @Autowired
    protected WorkflowInstanceStateMachine stateMachine;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService = redissonClient.getExecutorService(WorkflowInstanceStateMachine.EXECUTOR);
        executorService.registerWorkers(WorkerOptions.defaults().workers(20).beanFactory(beanFactory));
    }

    @Override
    public void onEvent(WorkflowInstanceEventDTO event) {
        try {
            handleEventAsync(event);
        } catch (Throwable throwable) {
            onFailure(event.getWorkflowInstanceId(), throwable);
        }
    }

    protected void onFailure(Long workflowInstanceId, Throwable throwable) {
        stateMachine.onFailure(dagInstanceComplexService.selectSimpleOne(workflowInstanceId), throwable);
    }

    protected abstract CompletableFuture handleEventAsync(WorkflowInstanceEventDTO event);
}
