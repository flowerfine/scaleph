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

import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.project.service.dto.WsFlinkArtifactDTO;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * flink artifact jar
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "FlinkArtifactJar对象", description = "flink artifact jar")
public class WsFlinkArtifactJarDTO extends BaseDTO {

    @NotNull
    @Schema(description = "Flink Artifact")
    private WsFlinkArtifactDTO wsFlinkArtifact;

    @NotNull
    @Schema(description = "flink 版本")
    private FlinkVersion flinkVersion;

    @NotBlank
    @Schema(description = "Entry Class")
    private String entryClass;

    @Schema(description = "Jar 文件名")
    private String fileName;

    @Schema(description = "Jar 存储路径")
    private String path;

    @Schema(description = "jar_params")
    private String jarParams;

    @Schema(description = "current version")
    private YesOrNo current;

}
