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

import cn.sliew.scaleph.common.dto.BaseDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FlinkJobConfig对象", description = "flink job config")
public class FlinkJobConfigDTO extends BaseDTO {

    @ApiModelProperty("类型。0: artifact, 1: sql+udf")
    private DictVO type;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("flink 集群配置 ID")
    private Long flinkClusterConfigId;

    @ApiModelProperty("任务自身 配置参数")
    private Map<String, String> jobConfig;

    @ApiModelProperty("flink 配置参数")
    private Map<String, String> flinkConfig;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty("备注")
    private String remark;
}
