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

package cn.sliew.scaleph.workflow.service.impl;

import cn.sliew.scaleph.workflow.manager.WorkflowTaskInstanceManager;
import cn.sliew.scaleph.workflow.service.WorkflowTaskInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowTaskInstanceServiceImpl implements WorkflowTaskInstanceService {

    @Autowired
    private WorkflowTaskInstanceManager workflowTaskInstanceManager;

    @Override
    public void deploy(Long id) {
        workflowTaskInstanceManager.deploy(id);
    }

    @Override
    public void shutdown(Long id) {
        workflowTaskInstanceManager.shutdown(id);
    }

    @Override
    public void suspend(Long id) {
        workflowTaskInstanceManager.suspend(id);
    }

    @Override
    public void resume(Long id) {
        workflowTaskInstanceManager.resume(id);
    }
}
