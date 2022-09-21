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

import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "FlinkArtifactJar对象", description = "flink artifact jar")
public class FlinkArtifactJarDTO extends BaseDTO {

    @NotNull
    @ApiModelProperty("Flink Artifact")
    private FlinkArtifactDTO flinkArtifact;

    @NotBlank
    @ApiModelProperty("Jar 版本")
    private String version;

    @NotBlank
    @ApiModelProperty("flink 版本")
    private FlinkVersion flinkVersion;

    @NotBlank
    @ApiModelProperty("Entry Class")
    private String entryClass;

    @ApiModelProperty("Jar 文件名")
    private String fileName;

    @ApiModelProperty("Jar 存储路径")
    private String path;

}
