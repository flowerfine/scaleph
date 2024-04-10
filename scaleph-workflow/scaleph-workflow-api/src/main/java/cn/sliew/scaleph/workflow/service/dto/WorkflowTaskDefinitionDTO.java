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

import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class WorkflowTaskDefinitionDTO extends BaseDTO {

    @Schema(description = "workflow definition id")
    private Long workflowDefinitionId;

    @Schema(description = "dag id")
    private Long dagId;

    @Schema(description = "步骤id")
    private String stepId;

    @Schema(description = "步骤名称")
    private String stepName;

    @Schema(description = "x坐标")
    private Integer positionX;

    @Schema(description = "y坐标")
    private Integer positionY;

    @Schema(description = "step meta")
    private WorkflowTaskDefinitionMeta stepMeta;

    @Schema(description = "step attrs")
    private WorkflowTaskDefinitionAttrs stepAttrs;
}
