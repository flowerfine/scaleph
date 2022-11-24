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
import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FlinkJob对象", description = "flink job")
public class FlinkJobDTO extends BaseDTO {

    @NotNull
    @ApiModelProperty("job type. 0: jar, 1: sql+udf, 2: seatunnel")
    private FlinkJobType type;

    @ApiModelProperty("job code")
    private Long code;

    @ApiModelProperty("job name")
    private String name;

    @NotNull
    @ApiModelProperty("job artifact id")
    private Long flinkArtifactId;

    @ApiModelProperty("job config")
    private Map<String, String> jobConfig;

    @ApiModelProperty("flink cluster config id")
    private Long flinkClusterConfigId;

    @NotNull
    @ApiModelProperty("flink cluster instance id")
    private Long flinkClusterInstanceId;

    @ApiModelProperty("flink config")
    private Map<String, String> flinkConfig;

    @ApiModelProperty("jars")
    private List<Long> jars;

    @ApiModelProperty("job from version")
    private Long fromVersion;

    @ApiModelProperty("job version")
    private Long version;

    @ApiModelProperty("job remark")
    private String remark;
}
