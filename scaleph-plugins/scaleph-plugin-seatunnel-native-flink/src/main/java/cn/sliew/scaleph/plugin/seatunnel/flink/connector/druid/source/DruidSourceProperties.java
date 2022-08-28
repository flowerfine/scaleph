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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.druid.source;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum DruidSourceProperties {
    ;

    public static final PropertyDescriptor<String> JDBC_URL = new PropertyDescriptor.Builder<String>()
            .name("jdbc_url")
            .description("Apache Druid jdbc url")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DATASOURCE_NAME = new PropertyDescriptor.Builder()
            .name("datasourceName")
            .description("Apache Druid datasource name")
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
            .fallbackProperty(DATASOURCE_NAME)
            .validateAndBuild();

    public static final PropertyDescriptor<String> START_DATE = new PropertyDescriptor.Builder<String>()
            .name("start_date")
            .description("The start date of DataSource, for example, '2016-06-27', '2016-06-27 00:00:00', etc.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> END_DATE = new PropertyDescriptor.Builder<String>()
            .name("end_date")
            .description("The end date of DataSource, for example, '2016-06-28', '2016-06-28 00:00:00', etc.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> COLUMNS = new PropertyDescriptor.Builder<String>()
            .name("columns")
            .description("columns to be queried from DataSource.")
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
