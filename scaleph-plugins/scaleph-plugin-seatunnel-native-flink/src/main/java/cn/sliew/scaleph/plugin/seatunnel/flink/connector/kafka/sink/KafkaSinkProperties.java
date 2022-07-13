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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.kafka.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum KafkaSinkProperties {
    ;

    public static final PropertyDescriptor<String> TOPICS = new PropertyDescriptor.Builder<String>()
            .name("topics")
            .description("Kafka topic")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PRODUCER_BOOTSTRAP_SERVERS = new PropertyDescriptor.Builder<String>()
            .name("producer_bootstrap_servers")
            .description("cluster address, separated by (,)")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PRODUCER_CONF = new PropertyDescriptor.Builder<String>()
            .name("producer_conf")
            .description("Specify parameters adding the prefix producer to the original parameter name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SEMANTIC = new PropertyDescriptor.Builder<String>()
            .name("semantic")
            .description("Semantics that can be chosen. exactly_once/at_least_once/none, default is at_least_once")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
//            .allowableValues("exactly_once", "at_least_once", "none")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();
}
