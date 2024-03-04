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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.doris.sink;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.PropertyType;
import cn.sliew.scaleph.plugin.framework.property.Validators;
import com.fasterxml.jackson.databind.JsonNode;

public enum DorisSinkProperties {
    ;

    public static final PropertyDescriptor<String> SINK_LABEL_PREFIX = new PropertyDescriptor.Builder()
            .name("sink.label-prefix")
            .description("The prefix of Doris stream load label")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> SINK_ENABLE_2PC = new PropertyDescriptor.Builder()
            .name("sink.enable-2pc")
            .description("Whether to enable two-phase commit (2pc), the default is true, to ensure Exactly-Once semantics")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(true)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> SINK_ENABLE_DELETE = new PropertyDescriptor.Builder()
            .name("sink.enable-delete")
            .description("Whether to enable deletion.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(false)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SINK_CHECK_INTERVAL = new PropertyDescriptor.Builder()
            .name("sink.check-interval")
            .description("check exception with the interval while loading")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(10000)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SINK_MAX_RETRIES = new PropertyDescriptor.Builder()
            .name("sink.max-retries")
            .description("the max retry times if writing records to database failed")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(3)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SINK_BUFFER_SIZE = new PropertyDescriptor.Builder()
            .name("sink.buffer-size")
            .description("the buffer size to cache data for stream load.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(256 * 1024)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SINK_BUFFER_COUNT = new PropertyDescriptor.Builder()
            .name("sink.buffer-count")
            .description("the buffer count to cache data for stream load.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(3)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> NEEDS_UNSUPPORTED_TYPE_CASTING = new PropertyDescriptor.Builder()
            .name("needs_unsupported_type_casting")
            .description("Whether to enable the unsupported type casting, such as Decimal64 to Double")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(false)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SAVE_MODE_CREATE_TEMPLATE = new PropertyDescriptor.Builder()
            .name("save_mode_create_template")
            .description("the data save mode")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CUSTOM_SQL = new PropertyDescriptor.Builder()
            .name("custom_sql")
            .description("When data_save_mode selects CUSTOM_PROCESSING, you should fill in the CUSTOM_SQL parameter. This parameter usually fills in a SQL that can be executed. SQL will be executed before synchronization tasks.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> DORIS_CONFIG = new PropertyDescriptor.Builder()
            .name("doris.config")
            .description("The way to specify the parameter is to add the prefix sink.properties. to the original stream load parameter")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
