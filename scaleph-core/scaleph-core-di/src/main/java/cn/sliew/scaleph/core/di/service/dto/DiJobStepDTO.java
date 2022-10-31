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

package cn.sliew.scaleph.core.di.service.dto;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginName;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginType;
import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * <p>
 * 数据集成-作业步骤信息
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "作业步骤信息", description = "数据集成-作业步骤信息")
public class DiJobStepDTO extends BaseDTO {

    private static final long serialVersionUID = -5718957095121629912L;

    @NotNull
    @ApiModelProperty(value = "作业id")
    private Long jobId;

    @NotNull
    @Length(max = 36)
    @ApiModelProperty(value = "步骤编码")
    private String stepCode;

    @NotNull
    @Length(min = 1, max = 120)
    @ApiModelProperty(value = "步骤标题")
    private String stepTitle;

    @NotNull
    @ApiModelProperty(value = "步骤类型")
    private SeaTunnelPluginType stepType;

    @NotNull
    @Length(min = 1, max = 120)
    @ApiModelProperty(value = "步骤名称")
    private SeaTunnelPluginName stepName;

    @ApiModelProperty(value = "x坐标")
    private Integer positionX;

    @ApiModelProperty(value = "y坐标")
    private Integer positionY;

    @ApiModelProperty(value = "作业步骤属性")
    private Map<String, Object> stepAttrs;
}
