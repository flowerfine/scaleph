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

package cn.sliew.scaleph.engine.flink.kubernetes.service.param;

import cn.sliew.scaleph.common.dict.flink.FlinkDeploymentMode;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.FlinkRuntimeExecutionMode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class WsFlinkKubernetesJobAddParam {

    @NotNull
    @ApiModelProperty("project id")
    private Long projectId;

    @NotBlank
    @ApiModelProperty("name")
    private String name;

    @NotNull
    @ApiModelProperty("execution mode")
    private FlinkRuntimeExecutionMode executionMode;

    @NotNull
    @ApiModelProperty("deployment mode")
    private FlinkDeploymentMode flinkDeploymentMode;

    @ApiModelProperty("flink deployment id")
    private Long flinkDeploymentId;

    @ApiModelProperty("flink session cluster id")
    private Long flinkSessionClusterId;

    @NotNull
    @ApiModelProperty("flink job type")
    private FlinkJobType type;

    @ApiModelProperty("flink artifact jar id")
    private Long flinkArtifactJarId;

    @ApiModelProperty("flink artifact sql id")
    private Long flinkArtifactSqlId;

    @ApiModelProperty("remark")
    private String remark;
}
