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
 *
 */

package cn.sliew.scaleph.engine.sql.gateway.services.dto.factory;

import cn.sliew.scaleph.common.dict.common.YesOrNo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.description.HtmlFormatter;

import java.lang.reflect.Field;

@Data
@EqualsAndHashCode
@Schema(name = "Flink Factory 初始化参数选项信息")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FactoryOptionDTO {

    @Schema(description = "参数名称")
    private String parameterName;
    @Schema(description = "参数类型")
    private String parameterType;
    @Schema(description = "是否是列表")
    private YesOrNo isList;
    @Schema(description = "参数是否含有默认值")
    private YesOrNo hasDefaultValue;
    @Schema(description = "默认值")
    private Object defaultValue;
    @Schema(description = "参数描述")
    private String description;

    public static FactoryOptionDTO from(ConfigOption<?> configOption) {
        FactoryOptionDTOBuilder builder = FactoryOptionDTO.builder();
        builder.parameterName(configOption.key());
        Class<? extends ConfigOption> configOptionClass = configOption.getClass();
        try {
            Field parameterClassField = configOptionClass.getDeclaredField("clazz");
            Class<?> parameterClass = (Class<?>) parameterClassField.get(configOption);
            builder.parameterType(parameterClass.getTypeName());
            Field isListField = configOptionClass.getDeclaredField("isList");
            isListField.setAccessible(true);
            boolean isList = (boolean) isListField.get(configOption);
            builder.isList(YesOrNo.ofBoolean(isList));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        builder.hasDefaultValue(YesOrNo.ofBoolean(configOption.hasDefaultValue()));
        builder.defaultValue(configOption.defaultValue());
        builder.description(new HtmlFormatter().format(configOption.description()));
        return builder.build();
    }
}
