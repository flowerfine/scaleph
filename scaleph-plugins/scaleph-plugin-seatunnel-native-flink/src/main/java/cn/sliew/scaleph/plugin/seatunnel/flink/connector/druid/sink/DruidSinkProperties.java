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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.druid.sink;

import cn.sliew.scaleph.plugin.framework.property.*;
import cn.sliew.scaleph.plugin.seatunnel.flink.connector.druid.source.DruidSourceProperties;

public enum DruidSinkProperties {
    ;

    public static final PropertyDescriptor<String> COORDINATOR_URL = new PropertyDescriptor.Builder<String>()
            .name("coordinator_url")
            .description("Apache Druid Coordinator service url")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DATASOURCE = new PropertyDescriptor.Builder<String>()
            .name("datasource")
            .description("Apache Druid datasource name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .fallbackProperty(DruidSourceProperties.DATASOURCE_NAME)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TIMESTAMP_COLUMN = new PropertyDescriptor.Builder<String>()
            .name("timestamp_column")
            .description("Apache Druid timestamp column name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    /**
     * https://seatunnel.apache.org/docs/2.1.1/connector/sink/Druid#timestamp_format-string
     */
    public static final PropertyDescriptor<String> TIMESTAMP_FORMAT = new PropertyDescriptor.Builder<String>()
            .name("timestamp_format")
            .description("Apache Druid timestamp format")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TIMESTAMP_MISSING_VALUE = new PropertyDescriptor.Builder<String>()
            .name("timestamp_missing_value")
            .description("Apache Druid timestamp missing value working when input records that have a null or missing timestamp")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> PARALLELISM = new PropertyDescriptor.Builder()
            .name("parallelism")
            .description("The flink operator parallelism")
            .type(PropertyType.INT)
            .defaultValue(1)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

}
