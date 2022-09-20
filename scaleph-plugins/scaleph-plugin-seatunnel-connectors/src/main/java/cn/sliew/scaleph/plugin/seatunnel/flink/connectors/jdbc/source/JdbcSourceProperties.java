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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.jdbc.source;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum JdbcSourceProperties {
    ;

    public static final PropertyDescriptor<String> QUERY = new PropertyDescriptor.Builder<String>()
            .name("query")
            .description("query statement")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PARTITION_COLUMN = new PropertyDescriptor.Builder<String>()
            .name("partition_column")
            .description("The column name for parallelism's partition, only support numeric type.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> PARTITION_UPPER_BOUND = new PropertyDescriptor.Builder<String>()
            .name("partition_upper_bound")
            .description(
                    "The partition_column max value for scan, if not set SeaTunnel will query database get max value.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> PARTITION_LOWER_BOUND = new PropertyDescriptor.Builder<String>()
            .name("partition_lower_bound")
            .description(
                    "The partition_column min value for scan, if not set SeaTunnel will query database get min value.")
            .type(PropertyType.STRING)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();
}
