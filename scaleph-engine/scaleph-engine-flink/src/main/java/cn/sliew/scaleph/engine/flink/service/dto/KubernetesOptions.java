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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "KubernetesOptions", description = "Kubernetes Options")
public class KubernetesOptions {

    @ApiModelProperty("镜像注册中心")
    private String registry;

    @ApiModelProperty("镜像仓库")
    private String repository;

    @ApiModelProperty("镜像")
    private String image;

    @ApiModelProperty("kubernetes namepace")
    private String namespace;

    @ApiModelProperty("kubernetes JobManager pod cpu")
    private Double jobManagerCPU;

    @ApiModelProperty("kubernetes JobManager memory")
    private String jobManagerMemory;

    @ApiModelProperty("kubernetes JobManager replica")
    private Integer jobManagerReplicas;

    @ApiModelProperty("kubernetes TaskManager pod")
    private Double taskManagerCPU;

    @ApiModelProperty("kubernetes TaskManager memory")
    private String taskManagerMemory;

    @ApiModelProperty("kubernetes TaskManager replica")
    private Integer taskManagerReplicas;
}
