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

package cn.sliew.scaleph.workspace.seatunnel.service.dto;

import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelEngineType;
import cn.sliew.scaleph.workspace.project.service.dto.WsArtifactDTO;
import cn.sliew.scaleph.workspace.seatunnel.service.vo.DiJobAttrVO;
import cn.sliew.scaleph.workspace.seatunnel.service.vo.JobGraphVO;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 数据集成-作业信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "作业信息", description = "数据集成-作业信息")
public class WsDiJobDTO extends BaseDTO {

    private static final long serialVersionUID = -4161534628783250968L;

    @NotNull
    @Schema(description = "Flink Artifact")
    private WsArtifactDTO wsFlinkArtifact;

    @NotNull
    @Schema(description = "作业引擎")
    private SeaTunnelEngineType jobEngine;

    @Schema(description = "作业编码")
    private String jobId;

    @Schema(description = "dag id")
    private Long dagId;

    @Schema(description = "current version")
    private YesOrNo current;

    @Schema(description = "作业属性信息")
    private DiJobAttrVO jobAttrList;

    @Schema(description = "作业连线信息")
    private List<WsDiJobLinkDTO> jobLinkList;

    @Schema(description = "步骤信息")
    private List<WsDiJobStepDTO> jobStepList;

    @Schema(description = "作业图信息")
    private JobGraphVO jobGraph;
}
