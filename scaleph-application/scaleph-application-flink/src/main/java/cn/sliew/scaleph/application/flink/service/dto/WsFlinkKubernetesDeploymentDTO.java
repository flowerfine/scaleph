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

import cn.sliew.scaleph.application.flink.operator.spec.IngressSpec;
import cn.sliew.scaleph.application.flink.operator.spec.JobManagerSpec;
import cn.sliew.scaleph.application.flink.operator.spec.TaskManagerSpec;
import cn.sliew.scaleph.application.flink.service.vo.KubernetesOptionsVO;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.fabric8.kubernetes.api.model.Pod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode
@Schema(name = "WsFlinkKubernetesDeployment对象", description = "flink kubernetes deployment")
public class WsFlinkKubernetesDeploymentDTO extends BaseDTO {

    @Schema(description = "project id")
    private Long projectId;

    @Schema(description = "cluster credential id")
    private Long clusterCredentialId;

    @Schema(description = "name")
    private String name;

    @Schema(description = "deployment id")
    private String deploymentId;

    @Schema(description = "namespace")
    private String namespace;

    @Schema(description = "kubernetes options")
    private KubernetesOptionsVO kubernetesOptions;

    @Schema(description = "job manager spec")
    private JobManagerSpec jobManager;

    @Schema(description = "task manager spec")
    private TaskManagerSpec taskManager;

    @Schema(description = "pod template")
    private Pod podTemplate;

    @Schema(description = "flink configuration")
    private Map<String, String> flinkConfiguration;

    @Schema(description = "log configuration")
    private Map<String, String> logConfiguration;

    @Schema(description = "ingress spec")
    private IngressSpec ingress;

    @Schema(description = "additional dependencies")
    private List<Long> additionalDependencies;

    @Schema(description = "remark")
    private String remark;

}
