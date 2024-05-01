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

import cn.sliew.scaleph.common.dict.workflow.WorkflowInstanceEvent;
import cn.sliew.scaleph.common.dict.workflow.WorkflowInstanceState;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class WorkflowInstanceEventDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final WorkflowInstanceState state;
    private final WorkflowInstanceState nextState;
    private final WorkflowInstanceEvent event;
    private final Long workflowInstanceId;
    private final Throwable throwable;

    public WorkflowInstanceEventDTO(WorkflowInstanceState state, WorkflowInstanceState nextState, WorkflowInstanceEvent event, Long workflowInstanceId) {
        this(state, nextState, event, workflowInstanceId, null);
    }

    public WorkflowInstanceEventDTO(WorkflowInstanceState state, WorkflowInstanceState nextState, WorkflowInstanceEvent event, Long workflowInstanceId, Throwable throwable) {
        this.state = state;
        this.nextState = nextState;
        this.event = event;
        this.workflowInstanceId = workflowInstanceId;
        this.throwable = throwable;
    }
}
