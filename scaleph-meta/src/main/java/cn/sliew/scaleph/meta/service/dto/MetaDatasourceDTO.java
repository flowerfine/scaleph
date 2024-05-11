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

import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.util.I18nUtil;
import cn.sliew.scaleph.system.model.BaseDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 元数据-数据源信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "MetaDatasource对象", description = "元数据-数据源信息")
public class MetaDatasourceDTO extends BaseDTO {

    private static final long serialVersionUID = -4194301324161897144L;

    @NotBlank
    @Length(min = 1, max = 64)
    @Pattern(regexp = "\\w+$")
    @Schema(description = "数据源名称")
    private String datasourceName;

    @NotNull
    @Schema(description = "数据源类型")
    private DictVO datasourceType;

    @Schema(description = "数据源支持的属性对象")
    private Map<String, Object> props;

    @Schema(description = "数据源支持的属性字符串")
    private String propsStr;

    @Schema(description = "数据源支持的额外属性对象")
    private Map<String, Object> additionalProps;

    @Schema(description = "数据源支持的额外属性符串")
    private String additionalPropsStr;

    @Length(max = 250)
    @Schema(description = "备注描述")
    private String remark;

    @Schema(description = "是否改变了密码")
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
            props.forEach((k, v) -> {
                        if (!v.toString().startsWith(Constants.CODEC_STR_PREFIX)) {
                            buffer.append("<b>" + I18nUtil.get("datadev.datasource.props." + k) + "</b>")
                                    .append(":")
                                    .append(v)
                                    .append("<br/>");
                        }
                    }
            );
            setPropsStr(buffer.toString());
        }
    }

    public void setAdditionalPropsStr(Map<String, Object> additionalProps) {
        if (!CollectionUtils.isEmpty(additionalProps)) {
            StringBuffer buffer = new StringBuffer();
            additionalProps.forEach((k, v) ->
                    buffer.append(k).append(":").append(v).append("\n")
            );
            setAdditionalPropsStr(buffer.toString());
        }
    }
}
