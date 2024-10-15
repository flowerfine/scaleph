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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue;

import cn.sliew.scaleph.plugin.framework.property.*;
import org.codehaus.jackson.JsonNode;

public enum ClickHouseProperties {
    ;

    public static final PropertyDescriptor<String> HOST = new PropertyDescriptor.Builder<String>()
            .name("host")
            .description(
                    "ClickHouse cluster address, the format is host:port , allowing multiple hosts to be specified. Such as \"host1:8123,host2:8123\"")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DATABASE = new PropertyDescriptor.Builder<String>()
            .name("database")
            .description("The ClickHouse database")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> USERNAME = new PropertyDescriptor.Builder<String>()
            .name("username")
            .description("ClickHouse user username")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PASSWORD = new PropertyDescriptor.Builder<String>()
            .name("password")
            .description("ClickHouse user password")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required, Property.Sensitive)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> CLICKHOUSE_CONFIG = new PropertyDescriptor.Builder()
            .name("clickhouse.config")
            .description(
                    "clickhouse.* The way to specify the parameter is to add the prefix clickhouse. to the original parameter name. For example, the way to specify socket_timeout is: clickhouse.socket_timeout = 50000 . "
                            + "If these non-essential parameters are not specified, they will use the default values given by clickhouse-jdbc.\n")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .validateAndBuild();

}
