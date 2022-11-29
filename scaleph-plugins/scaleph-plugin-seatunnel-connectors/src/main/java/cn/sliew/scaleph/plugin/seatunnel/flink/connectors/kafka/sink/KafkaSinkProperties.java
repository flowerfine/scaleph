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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kafka.sink;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum KafkaSinkProperties {
    ;

    public static final PropertyDescriptor<String> SEMANTIC = new PropertyDescriptor.Builder<String>()
            .name("semantic")
            .description("Semantics that can be chosen EXACTLY_ONCE/AT_LEAST_ONCE/NON, default NON.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .defaultValue("NON")
            .allowableValues("EXACTLY_ONCE", "AT_LEAST_ONCE", "NON")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PARTITION_KEY = new PropertyDescriptor.Builder<String>()
            .name("partition_key")
            .description(
                    "Configure which field is used as the key of the kafka message.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> PARTITION = new PropertyDescriptor.Builder<Integer>()
            .name("partition")
            .description(
                    "We can specify the partition, all messages will be sent to this partition.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.NON_NEGATIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> ASSIGN_PARTITIONS = new PropertyDescriptor.Builder()
            .name("assign_partitions")
            .description("We can decide which partition to send based on the content of the message")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TRANSACTION_PREFIX = new PropertyDescriptor.Builder<String>()
            .name("transaction_prefix")
            .description("If semantic is specified as EXACTLY_ONCE, the producer will write all messages in a Kafka transaction")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
