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

import javax.validation.constraints.NotBlank;

import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 数据集成-项目目录
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "目录信息", description = "数据集成-项目目录")
public class DiDirectoryDTO extends BaseDTO {

    private static final long serialVersionUID = -5411947916873915670L;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @NotBlank
    @Length(min = 1, max = 32)
    @ApiModelProperty(value = "目录名称")
    private String directoryName;

    @ApiModelProperty(value = "上级目录id")
    private Long pid;

    @ApiModelProperty(value = "路径")
    private String fullPath;

}
