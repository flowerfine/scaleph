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

package cn.sliew.scaleph.dao.entity.master.workflow;

import cn.sliew.scaleph.common.dict.workflow.WorkflowInstanceState;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "WorkflowInstance对象", description = "workflow instance")
public class WorkflowInstanceVO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("workflow definition")
    @TableField(value = "workflow_definition_id", exist = false)
    private WorkflowDefinition workflowDefinition;

    @ApiModelProperty("workflow instance state")
    @TableField("state")
    private WorkflowInstanceState state;

    @ApiModelProperty("start time")
    @TableField("start_time")
    private Date startTime;

    @ApiModelProperty("end time")
    @TableField("end_time")
    private Date endTime;

    @ApiModelProperty("message")
    @TableField("message")
    private String message;

}
