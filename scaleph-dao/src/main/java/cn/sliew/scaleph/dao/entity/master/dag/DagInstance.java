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

import java.util.Date;

/**
 * DAG 实例
 */
@Data
@TableName("dag_instance")
@Schema(name = "DagInstance", description = "DAG 实例")
public class DagInstance extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "DAG配置id")
    @TableField("dag_config_id")
    private Long dagConfigId;

    @Schema(description = "instance id")
    @TableField("instance_id")
    private String instanceId;

    @Schema(description = "输入参数")
    @TableField("inputs")
    private String inputs;

    @Schema(description = "输出参数")
    @TableField("outputs")
    private String outputs;

    @Schema(description = "状态")
    @TableField("`status`")
    private String status;

    @Schema(description = "启动时间")
    @TableField("start_time")
    private Date startTime;

    @Schema(description = "结束时间")
    @TableField("end_time")
    private Date endTime;
}
