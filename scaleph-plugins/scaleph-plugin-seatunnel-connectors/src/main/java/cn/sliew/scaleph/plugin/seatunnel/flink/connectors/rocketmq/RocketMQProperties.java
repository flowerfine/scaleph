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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.rocketmq;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum RocketMQProperties {
    ;

    public static final PropertyDescriptor<String> NAME_SRV_ADDR = new PropertyDescriptor.Builder()
            .name("name.srv.addr")
            .description("RocketMQ name server cluster address.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TOPICS = new PropertyDescriptor.Builder()
            .name("topics")
            .description("RocketMQ topic name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CONSUMER_GROUP = new PropertyDescriptor.Builder()
            .name("consumer.group")
            .description("RocketMQ consumer group id")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .defaultValue("SeaTunnel-Consumer-Group")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
