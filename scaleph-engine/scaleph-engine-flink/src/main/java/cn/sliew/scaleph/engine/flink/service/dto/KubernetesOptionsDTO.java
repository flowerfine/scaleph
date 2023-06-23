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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "KubernetesOptions", description = "Kubernetes Options")
public class KubernetesOptionsDTO {

    @Schema(description = "Kubeconfig context")
    private String context;

    @Schema(description = "kubernetes namepace")
    private String namespace;

    @Schema(description = "kubernetes service account")
    private String serviceAccount;

    @Schema(description = "image registry")
    private String registry;

    @Schema(description = "image repository")
    private String repository;

    @Schema(description = "image")
    private String image;

    @Schema(description = "image pull policy")
    private String imagePullPolicy;

    @Schema(description = "kubernetes JobManager pod cpu")
    private Double jobManagerCPU;

    @Schema(description = "kubernetes JobManager memory")
    private String jobManagerMemory;

    @Schema(description = "kubernetes JobManager replica")
    private Integer jobManagerReplicas;

    @Schema(description = "kubernetes TaskManager pod")
    private Double taskManagerCPU;

    @Schema(description = "kubernetes TaskManager memory")
    private String taskManagerMemory;

    @Schema(description = "kubernetes TaskManager replica")
    private Integer taskManagerReplicas;
}
