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

package cn.sliew.scaleph.workflow.listener.workflowinstance;

import cn.sliew.scaleph.common.dict.workflow.WorkflowInstanceEvent;
import cn.sliew.scaleph.common.dict.workflow.WorkflowInstanceState;
import cn.sliew.scaleph.workflow.queue.Event;
import cn.sliew.scaleph.workflow.service.dto.WorkflowInstanceDTO;
import lombok.Getter;

import java.util.Optional;

@Getter
public class WorkflowInstanceEventDTO implements Event {

    private final String topic;
    private final WorkflowInstanceState state;
    private final WorkflowInstanceState nextState;
    private final WorkflowInstanceEvent event;
    private final Long workflowInstanceId;
    private final Optional<Exception> exception;

    public WorkflowInstanceEventDTO(String topic, WorkflowInstanceState state, WorkflowInstanceState nextState, WorkflowInstanceEvent event, Long workflowInstanceId) {
        this.topic = topic;
        this.state = state;
        this.nextState = nextState;
        this.event = event;
        this.workflowInstanceId = workflowInstanceId;
        this.exception = Optional.empty();
    }

    public WorkflowInstanceEventDTO(String topic, WorkflowInstanceState state, WorkflowInstanceState nextState, WorkflowInstanceEvent event, Long workflowInstanceId, Exception exception) {
        this.topic = topic;
        this.state = state;
        this.nextState = nextState;
        this.event = event;
        this.workflowInstanceId = workflowInstanceId;
        this.exception = Optional.ofNullable(exception);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
