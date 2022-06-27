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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.jdbc.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum JdbcSinkProperties {
    ;

    public static final PropertyDescriptor<String> QUERY = new PropertyDescriptor.Builder<String>()
            .name("query")
            .description("upsert statement")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> BATCH_SIZE = new PropertyDescriptor.Builder<String>()
            .name("batch_size")
            .description("The number of records writen per batch")
            .type(PropertyType.INT)
            .defaultValue(1024)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> PRE_SQL = new PropertyDescriptor.Builder<String>()
            .name("pre_sql")
            .description("This sql can be executed before output")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> POST_SQL = new PropertyDescriptor.Builder<String>()
            .name("post_sql")
            .description("This sql can be executed after output, and just supports for batch job")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .validateAndBuild();
}
