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

import cn.sliew.carp.framework.common.dict.DictInstance;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.flink.table.factories.Factory;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FactoryType implements DictInstance {

    CatalogFactory(
            "CatalogFactory",
            org.apache.flink.table.factories.CatalogFactory.class),
    FormatFactory("FormatFactory",
            org.apache.flink.table.factories.FormatFactory.class),
    DynamicTableSourceFactory("DynamicTableSourceFactory",
            org.apache.flink.table.factories.DynamicTableSourceFactory.class),
    DynamicTableSinkFactory("DynamicTableSinkFactory",
            org.apache.flink.table.factories.DynamicTableSinkFactory.class);

    private final String value;
    private final Class<? extends Factory>[] factoryClasses;

    FactoryType(String value, Class<? extends Factory>... factoryClasses) {
        this.value = value;
        this.factoryClasses = factoryClasses;
    }

    @JsonCreator
    public static FactoryType of(String value) {
        return Arrays.stream(values())
                .filter(instance -> instance.getValue().equals(value))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(FactoryType.class, value));
    }

    public Class<? extends Factory>[] getFactoryClasses() {
        return factoryClasses;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return value;
    }
}
