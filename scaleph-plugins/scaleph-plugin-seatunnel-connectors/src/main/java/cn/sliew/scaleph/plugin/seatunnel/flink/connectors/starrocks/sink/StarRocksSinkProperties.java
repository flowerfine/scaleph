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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.starrocks.sink;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum StarRocksSinkProperties {
    ;

    public static final PropertyDescriptor<String> BASE_URL = new PropertyDescriptor.Builder()
            .name("base-url")
            .description("The JDBC URL")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> LABEL_PREFIX = new PropertyDescriptor.Builder()
            .name("labelPrefix")
            .description("The prefix of StarRocks stream load label")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> BATCH_MAX_ROWS = new PropertyDescriptor.Builder()
            .name("batch_max_rows")
            .description("For batch writing, when the number of buffers reaches the number of batch_max_rows or the byte size of batch_max_bytes or the time reaches batch_interval_ms, the data will be flushed into the StarRocks")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> BATCH_MAX_BYTES = new PropertyDescriptor.Builder()
            .name("batch_max_bytes")
            .description("For batch writing, when the number of buffers reaches the number of batch_max_rows or the byte size of batch_max_bytes or the time reaches batch_interval_ms, the data will be flushed into the StarRocks")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> BATCH_INTERVAL_MS = new PropertyDescriptor.Builder()
            .name("batch_interval_ms")
            .description("For batch writing, when the number of buffers reaches the number of batch_max_rows or the byte size of batch_max_bytes or the time reaches batch_interval_ms, the data will be flushed into the StarRocks")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_RETRIES = new PropertyDescriptor.Builder()
            .name("max_retries")
            .description("The number of retries to flush failed")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RETRY_BACKOFF_MULTIPLIER_MS = new PropertyDescriptor.Builder()
            .name("retry_backoff_multiplier_ms")
            .description("Using as a multiplier for generating the next delay for backoff")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_RETRY_BACKOFF_MS = new PropertyDescriptor.Builder()
            .name("max_retry_backoff_ms")
            .description("The amount of time to wait before attempting to retry a request to Doris")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> ENABLE_UPSERT_DELETE = new PropertyDescriptor.Builder()
            .name("enable_upsert_delete")
            .description("Whether to enable upsert/delete, only supports PrimaryKey model.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SAVE_MODE_CREATE_TEMPLATE = new PropertyDescriptor.Builder()
            .name("save_mode_create_template")
            .description("We use templates to automatically create starrocks tables")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> STARROCKS_CONFIG = new PropertyDescriptor.Builder()
            .name("starrocks.config")
            .description("The parameter of the stream load")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> HTTP_SOCKET_TIMEOUT_MS = new PropertyDescriptor.Builder()
            .name("http_socket_timeout_ms")
            .description("Set http socket timeout, default is 3 minutes.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SCHEMA_SAVE_MODE = new PropertyDescriptor.Builder()
            .name("schema_save_mode")
            .description("Before the synchronous task is turned on, different treatment schemes are selected for the existing surface structure of the target side.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DATA_SAVE_MODE = new PropertyDescriptor.Builder()
            .name("data_save_mode")
            .description("Before the synchronous task is turned on, different processing schemes are selected for data existing data on the target side.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CUSTOM_SQL = new PropertyDescriptor.Builder()
            .name("custom_sql")
            .description("When data_save_mode selects CUSTOM_PROCESSING, you should fill in the CUSTOM_SQL parameter.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();


}
