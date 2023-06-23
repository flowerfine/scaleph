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

import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskType;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class WorkflowTaskDefinitionDTO extends BaseDTO {

    @Schema(description = "workflow definition id")
    private Long workflowDefinitionId;

    @Schema(description = "workflow task type")
    private WorkflowTaskType type;

    @Schema(description = "workflow task name")
    private String name;

    @Schema(description = "workflow task handler")
    private String handler;

    @Schema(description = "workflow task param")
    private Map<String, Object> param;

    @Schema(description = "备注")
    private String remark;

}
