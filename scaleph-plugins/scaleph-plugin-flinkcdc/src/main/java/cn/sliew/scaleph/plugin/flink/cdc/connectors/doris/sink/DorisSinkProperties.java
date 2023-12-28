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

package cn.sliew.scaleph.plugin.flink.cdc.connectors.doris.sink;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public enum DorisSinkProperties {
    ;

    public static final PropertyDescriptor<String> FENODES = new PropertyDescriptor.Builder()
            .name("fenodes")
            .description("Http address of Doris cluster FE.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<String> BENODES = new PropertyDescriptor.Builder()
            .name("benodes")
            .description("Http address of Doris cluster BE")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> JDBC_URL = new PropertyDescriptor.Builder()
            .name("jdbc-url")
            .description("JDBC address of Doris cluster")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> USERNAME = new PropertyDescriptor.Builder()
            .name("username")
            .description("Username of Doris cluster")
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
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> AUTO_REDIRECT = new PropertyDescriptor.Builder()
            .name("auto-redirect")
            .description("Whether to write through FE redirection and directly connect to BE to write")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .defaultValue(false)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> SINK_ENABLE_BATCH_MODE = new PropertyDescriptor.Builder()
            .name("sink.enable.batch-mode")
            .description("Whether to use the batch method to write to Doris")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .defaultValue(true)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SINK_FLUSH_QUEUE_SIZE = new PropertyDescriptor.Builder()
            .name("sink.flush.queue-size")
            .description("Queue size for batch writing")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(2)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SINK_BUFFER_FLUSH_MAX_ROWS = new PropertyDescriptor.Builder()
            .name("sink.buffer-flush.max-rows")
            .description("Maximum number of Flush records in a single batch")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(50000)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SINK_BUFFER_FLUSH_MAX_BYTES = new PropertyDescriptor.Builder()
            .name("sink.buffer-flush.max-bytes")
            .description("Maximum number of bytes flushed in a single batch")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(10485760)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SINK_BUFFER_FLUSH_INTERVAL = new PropertyDescriptor.Builder()
            .name("sink.buffer-flush.interval")
            .description("Flush interval duration")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .defaultValue("10s")
            .validateAndBuild();

    public static final PropertyDescriptor<ObjectNode> SINK_PROPERTIES = new PropertyDescriptor.Builder()
            .name("sink.properties")
            .description("Parameters of StreamLoad.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<ObjectNode> TABLE_CREATE_PROPERTIES = new PropertyDescriptor.Builder()
            .name("table.create.properties")
            .description("Create the Properties configuration of the table")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
