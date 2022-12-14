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

import cn.sliew.scaleph.common.dict.workflow.WorkflowExecuteType;
import cn.sliew.scaleph.common.dict.workflow.WorkflowType;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("workflow_definition")
@ApiModel(value = "WorkflowDefinitionVO对象", description = "workflow definition vo")
public class WorkflowDefinitionVO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("0: system, 1: user")
    @TableField("`type`")
    private WorkflowType type;

    @ApiModelProperty("name")
    @TableField("`name`")
    private String name;

    @ApiModelProperty("0: sequential, 1: parallel, 2: dependent, 3: if, 4: switch, 5: while")
    @TableField("execute_type")
    private WorkflowExecuteType executeType;

    @ApiModelProperty("param")
    @TableField("param")
    private String param;

    @ApiModelProperty("remark")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("schedule")
    @TableField("schedule")
    private WorkflowSchedule schedule;

}
