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

import cn.sliew.scaleph.common.dto.BaseDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import cn.sliew.scaleph.system.util.I18nUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Map;

/**
 * 元数据-数据源信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MetaDatasource对象", description = "元数据-数据源信息")
public class MetaDatasourceDTO extends BaseDTO {

    private static final long serialVersionUID = -4194301324161897144L;

    @NotBlank
    @Length(min = 1, max = 64)
    @Pattern(regexp = "\\w+$")
    @ApiModelProperty(value = "数据源名称")
    private String datasourceName;

    @NotNull
    @ApiModelProperty(value = "数据源类型")
    private DictVO datasourceType;

    @ApiModelProperty(value = "数据源支持的属性对象")
    private Map<String, Object> props;

    @ApiModelProperty(value = "数据源支持的属性字符串")
    private String propsStr;

    @ApiModelProperty(value = "数据源支持的额外属性对象")
    private Map<String, Object> additionalProps;

    @ApiModelProperty(value = "数据源支持的额外属性符串")
    private String additionalPropsStr;

    @Length(max = 250)
    @ApiModelProperty(value = "备注描述")
    private String remark;

    @ApiModelProperty("是否改变了密码")
    private Boolean passwdChanged;

    public void setPropsStr(String propsStr) {
        this.propsStr = propsStr;
    }

    public void setAdditionalPropsStr(String additionalPropsStr) {
        this.additionalPropsStr = additionalPropsStr;
    }

    public void setPropsStr(Map<String, Object> props) {
        if (!CollectionUtils.isEmpty(props)) {
            StringBuffer buffer = new StringBuffer();
            props.forEach((k, v) ->
                    buffer.append("<b>" + I18nUtil.get("datadev.datasource.props." + k) + "</b>")
                            .append(":")
                            .append(v)
                            .append("<br/>")
            );
            setPropsStr(buffer.toString());
        }
    }

    public void setAdditionalPropsStr(Map<String, Object> additionalProps) {
        if (!CollectionUtils.isEmpty(additionalProps)) {
            StringBuffer buffer = new StringBuffer();
            additionalProps.forEach((k, v) ->
                    buffer.append(k).append(":").append(v).append("<br/>")
            );
            setAdditionalPropsStr(buffer.toString());
        }
    }
}
