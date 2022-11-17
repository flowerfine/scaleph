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

import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskType;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * workflow task definition
 * </p>
 */
@Data
@TableName("workflow_task_definition")
@ApiModel(value = "WorkflowTaskDefinition对象", description = "workflow task definition")
public class WorkflowTaskDefinition extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("workflow definition id")
    @TableField("workflow_definition_id")
    private Long workflowDefinitionId;

    @ApiModelProperty("type")
    @TableField("`type`")
    private WorkflowTaskType type;

    @ApiModelProperty("name")
    @TableField("`name`")
    private String name;

    @ApiModelProperty("handler")
    @TableField("`handler`")
    private String handler;

    @ApiModelProperty("param")
    @TableField("param")
    private String param;

    @ApiModelProperty("remark")
    @TableField("remark")
    private String remark;

}
