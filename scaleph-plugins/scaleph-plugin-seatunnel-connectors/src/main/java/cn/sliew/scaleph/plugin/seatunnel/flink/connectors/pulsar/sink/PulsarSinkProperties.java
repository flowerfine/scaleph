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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.pulsar.sink;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum PulsarSinkProperties {
    ;

    public static final PropertyDescriptor<String> SEMANTICS = new PropertyDescriptor.Builder()
            .name("semantics")
            .description("Consistency semantics for writing to pulsar.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> TRANSACTION_TIMEOUT = new PropertyDescriptor.Builder()
            .name("transaction_timeout")
            .description("The transaction timeout is specified as 10 minutes by default.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> MESSAGE_ROUTING_MODE = new PropertyDescriptor.Builder()
            .name("message.routing.mode")
            .description("Default routing mode for messages to partition.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> PARTITION_KEY_FIELDS = new PropertyDescriptor.Builder()
            .name("partition_key_fields")
            .description("Configure which fields are used as the key of the pulsar message.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
