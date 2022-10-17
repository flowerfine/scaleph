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

package cn.sliew.scaleph.engine.flink.service.dto;

import cn.sliew.scaleph.common.dict.flink.FlinkClusterStatus;
import cn.sliew.scaleph.common.dict.flink.FlinkJobState;
import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FlinkJobInstance对象", description = "flink job instance")
public class FlinkJobInstanceDTO extends BaseDTO {

    @ApiModelProperty("flink job code")
    private Long flinkJobCode;

    @ApiModelProperty("flink job version")
    private Long flinkJobVersion;

    @ApiModelProperty("flink job ID")
    private String jobId;

    @ApiModelProperty("flink job name")
    private String jobName;

    @ApiModelProperty("flink job state")
    private FlinkJobState jobState;

    @ApiModelProperty("cluster ID")
    private String clusterId;

    @ApiModelProperty("flink web-ui url")
    private String webInterfaceUrl;

    @ApiModelProperty("flink cluster status")
    private FlinkClusterStatus clusterStatus;

    @ApiModelProperty("job start time")
    private Date startTime;

    @ApiModelProperty("job end time")
    private Date endTime;

    @ApiModelProperty("flink cluster status")
    private Long duration;

}
