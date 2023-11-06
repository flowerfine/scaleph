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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.flink.table.factories.Factory;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode
@Schema(name = "Flink Factory 信息包装")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlinkFactoryDTO {

    @Schema(description = "Factory的唯一标识，Flink根据此标识来识别Factory")
    private String factoryIdentifier;
    @Schema(description = "Factory类型",
            allowableValues = {"CatalogFactory", "FormatFactory", "DynamicTableSourceFactory", "DynamicTableSinkFactory"})
    private FactoryType factoryType;
    @Schema(description = "Factory初始化时的必填选项")
    private Set<FactoryOptionDTO> requiredOptions;
    @Schema(description = "Factory初始化时的可选选项")
    private Set<FactoryOptionDTO> optionalOptions;

    public static <T extends Factory> FlinkFactoryDTO fromFactory(T factory) {
        FlinkFactoryDTOBuilder builder = FlinkFactoryDTO.builder();
        Class<? extends Factory> factoryClass = factory.getClass();
        Arrays.stream(FactoryType.values())
                .filter(factoryType ->
                        Arrays.stream(factoryType.getFactoryClasses())
                                .anyMatch(factoryTypeClass -> factoryTypeClass.isAssignableFrom(factoryClass))
                )
                .findAny()
                .ifPresent(builder::factoryType);
        Set<FactoryOptionDTO> requiredOptions = factory.requiredOptions()
                .stream()
                .map(FactoryOptionDTO::from)
                .collect(Collectors.toSet());
        builder.requiredOptions(requiredOptions);
        Set<FactoryOptionDTO> optionalOptions = factory.optionalOptions()
                .stream()
                .map(FactoryOptionDTO::from)
                .collect(Collectors.toSet());
        builder.optionalOptions(optionalOptions);
        return builder.build();
    }

}
