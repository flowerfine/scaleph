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

import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据集成-作业步骤参数类型信息
 * </p>
 *
 * @author liyu
 * @since 2022-03-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "作业步骤参数类型", description = "数据集成-作业步骤参数类型信息")
public class DiJobStepAttrTypeDTO extends BaseDTO {

    private static final long serialVersionUID = 153877584223991187L;

    @ApiModelProperty(value = "步骤类型")
    private String stepType;

    @ApiModelProperty(value = "步骤名称")
    private String stepName;

    @ApiModelProperty(value = "步骤参数key")
    private String stepAttrKey;

    @ApiModelProperty(value = "步骤参数默认值")
    private String stepAttrDefaultValue;

    @ApiModelProperty(value = "是否需要")
    private String isRequired;

    @ApiModelProperty(value = "步骤参数描述")
    private String stepAttrDescribe;


}
