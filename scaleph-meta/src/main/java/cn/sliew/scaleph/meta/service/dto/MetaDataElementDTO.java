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

import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.job.DataType;
import cn.sliew.scaleph.common.dto.BaseDTO;
import cn.sliew.scaleph.dao.entity.master.meta.MetaDataSetType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 元数据-数据元信息
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MetaDataElement对象", description = "元数据-数据元信息")
public class MetaDataElementDTO extends BaseDTO {

    private static final long serialVersionUID = 6216584991752649574L;

    @NotBlank
    @Length(min = 1, max = 30)
    @Pattern(regexp = "\\w+$")
    @ApiModelProperty(value = "数据元标识")
    private String elementCode;

    @NotBlank
    @Length(max = 250)
    @ApiModelProperty(value = "数据元名称")
    private String elementName;

    @NotNull
    @ApiModelProperty(value = "数据类型")
    private DataType dataType;

    @ApiModelProperty(value = "长度")
    private Long dataLength;

    @ApiModelProperty(value = "数据精度，有效位")
    private Integer dataPrecision;

    @ApiModelProperty(value = "小数位数")
    private Integer dataScale;

    @ApiModelProperty(value = "是否可以为空,1-是;0-否")
    private YesOrNo nullable;

    @ApiModelProperty(value = "默认值")
    private String dataDefault;

    @ApiModelProperty(value = "最小值")
    private String lowValue;

    @ApiModelProperty(value = "最大值")
    private String highValue;

    @ApiModelProperty(value = "参考数据类型")
    private MetaDataSetType dataSetType;

}
