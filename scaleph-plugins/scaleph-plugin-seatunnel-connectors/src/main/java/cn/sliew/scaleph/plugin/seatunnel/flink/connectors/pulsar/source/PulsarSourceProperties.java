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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.pulsar.source;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum PulsarSourceProperties {
    ;

    public static final PropertyDescriptor<String> CLIENT_SERVICE_URL = new PropertyDescriptor.Builder()
            .name("client.service-url")
            .description("Service URL provider for Pulsar service.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> ADMIN_SERVICE_URL = new PropertyDescriptor.Builder()
            .name("admin.service-url")
            .description("The Pulsar service HTTP URL for the admin endpoint.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> AUTH_PLUGIN_CLASS = new PropertyDescriptor.Builder()
            .name("auth.plugin-class")
            .description("Name of the authentication plugin.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> AUTH_PARAMS = new PropertyDescriptor.Builder()
            .name("auth.params")
            .description("Parameters for the authentication plugin.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SUBSCRIPTION_NAME = new PropertyDescriptor.Builder()
            .name("subscription.name")
            .description("Specify the subscription name for this consumer.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TOPIC = new PropertyDescriptor.Builder()
            .name("topic")
            .description("Topic name(s) to read data from when the table is used as source. It also supports topic list for source by separating topic by semicolon")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TOPIC_PATTERN = new PropertyDescriptor.Builder()
            .name("topic-pattern")
            .description("The regular expression for a pattern of topic names to read from.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> TOPIC_DISCOVERY_INTERVAL = new PropertyDescriptor.Builder()
            .name("topic-discovery.interval")
            .description("The interval (in ms) for the Pulsar source to discover the new topic partitions.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> POLL_TIMEOUT = new PropertyDescriptor.Builder()
            .name("poll.timeout")
            .description("The maximum time (in ms) to wait when fetching records.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> POLL_INTERVAL = new PropertyDescriptor.Builder()
            .name("poll.interval")
            .description("The interval time(in ms) when fetcing records.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> POLL_BATCH_SIZE = new PropertyDescriptor.Builder()
            .name("poll.batch.size")
            .description("The maximum number of records to fetch to wait when polling")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CURSOR_STARTUP_MODE = new PropertyDescriptor.Builder()
            .name("cursor.startup.mode")
            .description("Startup mode for Pulsar consumer")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .allowableValues("EARLIEST", "LATEST", "SUBSCRIPTION", "TIMESTAMP")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CURSOR_STARTUP_TIMESTAMP = new PropertyDescriptor.Builder()
            .name("cursor.startup.timestamp")
            .description("Start from the specified epoch timestamp (in milliseconds).")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CURSOR_RESET_MODE = new PropertyDescriptor.Builder()
            .name("cursor.reset.mode")
            .description("Cursor reset strategy for Pulsar consumer")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .allowableValues("EARLIEST", "LATEST")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CURSOR_STOP_MODE = new PropertyDescriptor.Builder()
            .name("cursor.stop.mode")
            .description("Stop mode for Pulsar consumer")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .allowableValues("NEVER", "LATEST", "TIMESTAMP")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CURSOR_STOP_TIMESTAMP = new PropertyDescriptor.Builder()
            .name("cursor.stop.timestamp")
            .description("Stop from the specified epoch timestamp (in milliseconds).")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> SCHEMA = new PropertyDescriptor.Builder()
            .name("schema")
            .description("The schema information of upstream data.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
