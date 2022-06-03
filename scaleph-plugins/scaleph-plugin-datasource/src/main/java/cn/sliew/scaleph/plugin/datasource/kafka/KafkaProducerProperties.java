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

package cn.sliew.scaleph.plugin.datasource.kafka;

import cn.sliew.scaleph.plugin.framework.property.*;
import org.apache.kafka.clients.producer.ProducerConfig;

public enum KafkaProducerProperties {
    ;

    public static final AllowableValue DELIVERY_REPLICATED =
            new AllowableValue("all", "Guarantee Replicated Delivery",
                    "FlowFile will be routed to failure unless the message is replicated to the appropriate "
                            + "number of Kafka Nodes according to the Topic configuration");
    public static final AllowableValue DELIVERY_ONE_NODE =
            new AllowableValue("1", "Guarantee Single Node Delivery",
                    "FlowFile will be routed to success if the message is received by a single Kafka node, "
                            +
                            "whether or not it is replicated. This is faster than <Guarantee Replicated Delivery> "
                            + "but can result in data loss if a Kafka node crashes");
    public static final AllowableValue DELIVERY_BEST_EFFORT = new AllowableValue("0", "Best Effort",
            "FlowFile will be routed to success after successfully sending the content to a Kafka node, "
                    +
                    "without waiting for any acknowledgment from the node at all. This provides the best performance but may result in data loss.");

    public static final PropertyDescriptor DELIVERY_GUARANTEE = new PropertyDescriptor.Builder()
            .name(ProducerConfig.ACKS_CONFIG)
            .description(
                    "Specifies the requirement for guaranteeing that a message is sent to Kafka. Corresponds to Kafka's 'acks' property.")
            .type(PropertyType.STRING)
            .defaultValue(DELIVERY_REPLICATED.getValue())
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .allowableValues(DELIVERY_BEST_EFFORT, DELIVERY_ONE_NODE, DELIVERY_REPLICATED)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor METADATA_WAIT_TIME = new PropertyDescriptor.Builder()
            .name(ProducerConfig.MAX_BLOCK_MS_CONFIG)
            .description(
                    "The amount of time publisher will wait to obtain metadata or wait for the buffer to flush during the 'send' call before failing the "
                            + "entire 'send' call. Corresponds to Kafka's 'max.block.ms' property")
            .type(PropertyType.INT)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .defaultValue("5 sec")
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor ACK_WAIT_TIME = new PropertyDescriptor.Builder()
            .name("ack.wait.time")
            .description(
                    "After sending a message to Kafka, this indicates the amount of time that we are willing to wait for a response from Kafka. "
                            +
                            "If Kafka does not acknowledge the message within this time period, the FlowFile will be routed to 'failure'.")
            .type(PropertyType.INT)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .defaultValue("5 secs")
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor MAX_REQUEST_SIZE = new PropertyDescriptor.Builder()
            .name(ProducerConfig.MAX_REQUEST_SIZE_CONFIG)
            .description(
                    "The maximum size of a request in bytes. Corresponds to Kafka's 'max.request.size' property and defaults to 1 MB (1048576).")
            .type(PropertyType.INT)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .defaultValue("1 MB")
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor COMPRESSION_CODEC = new PropertyDescriptor.Builder()
            .name(ProducerConfig.COMPRESSION_TYPE_CONFIG)
            .description(
                    "This parameter allows you to specify the compression codec for all data generated by this producer.")
            .type(PropertyType.STRING)
            .defaultValue("none")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .allowableValues("none", "gzip", "snappy", "lz4")
            .properties(Property.Required)
            .validateAndBuild();
}
