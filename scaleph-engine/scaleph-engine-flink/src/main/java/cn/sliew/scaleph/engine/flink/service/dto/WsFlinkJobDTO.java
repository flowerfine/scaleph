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

import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "FlinkJob对象", description = "flink job")
public class WsFlinkJobDTO extends BaseDTO {

    @NotNull
    @Schema(description = "项目ID")
    private Long projectId;

    @NotNull
    @Schema(description = "job type. 0: jar, 1: sql+udf, 2: seatunnel")
    private FlinkJobType type;

    @Schema(description = "job code")
    private Long code;

    @Schema(description = "job name")
    private String name;

    @NotNull
    @Schema(description = "job artifact id")
    private Long flinkArtifactId;

    @Schema(description = "job config")
    private Map<String, String> jobConfig;

    @Schema(description = "flink job instance")
    private WsFlinkJobInstanceDTO wsFlinkJobInstance;

    @Schema(description = "flink cluster config")
    private WsFlinkClusterConfigDTO wsFlinkClusterConfig;

    @NotNull
    @Schema(description = "flink cluster instance")
    private WsFlinkClusterInstanceDTO wsFlinkClusterInstance;

    @Schema(description = "flink config")
    private Map<String, String> flinkConfig;

    @Schema(description = "jars")
    private List<Long> jars;
}
