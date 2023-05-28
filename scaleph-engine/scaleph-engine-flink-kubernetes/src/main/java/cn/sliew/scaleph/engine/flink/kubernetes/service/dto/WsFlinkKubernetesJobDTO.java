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

import cn.sliew.scaleph.common.dict.flink.FlinkDeploymentMode;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.FlinkRuntimeExecutionMode;
import cn.sliew.scaleph.common.dto.BaseDTO;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactJar;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactSql;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * flink kubernetes job
 * </p>
 */
@Data
@ApiModel(value = "WsFlinkKubernetesJob对象", description = "flink kubernetes job")
public class WsFlinkKubernetesJobDTO extends BaseDTO {

    @ApiModelProperty("project id")
    private Long projectId;

    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("job id")
    private String jobId;

    @ApiModelProperty("flink execution mode")
    private FlinkRuntimeExecutionMode executionMode;

    @ApiModelProperty("flink deployment mode")
    private FlinkDeploymentMode flinkDeploymentMode;

    @ApiModelProperty("flink deployment")
    private WsFlinkKubernetesDeploymentDTO flinkDeployment;

    @ApiModelProperty("flink session cluster")
    private WsFlinkKubernetesSessionClusterDTO flinkSessionCluster;

    @ApiModelProperty("type")
    private FlinkJobType type;

    @ApiModelProperty("flink artifact jar")
    private WsFlinkArtifactJar flinkArtifactJar;

    @ApiModelProperty("flink artifact sql")
    private WsFlinkArtifactSql flinkArtifactSql;

    @ApiModelProperty("remark")
    private String remark;

}
