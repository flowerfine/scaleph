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

package cn.sliew.scaleph.dao.entity.master.flink;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * flink job instance
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flink_job_instance")
@ApiModel(value = "FlinkJobInstance对象", description = "flink job instance")
public class FlinkJobInstance extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("job type. 0: jar, 1: sql+udf, 2: seatunnel")
    @TableField("`type`")
    private String type;

    @ApiModelProperty("flink 任务配置 ID")
    @TableField("flink_job_config_id")
    private Long flinkJobConfigId;

    @ApiModelProperty("flink 集群实例 ID")
    @TableField("flink_cluster_instance_id")
    private Long flinkClusterInstanceId;

    @ApiModelProperty("flink 任务 ID")
    @TableField("job_id")
    private String jobId;

    @ApiModelProperty("任务状态。0: 已创建, 1: 创建失败")
    @TableField("`status`")
    private String status;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

}
