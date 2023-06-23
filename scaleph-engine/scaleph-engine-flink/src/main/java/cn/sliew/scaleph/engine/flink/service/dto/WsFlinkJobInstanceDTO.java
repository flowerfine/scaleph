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
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "FlinkJobInstance对象", description = "flink job instance")
public class WsFlinkJobInstanceDTO extends BaseDTO {

    @Schema(description = "flink job code")
    private Long flinkJobCode;

    @Schema(description = "flink job ID")
    private String jobId;

    @Schema(description = "flink job name")
    private String jobName;

    @Schema(description = "flink job state")
    private FlinkJobState jobState;

    @Schema(description = "cluster ID")
    private String clusterId;

    @Schema(description = "flink web-ui url")
    private String webInterfaceUrl;

    @Schema(description = "flink cluster status")
    private FlinkClusterStatus clusterStatus;

    @Schema(description = "job start time")
    private Date startTime;

    @Schema(description = "job end time")
    private Date endTime;

    @Schema(description = "flink cluster status")
    private Long duration;

}
