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

package cn.sliew.scaleph.dao.entity.master.dag;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DAG 步骤
 */
@Data
@TableName("dag_step")
@Schema(name = "DagStep", description = "DAG 步骤")
public class DagStep extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "DAG id")
    @TableField("dag_id")
    private Long dagId;

    @Schema(description = "步骤id")
    @TableField("step_id")
    private String stepId;

    @Schema(description = "步骤名称")
    @TableField("step_name")
    private String stepName;

    @Schema(description = "x坐标")
    @TableField("position_x")
    private Integer positionX;

    @Schema(description = "y坐标")
    @TableField("position_y")
    private Integer positionY;

    @Schema(description = "步骤元信息")
    @TableField("step_meta")
    private String stepMeta;

    @Schema(description = "步骤属性")
    @TableField("step_attrs")
    private String stepAttrs;
}
