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

package cn.sliew.scaleph.workspace.flink.cdc.service.dto;

import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCVersion;
import cn.sliew.scaleph.system.model.BaseDTO;
import cn.sliew.scaleph.workspace.project.service.dto.WsArtifactDTO;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "WsArtifactFlinkCDC对象", description = "artifact flink-cdc")
public class WsArtifactFlinkCDCDTO extends BaseDTO {

    @Schema(description = "作业artifact")
    private WsArtifactDTO artifact;

    @Schema(description = "flink版本")
    private FlinkVersion flinkVersion;

    @Schema(description = "flink cdc 版本")
    private FlinkCDCVersion flinkCDCVersion;

    @Schema(description = "全局并行度")
    private Integer parallelism;

    @Schema(description = "时区")
    private String localTimeZone;

    @Schema(description = "来源-数据源id")
    private Long fromDsId;

    @Schema(description = "来源-数据源配置")
    private JsonNode fromDsConfig;

    @Schema(description = "去向-数据源id")
    private Long toDsId;

    @Schema(description = "去向-数据源配置")
    private JsonNode toDsConfig;

    @Schema(description = "transform")
    private JsonNode transform;

    @Schema(description = "route")
    private JsonNode route;

    @Schema(description = "`current`")
    private YesOrNo current;
}
