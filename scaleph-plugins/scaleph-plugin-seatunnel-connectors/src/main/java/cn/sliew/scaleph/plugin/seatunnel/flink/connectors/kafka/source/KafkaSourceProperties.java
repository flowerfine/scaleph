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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kafka.source;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author lizu
 */
public enum KafkaSourceProperties {
    ;

    public static final PropertyDescriptor<Boolean> PATTERN = new PropertyDescriptor.Builder()
            .name("pattern")
            .description(
                    "If pattern is set to true,the regular expression for a pattern of topic names to read from. All topics in clients with names that match the specified regular expression will be subscribed by the consumer.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(false)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CONSUMER_GROUP = new PropertyDescriptor.Builder<String>()
            .name("consumer.group")
            .description("Kafka consumer group id, used to distinguish different consumer groups.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .defaultValue("SeaTunnel-Consumer-Group")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> COMMIT_ON_CHECKPOINT = new PropertyDescriptor.Builder<Boolean>()
            .name("commit_on_checkpoint")
            .description(
                    "If true the consumer's offset will be periodically committed in the background.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(true)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> SCHEMA = new PropertyDescriptor.Builder()
            .name("schema")
            .description("The schema information of upstream data.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FORMAT_ERROR_HANDLE_WAY = new PropertyDescriptor.Builder<String>()
            .name("format_error_handle_way")
            .description("The processing method of data format error.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .defaultValue("fail")
            .allowableValues("fail", "skip")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> START_MODE = new PropertyDescriptor.Builder<String>()
            .name("start_mode")
            .description("The initial consumption pattern of consumers")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .allowableValues("earliest", "group_offsets", "latest", "specific_offsets", "timestamp")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> START_MODE_TIMESTAMP = new PropertyDescriptor.Builder<Long>()
            .name("start_mode.timestamp")
            .description("The time required for consumption mode to be timestamp.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> START_MODE_OFFSETS = new PropertyDescriptor.Builder()
            .name("start_mode.offsets")
            .description("The offset required for consumption mode to be specific_offsets")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> PARTITION_DISCOVERY_INTERVAL_MILLIS = new PropertyDescriptor.Builder()
            .name("partition-discovery.interval-millis")
            .description("The interval for dynamically discovering topics and partitions.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();

}
