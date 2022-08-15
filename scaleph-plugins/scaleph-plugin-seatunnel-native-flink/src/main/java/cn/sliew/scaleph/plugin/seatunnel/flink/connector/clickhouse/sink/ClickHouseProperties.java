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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.clickhouse.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum ClickHouseProperties {
    ;

    public static final PropertyDescriptor<String> HOST = new PropertyDescriptor.Builder<String>()
            .name("host")
            .description("host:port cluster address. Multiple hosts seperated by (,), such as \"host1:8123,host2:8123\"")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> USERNAME = new PropertyDescriptor.Builder<String>()
            .name("username")
            .description("This field is only required when permission is enabled in ClickHouse")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PASSWORD = new PropertyDescriptor.Builder<String>()
            .name("password")
            .description("This field is only required when the permission is enabled in ClickHouse")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DATABASE = new PropertyDescriptor.Builder<String>()
            .name("database")
            .description("database name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TABLE = new PropertyDescriptor.Builder<String>()
            .name("table")
            .description("table name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FIELDS = new PropertyDescriptor.Builder<String>()
            .name("fields")
            .description("The data field that needs to be output to ClickHouse , if not configured, it will be automatically adapted according to the data schema .")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();


    public static final PropertyDescriptor<String> CLICKHOUSE_CONF = new PropertyDescriptor.Builder<String>()
        .name("clickhouse_conf")
        .description("The clickhouse load parameters.you can use 'clickhouse.' prefix + load properties.")
        .type(PropertyType.STRING)
        .parser(Parsers.STRING_PARSER)
        .validateAndBuild();

}
