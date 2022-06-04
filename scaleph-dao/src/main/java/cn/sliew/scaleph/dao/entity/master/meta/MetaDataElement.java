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

package cn.sliew.scaleph.dao.entity.master.meta;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@TableName(value = "meta_data_element", resultMap = "MetaDataElementMap")
@ApiModel(value = "MetaDataElement对象", description = "元数据-数据元信息")
public class MetaDataElement extends BaseDO {

    private static final long serialVersionUID = -4396296342497985370L;

    @ApiModelProperty(value = "数据元标识")
    private String elementCode;

    @ApiModelProperty(value = "数据元名称")
    private String elementName;

    @ApiModelProperty(value = "数据类型")
    private String dataType;

    @ApiModelProperty(value = "长度")
    private Long dataLength;

    @ApiModelProperty(value = "数据精度，有效位")
    private Integer dataPrecision;

    @ApiModelProperty(value = "小数位数")
    private Integer dataScale;

    @ApiModelProperty(value = "是否可以为空,1-是;0-否")
    private String nullable;

    @ApiModelProperty(value = "默认值")
    private String dataDefault;

    @ApiModelProperty(value = "最小值")
    private String lowValue;

    @ApiModelProperty(value = "最大值")
    private String highValue;

    @ApiModelProperty(value = "参考数据类型id")
    private Long dataSetTypeId;

    @TableField(exist = false)
    private MetaDataSetType dataSetType;
}
