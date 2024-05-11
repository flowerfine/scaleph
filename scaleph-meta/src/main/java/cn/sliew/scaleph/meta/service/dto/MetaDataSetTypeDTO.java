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

import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 元数据-参考数据类型
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "MetaDataSetType对象", description = "元数据-参考数据类型")
public class MetaDataSetTypeDTO extends BaseDTO {

    private static final long serialVersionUID = 263121616621915206L;

    @NotBlank
    @Length(min = 1, max = 32)
    @Pattern(regexp = "\\w+$")
    @Schema(description = "参考数据类型编码")
    private String dataSetTypeCode;

    @NotBlank
    @Length(max = 128)
    @Schema(description = "参考数据类型名称")
    private String dataSetTypeName;

    @Length(max = 256)
    @Schema(description = "备注")
    private String remark;

}
