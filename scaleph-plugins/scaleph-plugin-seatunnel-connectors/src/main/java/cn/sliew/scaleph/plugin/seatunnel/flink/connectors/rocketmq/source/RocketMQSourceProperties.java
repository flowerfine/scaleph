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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.rocketmq.source;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum RocketMQSourceProperties {
    ;

    public static final PropertyDescriptor<String> TOPICS = new PropertyDescriptor.Builder()
            .name("topics")
            .description("RocketMQ topic name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> KEY_PARTITION_DISCOVERY_INTERVAL_MILLIS = new PropertyDescriptor.Builder()
            .name("partition.discovery.interval.millis")
            .description("The interval for dynamically discovering topics and partitions.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .defaultValue(-1L)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CONSUMER_GROUP = new PropertyDescriptor.Builder()
            .name("consumer.group")
            .description("RocketMQ consumer group id")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .defaultValue("SeaTunnel-Consumer-Group")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> COMMIT_ON_CHECKPOINT = new PropertyDescriptor.Builder()
            .name("commit.on.checkpoint")
            .description("If true, the consumer's offset will be stored in the background periodically.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(true)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> SCHEMA = new PropertyDescriptor.Builder()
            .name("schema")
            .description("The structure of the data, including field names and field types.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> START_MODE = new PropertyDescriptor.Builder()
            .name("start.mode")
            .description(
                    "The initial consumption pattern of consumers,there are several types:\n"
                            + "[CONSUME_FROM_LAST_OFFSET],[CONSUME_FROM_FIRST_OFFSET],[CONSUME_FROM_GROUP_OFFSETS],[CONSUME_FROM_TIMESTAMP],[CONSUME_FROM_SPECIFIC_OFFSETS]")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .allowableValues("CONSUME_FROM_LAST_OFFSET", "CONSUME_FROM_FIRST_OFFSET", "CONSUME_FROM_GROUP_OFFSETS", "CONSUME_FROM_TIMESTAMP", "CONSUME_FROM_SPECIFIC_OFFSETS")
            .defaultValue("CONSUME_FROM_GROUP_OFFSETS")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> START_MODE_TIMESTAMP = new PropertyDescriptor.Builder()
            .name("start.mode.timestamp")
            .description("The time required for consumption mode to be timestamp.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> START_MODE_OFFSETS = new PropertyDescriptor.Builder()
            .name("start.mode.offsets")
            .description("The offset required for consumption mode to be specific offsets.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> BATCH_SIZE = new PropertyDescriptor.Builder()
            .name("batch.size")
            .description("Rocketmq consumer pull batch size.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(100)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> POLL_TIMEOUT_MILLIS = new PropertyDescriptor.Builder()
            .name("consumer.poll.timeout.millis")
            .description("The poll timeout in milliseconds.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .defaultValue(5000L)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

}
