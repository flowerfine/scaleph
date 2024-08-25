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

import cn.sliew.carp.framework.dag.service.dto.DagConfigComplexDTO;
import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelEngineType;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelVersion;
import cn.sliew.scaleph.system.model.BaseDTO;
import cn.sliew.scaleph.workspace.project.service.dto.WsArtifactDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "WsArtifactSeaTunnel", description = "artifact seatunnel")
public class WsArtifactSeaTunnelDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "作业artifact")
    private WsArtifactDTO artifact;

    @Schema(description = "seatunnel 引擎")
    private SeaTunnelEngineType seaTunnelEngine;

    @Schema(description = "flink 版本")
    private FlinkVersion flinkVersion;

    @Schema(description = "seatunnel 版本")
    private SeaTunnelVersion seaTunnelVersion;

    @Schema(description = "dag id")
    private Long dagId;

    @Schema(description = "dag")
    private DagConfigComplexDTO dag;

    @Schema(description = "current artifact")
    private YesOrNo current;
}
