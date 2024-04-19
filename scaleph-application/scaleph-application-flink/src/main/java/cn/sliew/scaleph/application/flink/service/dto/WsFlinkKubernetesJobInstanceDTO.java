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

package cn.sliew.scaleph.application.flink.service.dto;

import cn.sliew.scaleph.common.dict.flink.FlinkJobState;
import cn.sliew.scaleph.common.dict.flink.kubernetes.ResourceLifecycleState;
import cn.sliew.scaleph.common.dict.flink.kubernetes.UpgradeMode;
import cn.sliew.scaleph.dao.entity.BaseDO;
import cn.sliew.scaleph.application.flink.operator.spec.JobManagerSpec;
import cn.sliew.scaleph.application.flink.operator.spec.TaskManagerSpec;
import cn.sliew.scaleph.application.flink.operator.status.TaskManagerInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * flink kubernetes job instance
 * </p>
 */
@Data
@Schema(name = "WsFlinkKubernetesJobInstance对象", description = "flink kubernetes job instance")
public class WsFlinkKubernetesJobInstanceDTO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "flink kubernetes job id")
    private Long wsFlinkKubernetesJobId;

    @Schema(description = "flink kubernetes job")
    private WsFlinkKubernetesJobDTO wsFlinkKubernetesJob;

    @Schema(description = "instance id")
    private String instanceId;

    @Schema(description = "parallelism")
    private Integer parallelism;

    @Schema(description = "upgrade mode")
    private UpgradeMode upgradeMode;

    @Schema(description = "allow to skip savepoint state")
    private Boolean allowNonRestoredState;

    @Schema(description = "job manager")
    private JobManagerSpec jobManager;

    @Schema(description = "task manager")
    private TaskManagerSpec taskManager;

    @Schema(description = "user flink configuration")
    private Map<String, String> userFlinkConfiguration;

    @Schema(description = "merged flink configuration")
    private Map<String, String> mergedFlinkConfiguration;

    @Schema(description = "deploy state")
    private ResourceLifecycleState state;

    @Schema(description = "job state")
    private FlinkJobState jobState;

    @Schema(description = "error")
    private String error;

    @Schema(description = "cluster info")
    private Map<String, String> clusterInfo;

    @Schema(description = "task manager info")
    private TaskManagerInfo taskManagerInfo;

    @Schema(description = "start time")
    private Date startTime;

    @Schema(description = "end time")
    private Date endTime;

    @Schema(description = "duration")
    private Long duration;

}
