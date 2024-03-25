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

package cn.sliew.scaleph.workflow.listener.taskinstance;

import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskInstanceEvent;
import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskInstanceStage;
import cn.sliew.scaleph.workflow.queue.Event;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskInstanceDTO;
import lombok.Getter;

import java.util.Optional;

@Getter
public class WorkflowTaskInstanceEventDTO implements Event {

    private final String topic;
    private final WorkflowTaskInstanceStage state;
    private final WorkflowTaskInstanceStage nextState;
    private final WorkflowTaskInstanceEvent event;
    private final WorkflowTaskInstanceDTO workflowTaskInstanceDTO;
    private final Optional<Exception> exception;

    public WorkflowTaskInstanceEventDTO(String topic, WorkflowTaskInstanceStage state, WorkflowTaskInstanceStage nextState, WorkflowTaskInstanceEvent event, WorkflowTaskInstanceDTO workflowTaskInstanceDTO) {
        this.topic = topic;
        this.state = state;
        this.nextState = nextState;
        this.event = event;
        this.workflowTaskInstanceDTO = workflowTaskInstanceDTO;
        this.exception = Optional.empty();
    }

    public WorkflowTaskInstanceEventDTO(String topic, WorkflowTaskInstanceStage state, WorkflowTaskInstanceStage nextState, WorkflowTaskInstanceEvent event, WorkflowTaskInstanceDTO workflowTaskInstanceDTO, Exception exception) {
        this.topic = topic;
        this.state = state;
        this.nextState = nextState;
        this.event = event;
        this.workflowTaskInstanceDTO = workflowTaskInstanceDTO;
        this.exception = Optional.ofNullable(exception);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
