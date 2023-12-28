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
 * DAG 连线
 */
@Data
@TableName("dag_link")
@Schema(name = "DagLink", description = "DAG 连线")
public class DagLink extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "DAG id")
    @TableField("dag_id")
    private Long dagId;

    @Schema(description = "连线id")
    @TableField("link_id")
    private String linkId;

    @Schema(description = "连线名称")
    @TableField("link_name")
    private String linkName;

    @Schema(description = "源步骤id")
    @TableField("from_step_id")
    private String fromStepId;

    @Schema(description = "目标步骤id")
    @TableField("to_step_id")
    private String toStepId;

    @Schema(description = "连线元信息")
    @TableField("link_meta")
    private String linkMeta;

    @Schema(description = "连线属性")
    @TableField("link_attrs")
    private String linkAttrs;
}
