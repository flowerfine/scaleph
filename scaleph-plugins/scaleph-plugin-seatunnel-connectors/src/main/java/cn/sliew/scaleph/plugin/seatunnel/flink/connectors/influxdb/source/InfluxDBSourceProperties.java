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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.influxdb.source;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum InfluxDBSourceProperties {
    ;

    public static final PropertyDescriptor<String> DATABASE = new PropertyDescriptor.Builder<String>()
            .name("database")
            .description("The influxDB database")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SQL = new PropertyDescriptor.Builder<String>()
            .name("sql")
            .description("The query sql used to search data")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SPLIT_COLUMN = new PropertyDescriptor.Builder<String>()
            .name("split_column")
            .description("the split_column of the influxDB when you select")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> LOWER_BOUND = new PropertyDescriptor.Builder<Long>()
            .name("lower_bound")
            .description("lower bound of the split_column column")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> UPPER_BOUND = new PropertyDescriptor.Builder<Long>()
            .name("upper_bound")
            .description("upper bound of the split_column column")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> PARTITION_NUM = new PropertyDescriptor.Builder<Integer>()
            .name("partition_num")
            .description("the partition_num of the InfluxDB when you select")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> EPOCH = new PropertyDescriptor.Builder<String>()
            .name("epoch")
            .description("returned time precision")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .defaultValue("n")
            .allowableValues("H", "m", "s", "MS", "u", "n")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
