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

package cn.sliew.scaleph.meta.service.dto;

import javax.validation.constraints.NotBlank;
import java.util.Map;

import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 元数据-数据源信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MetaDatasource对象", description = "元数据-数据源信息")
public class MetaDatasourceDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "数据源支持的属性")
    private Map<String, Object> props;

    @ApiModelProperty(value = "数据源支持的额外属性。")
    private Map<String, Object> additionalProps;

    @ApiModelProperty(value = "备注描述")
    private String remark;

}
