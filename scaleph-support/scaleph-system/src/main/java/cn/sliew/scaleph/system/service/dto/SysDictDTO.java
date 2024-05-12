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

package cn.sliew.scaleph.system.service.dto;

import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.dict.DictType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 数据字典表
 * </p>
 *
 * @author liyu
 * @since 2021-07-24
 */
@Data
@EqualsAndHashCode
@Schema(name = "数据字典信息", description = "数据字典表")
public class SysDictDTO {

    private static final long serialVersionUID = -4136245238746831595L;

    @NotNull
    @Schema(description = "字典类型")
    private DictType dictType;

    @NotBlank
    @Length(min = 1, max = 120)
    @Pattern(regexp = "[\\w.]+$")
    @Schema(description = "字典编码")
    private String value;

    @NotBlank
    @Length(min = 1, max = 100)
    @Schema(description = "字典值")
    private String label;

    @Length(max = 200)
    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否有效")
    private boolean valid;

    public String getKey() {
        return getDictType().getCode() + Constants.SEPARATOR + getValue();
    }
}
