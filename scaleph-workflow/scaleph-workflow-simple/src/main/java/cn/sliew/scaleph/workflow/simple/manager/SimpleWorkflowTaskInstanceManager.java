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

package cn.sliew.scaleph.workflow.simple.manager;

import cn.sliew.carp.framework.dag.service.DagStepService;
import cn.sliew.carp.framework.dag.service.dto.DagStepDTO;
import cn.sliew.scaleph.workflow.manager.WorkflowTaskInstanceManager;
import cn.sliew.scaleph.workflow.simple.statemachine.WorkflowTaskInstanceStateMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleWorkflowTaskInstanceManager implements WorkflowTaskInstanceManager {

    @Autowired
    private DagStepService dagStepService;
    @Autowired
    private WorkflowTaskInstanceStateMachine stateMachine;

    @Override
    public void deploy(Long id) {
        stateMachine.deploy(get(id));
    }

    @Override
    public void shutdown(Long id) {
        stateMachine.shutdown(get(id));
    }

    @Override
    public void suspend(Long id) {
        stateMachine.suspend(get(id));
    }

    @Override
    public void resume(Long id) {
        stateMachine.resume(get(id));
    }

    private DagStepDTO get(Long id) {
        return dagStepService.get(id);
    }
}
