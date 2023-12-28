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

package cn.sliew.scaleph.plugin.flink.cdc.connectors.starrocks.sink;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public enum StarRocksSinkProperties {
    ;

    public static final PropertyDescriptor<String> JDBC_URL = new PropertyDescriptor.Builder()
            .name("jdbc-url")
            .description("The address that is used to connect to the MySQL server of the FE")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> LOAD_URL = new PropertyDescriptor.Builder()
            .name("load-url")
            .description("The address that is used to connect to the HTTP server of the FE")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> USERNAME = new PropertyDescriptor.Builder()
            .name("username")
            .description("User name to use when connecting to the StarRocks database.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PASSWORD = new PropertyDescriptor.Builder()
            .name("password")
            .description("Password for Doris cluster")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SINK_LABEL_PREFIX = new PropertyDescriptor.Builder()
            .name("sink.label-prefix")
            .description("The label prefix used by Stream Load.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SINK_CONNECT_TIMEOUT_MS = new PropertyDescriptor.Builder()
            .name("sink.connect.timeout-ms")
            .description("The timeout for establishing HTTP connection")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .defaultValue("30000")
            .validateAndBuild();

    public static final PropertyDescriptor<String> SINK_WAIT_FOR_CONTINUE_TIMEOUT_MS = new PropertyDescriptor.Builder()
            .name("sink.wait-for-continue.timeout-ms")
            .description("Timeout in millisecond to wait for 100-continue response from FE http server")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .defaultValue("30000")
            .validateAndBuild();

    public static final PropertyDescriptor<Long> SINK_BUFFER_FLUSH_MAX_BYTES = new PropertyDescriptor.Builder()
            .name("sink.buffer-flush.max-bytes")
            .description("The maximum size of data that can be accumulated in memory before being sent to StarRocks at a time")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .defaultValue(157286400)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> SINK_BUFFER_FLUSH_INTERVAL_MS = new PropertyDescriptor.Builder()
            .name("sink.buffer-flush.interval-ms")
            .description("The interval at which data is flushed for each table")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .defaultValue(300000)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> SINK_SCAN_FREQUENCY_MS = new PropertyDescriptor.Builder()
            .name("sink.scan-frequency.ms")
            .description("Scan frequency in milliseconds to check whether the buffered data for a table should be flushed because of reaching the flush interval")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .defaultValue(50)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SINK_IO_THREAD_COUNT = new PropertyDescriptor.Builder()
            .name("sink.io.thread-count")
            .description("Number of threads used for concurrent stream loads among different tables.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .defaultValue(2)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> SINK_AT_LEAST_ONCE_USE_TRANSACTION_STREAM_LOAD = new PropertyDescriptor.Builder()
            .name("sink.at-least-once.use-transaction-stream-load")
            .description("Whether to use transaction stream load for at-least-once when it's available.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .defaultValue(true)
            .validateAndBuild();

    public static final PropertyDescriptor<ObjectNode> SINK_PROPERTIES = new PropertyDescriptor.Builder()
            .name("sink.properties")
            .description("Parameters of StreamLoad.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> TABLE_CREATE_NUM_BUCKETS = new PropertyDescriptor.Builder()
            .name("table.create.num-buckets")
            .description("Number of buckets when creating a StarRocks table automatically")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TABLE_SCHEMA_CHANGE_TIMEOUT = new PropertyDescriptor.Builder()
            .name("table.schema-change.timeout")
            .description("Timeout for a schema change on StarRocks side, and must be an integral multiple of seconds.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .defaultValue("30min")
            .validateAndBuild();

    public static final PropertyDescriptor<ObjectNode> TABLE_CREATE_PROPERTIES = new PropertyDescriptor.Builder()
            .name("table.create.properties")
            .description("Create the Properties configuration of the table")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
