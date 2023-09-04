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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.rocketmq.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

import java.util.List;

public enum RocketMQSinkProperties {
    ;

    public static final PropertyDescriptor<String> TOPIC = new PropertyDescriptor.Builder()
            .name("topic")
            .description("RocketMq topic name. ")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PRODUCER_GROUP = new PropertyDescriptor.Builder()
            .name("producer.group")
            .description("RocketMq producer group id.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .defaultValue("SeaTunnel-producer-Group")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SEMANTIC = new PropertyDescriptor.Builder()
            .name("semantic")
            .description("Semantics that can be chosen EXACTLY_ONCE/AT_LEAST_ONCE/NON, default NON.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .allowableValues("EXACTLY_ONCE", "AT_LEAST_ONCE", "NON")
            .defaultValue("NON")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<List<String>> PARTITION_KEY_FIELDS = new PropertyDescriptor.Builder()
            .name("partition.key.fields")
            .description("Configure which fields are used as the key of the RocketMq message.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.STRING_ARRAY_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> EXACTLY_ONCE = new PropertyDescriptor.Builder()
            .name("exactly.once")
            .description("If true, the transaction message will be sent.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(false)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> SEND_SYNC = new PropertyDescriptor.Builder()
            .name("producer.send.sync")
            .description("If true, the message will be sync sent.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(false)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_MESSAGE_SIZE = new PropertyDescriptor.Builder()
            .name("max.message.size")
            .description("Maximum allowed message body size in bytes.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(1024 * 1024 * 4)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SEND_MESSAGE_TIMEOUT_MILLIS = new PropertyDescriptor.Builder()
            .name("send.message.timeout")
            .description("Timeout for sending messages.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(3000)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

}
