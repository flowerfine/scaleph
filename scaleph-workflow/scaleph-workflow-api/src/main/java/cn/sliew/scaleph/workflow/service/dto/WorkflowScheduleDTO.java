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

import cn.sliew.scaleph.common.dict.workflow.ScheduleStatus;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class WorkflowScheduleDTO extends BaseDTO {

    @Schema(description = "workflow definition id")
    private Long workflowDefinitionId;

    @Schema(description = "schedule cron timezone")
    private String timezone;

    @Schema(description = "schedule crontab expression")
    private String crontab;

    @Schema(description = "schedule start time")
    private Date startTime;

    @Schema(description = "schedule end time")
    private Date endTime;

    @Schema(description = "status")
    private ScheduleStatus status;

    @Schema(description = "remark")
    private String remark;
}
