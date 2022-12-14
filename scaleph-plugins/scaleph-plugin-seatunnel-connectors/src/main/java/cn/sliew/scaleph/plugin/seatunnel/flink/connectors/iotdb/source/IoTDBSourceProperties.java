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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.iotdb.source;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum IoTDBSourceProperties {
    ;

    public static final PropertyDescriptor<String> SQL = new PropertyDescriptor.Builder()
            .name("sql")
            .description("sql")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> FIELDS = new PropertyDescriptor.Builder()
            .name("fields")
            .description("fields")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> FETCH_SIZE = new PropertyDescriptor.Builder()
            .name("fetch_size")
            .description("the fetch_size of the IoTDB when you select")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> THRIFT_DEFAULT_BUFFER_SIZE = new PropertyDescriptor.Builder()
            .name("thrift_default_buffer_size")
            .description("the thrift_default_buffer_size of the IoTDB when you selec")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> THRIFT_MAX_FRAME_SIZE = new PropertyDescriptor.Builder()
            .name("thrift_max_frame_size")
            .description("thrift max frame size")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> ENABLE_CACHE_LOADER = new PropertyDescriptor.Builder()
            .name("enable_cache_leader")
            .description("enable_cache_leader of the IoTDB when you select")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> VERSION = new PropertyDescriptor.Builder()
            .name("version")
            .description("Version represents the SQL semantic version used by the client")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .allowableValues("V_0_12", "V_0_13")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> NUM_PARTITIONS = new PropertyDescriptor.Builder()
            .name("num_partitions")
            .description("split num")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> LOWER_BOUND = new PropertyDescriptor.Builder()
            .name("lower_bound")
            .description("lower bound of the time column")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> UPPER_BOUND = new PropertyDescriptor.Builder()
            .name("upper_bound")
            .description("upper bound of the time column")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

}
