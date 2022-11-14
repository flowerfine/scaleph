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

package cn.sliew.scaleph.workflow.service.dto;

import cn.sliew.scaleph.common.dict.workflow.WorkflowInstanceState;
import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskInstanceStage;
import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class WorkflowTaskInstanceDTO extends BaseDTO {

    @ApiModelProperty("workflow task definition")
    private WorkflowTaskDefinitionDTO workflowTaskDefinition;

    @ApiModelProperty("workflow task instance state")
    private WorkflowInstanceState state;

    @ApiModelProperty("workflow task instance state")
    private WorkflowTaskInstanceStage stage;

    @ApiModelProperty("workflow task instance start time")
    private Date startTime;

    @ApiModelProperty("workflow task instance end time")
    private Date endTime;

    @ApiModelProperty("workflow task instance message")
    private String message;

}
