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

package cn.sliew.scaleph.application.flink.service.param;

import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.FlinkRuntimeExecutionMode;
import cn.sliew.scaleph.common.dict.flink.kubernetes.DeploymentKind;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WsFlinkKubernetesJobAddParam {

    @NotNull
    @Schema(description = "project id")
    private Long projectId;

    @NotBlank
    @Schema(description = "name")
    private String name;

    @NotNull
    @Schema(description = "execution mode")
    private FlinkRuntimeExecutionMode executionMode;

    @NotNull
    @Schema(description = "deployment kind")
    private DeploymentKind deploymentKind;

    @Schema(description = "flink deployment id")
    private Long flinkDeploymentId;

    @Schema(description = "flink session cluster id")
    private Long flinkSessionClusterId;

    @NotNull
    @Schema(description = "flink job type")
    private FlinkJobType type;

    @Schema(description = "artifact flink-jar id")
    private Long artifactFlinkJarId;

    @Schema(description = "artifact flink-sql id")
    private Long artifactFlinkSqlId;

    @Schema(description = "artifact flink-cdc id")
    private Long artifactFlinkCDCId;

    @Schema(description = "artifact seatunnel id")
    private Long artifactSeaTunnelId;

    @Schema(description = "remark")
    private String remark;
}
