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

import cn.sliew.scaleph.common.dict.flink.FlinkClusterStatus;
import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * flink 集群实例
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FlinkClusterConfig对象", description = "flink 集群配置")
public class FlinkClusterInstanceDTO extends BaseDTO {

    @NotNull
    @ApiModelProperty("集群配置")
    private Long flinkClusterConfigId;

    @NotBlank
    @ApiModelProperty("集群名称")
    private String name;

    @NotBlank
    @ApiModelProperty("集群id")
    private String clusterId;

    @NotBlank
    @ApiModelProperty("集群 web-ui 链接")
    private String webInterfaceUrl;

    @NotNull
    @ApiModelProperty("集群状态。0: 已创建, 1: 运行中, 2: 停止")
    private FlinkClusterStatus status;

    @ApiModelProperty("备注")
    private String remark;

}
