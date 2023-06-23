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

import cn.sliew.scaleph.common.dict.flink.FlinkDeploymentMode;
import cn.sliew.scaleph.common.dict.flink.FlinkResourceProvider;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import cn.sliew.scaleph.resource.service.dto.FlinkReleaseDTO;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * <p>
 * flink 集群配置
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "FlinkClusterConfig对象", description = "flink 集群配置")
public class WsFlinkClusterConfigDTO extends BaseDTO {

    @NotNull
    @Schema(description = "项目id")
    private Long projectId;

    @NotBlank
    @Schema(description = "名称")
    private String name;

    @Schema(description = "集群版本")
    private FlinkVersion flinkVersion;

    @NotNull
    @Schema(description = "Resource。0: Standalone, 1: Native Kubernetes, 2: YARN")
    private FlinkResourceProvider resourceProvider;

    @NotNull
    @Schema(description = "flink 部署模式。0: Application, 1: Per-Job, 2: Session")
    private FlinkDeploymentMode deployMode;

    @NotNull
    @Schema(description = "release id")
    private FlinkReleaseDTO flinkRelease;

    @Schema(description = "集群凭证 id。如 hadoop 的 core-site.xml，kubernetes 的 kubeconfig")
    private ClusterCredentialDTO clusterCredential;

    @Schema(description = "kubernetes 配置")
    private KubernetesOptionsDTO kubernetesOptionsDTO;

    @Schema(description = "flink 集群配置项")
    private Map<String, String> configOptions;

    @Schema(description = "备注")
    private String remark;
}
