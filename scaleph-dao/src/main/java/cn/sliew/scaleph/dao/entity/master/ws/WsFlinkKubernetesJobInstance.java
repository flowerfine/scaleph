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

package cn.sliew.scaleph.dao.entity.master.ws;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * flink kubernetes job instance
 * </p>
 */
@Data
@TableName("ws_flink_kubernetes_job_instance")
@ApiModel(value = "WsFlinkKubernetesJobInstance对象", description = "flink kubernetes job instance")
public class WsFlinkKubernetesJobInstance extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("ws_flink_kubernetes_job_id")
    private Long wsFlinkKubernetesJobId;

    @TableField("job_id")
    private String jobId;

    @TableField("job_manager")
    private String jobManager;

    @TableField("task_manager")
    private String taskManager;

    @TableField("user_flink_configuration")
    private String userFlinkConfiguration;

    @TableField("`status`")
    private String status;

    @TableField("state")
    private String state;

    @ApiModelProperty("开始时间")
    @TableField("start_time")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @TableField("end_time")
    private Date endTime;

    @ApiModelProperty("耗时")
    @TableField("duration")
    private Long duration;

}
