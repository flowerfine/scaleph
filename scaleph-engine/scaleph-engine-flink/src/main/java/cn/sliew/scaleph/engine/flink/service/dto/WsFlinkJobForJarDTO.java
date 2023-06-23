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

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "FlinkJobForJar对象", description = "flink job for jar")
public class WsFlinkJobForJarDTO extends BaseDTO {

    @Schema(description = "job type. 0: jar, 1: sql+udf, 2: seatunnel")
    private FlinkJobType type;

    @Schema(description = "job code")
    private Long code;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "flink artifact jar")
    private WsFlinkArtifactJarDTO flinkArtifactJar;

    @Schema(description = "任务自身 配置参数")
    private Map<String, String> jobConfig;

    @Schema(description = "flink cluster config")
    private WsFlinkClusterConfigDTO flinkClusterConfig;

    @Schema(description = "flink cluster instance")
    private WsFlinkClusterInstanceDTO flinkClusterInstance;

    @Schema(description = "flink 配置参数")
    private Map<String, String> flinkConfig;

    @Schema(description = "flink job instance")
    private WsFlinkJobInstanceDTO flinkJobInstance;

    @Schema(description = "jars")
    private List<Long> jars;

    @Schema(description = "job from version")
    private Long fromVersion;

    @Schema(description = "job version")
    private Long version;

    @Schema(description = "备注")
    private String remark;
}
