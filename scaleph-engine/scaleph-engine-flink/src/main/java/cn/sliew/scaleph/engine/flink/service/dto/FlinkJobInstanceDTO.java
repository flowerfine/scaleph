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

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FlinkJobConfig对象", description = "flink job config")
public class FlinkJobInstanceDTO extends BaseDTO {

    @NotNull
    @ApiModelProperty("job type. 0: jar, 1: sql+udf, 2: seatunnel")
    private DictVO type;

    @NotNull
    @ApiModelProperty("flink 任务配置 ID")
    private Long flinkJobConfigId;

    @ApiModelProperty("flink 集群实例 ID")
    private Long flinkClusterInstanceId;

    @ApiModelProperty("flink 任务 ID")
    private String jobId;

    @ApiModelProperty("任务状态。0: 已创建, 1: 创建失败")
    private DictVO status;

    @ApiModelProperty("备注")
    private String remark;
}
