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

package cn.sliew.scaleph.dao.entity.master.flink;

import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.dao.entity.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * flink job
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FlinkJobForJar对象", description = "flink job for jar")
public class FlinkJobForJar extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("job type. 0: jar, 1: sql+udf, 2: seatunnel")
    private FlinkJobType type;

    @ApiModelProperty("job code")
    private Long code;

    @ApiModelProperty("job name")
    private String name;

    @ApiModelProperty("flink artifact ID")
    private FlinkArtifactJarVO flinkArtifactJar;

    @ApiModelProperty("job artifact config")
    private String jobConfig;

    @ApiModelProperty("flink cluster config ID")
    private FlinkClusterConfig flinkClusterConfig;

    @ApiModelProperty("flink cluster config ID")
    private FlinkClusterInstance flinkClusterInstance;

    @ApiModelProperty("flink config")
    private String flinkConfig;

    @ApiModelProperty("from version")
    private Long fromVersion;

    @ApiModelProperty("version")
    private Long version;

    @ApiModelProperty("remark")
    private String remark;
}
