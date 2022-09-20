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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.jdbc;

import cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties;
import cn.sliew.scaleph.plugin.framework.property.*;

public enum JdbcProperties {
    ;

    public static final PropertyDescriptor<String> URL = new PropertyDescriptor.Builder()
            .name("url")
            .description("jdbc url")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .fallbackProperty(JdbcPoolProperties.JDBC_URL)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DRIVER = new PropertyDescriptor.Builder()
            .name("driver")
            .description("jdbc class name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .fallbackProperty(JdbcPoolProperties.DRIVER_CLASS_NAME)
            .validateAndBuild();

    public static final PropertyDescriptor<String> USER = new PropertyDescriptor.Builder()
            .name("user")
            .description("jdbc username")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .fallbackProperty(JdbcPoolProperties.USERNAME)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PASSWORD = new PropertyDescriptor.Builder()
            .name("password")
            .description("jdbc password")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .fallbackProperty(JdbcPoolProperties.PASSWORD)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> CONNECTION_CHECK_TIMEOUT_SEC = new PropertyDescriptor.Builder<String>()
            .name("connection_check_timeout_sec")
            .description("The time in seconds to wait for the database operation used to validate the connection to complete.")
            .type(PropertyType.STRING)
            .defaultValue(30)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();
}
