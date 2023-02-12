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

package cn.sliew.scaleph.engine.flink.kubernetes.service.dto;

import cn.sliew.scaleph.common.dict.flink.kubernetes.DeploymentKind;
import cn.sliew.scaleph.common.dto.BaseDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.JobManagerSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.JobSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.TaskManagerSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.service.vo.KubernetesOptionsVO;
import io.fabric8.kubernetes.api.model.Pod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode
@ApiModel(value = "WsFlinkKubernetesDeployment对象", description = "flink kubernetes deployment")
public class WsFlinkKubernetesDeploymentDTO extends BaseDTO {

    @ApiModelProperty("kind")
    private DeploymentKind kind;

    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("namespace")
    private String namespace;

    @ApiModelProperty("kubernetes options")
    private KubernetesOptionsVO kuberenetesOptions;

    @ApiModelProperty("job manager spec")
    private JobManagerSpec jobManager;

    @ApiModelProperty("task manager spec")
    private TaskManagerSpec taskManager;

    @ApiModelProperty("pod template")
    private Pod podTemplate;

    @ApiModelProperty("flink configuration")
    private Map<String, String> flinkConfiguration;

    @ApiModelProperty("deployment name for session job")
    private String deploymentName;

    @ApiModelProperty("job spec")
    private JobSpec job;

    @ApiModelProperty("remark")
    private String remark;

}
