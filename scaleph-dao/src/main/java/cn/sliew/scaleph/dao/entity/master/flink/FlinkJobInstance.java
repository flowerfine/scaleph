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

import cn.sliew.scaleph.common.dict.flink.FlinkClusterStatus;
import cn.sliew.scaleph.common.dict.flink.FlinkJobState;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

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

    @ApiModelProperty("flink job code")
    @TableField("flink_job_code")
    private Long flinkJobCode;

    @ApiModelProperty("flink job version")
    @TableField("flink_job_version")
    private Long flinkJobVersion;

    @ApiModelProperty("flink job ID")
    @TableField("job_id")
    private String jobId;

    @ApiModelProperty("flink job name")
    @TableField("job_name")
    private String jobName;

    @ApiModelProperty("flink job state")
    @TableField("`job_state`")
    private FlinkJobState jobState;

    @ApiModelProperty("cluster ID")
    @TableField("cluster_id")
    private String clusterId;

    @ApiModelProperty("flink web-ui url")
    @TableField("web_interface_url")
    private String webInterfaceUrl;

    @ApiModelProperty("flink cluster status")
    @TableField("cluster_status")
    private FlinkClusterStatus clusterStatus;

    @ApiModelProperty("job start time")
    @TableField("start_time")
    private Date startTime;

    @ApiModelProperty("job end time")
    @TableField("end_time")
    private Date endTime;

    @ApiModelProperty("flink cluster status")
    @TableField("duration")
    private Long duration;

}
