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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kafka;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum KafkaProperties {
    ;

    public static final PropertyDescriptor<String> BOOTSTRAP_SERVERS = new PropertyDescriptor.Builder<String>()
            .name("bootstrap.servers")
            .description("Kafka cluster address, separated by \",\".")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TOPIC = new PropertyDescriptor.Builder<String>()
            .name("topic")
            .description(
                    "Kafka topic name. If there are multiple topics, use , to split, for example: \"tpc1,tpc2\".\n")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FORMAT = new PropertyDescriptor.Builder<String>()
            .name("format")
            .description("We support the following file types: text, json")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .defaultValue("json")
            .allowableValues("text", "json", "canal_json", "debezium_json", "compatible_debezium_json", "compatible_kafka_connect_json")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FIELD_DELIMITER = new PropertyDescriptor.Builder<String>()
            .name("field_delimiter")
            .description("The separator between columns in a row of data. Only needed by text and csv file format")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> KAFKA_CONFIG = new PropertyDescriptor.Builder()
            .name("kafka.config")
            .description(
                    "The way to specify parameters is to add the prefix kafka. to the original parameter name. For example, the way to specify auto.offset.reset is: kafka.auto.offset.reset = latest")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
