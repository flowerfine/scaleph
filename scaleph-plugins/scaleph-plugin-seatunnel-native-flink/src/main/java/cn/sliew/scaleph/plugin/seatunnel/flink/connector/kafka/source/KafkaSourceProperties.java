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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.kafka.source;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum KafkaSourceProperties {
    ;

    public static final PropertyDescriptor<String> TOPICS = new PropertyDescriptor.Builder<String>()
            .name("topics")
            .description("If there are multiple topics, use (,) to split, for example: \"tpc1,tpc2\"")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CONSUMER_GROUP_ID = new PropertyDescriptor.Builder<String>()
            .name("consumer_group_id")
            .description("kafka consumer group id")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CONSUMER_BOOTSTRAP_SERVERS = new PropertyDescriptor.Builder<String>()
            .name("consumer_bootstrap_servers")
            .description("cluster address, separated by (,)")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FORMAT_TYPE = new PropertyDescriptor.Builder<String>()
            .name("format_type")
            .description("message format")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .allowableValues("json", "csv", "avro")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FORMAT_CONF = new PropertyDescriptor.Builder<String>()
            .name("format_conf")
            .description("The csv format uses this parameter to set the separator and so on. For example, set the column delimiter to \\t , format.field-delimiter=\\\\t")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SCHEMA = new PropertyDescriptor.Builder<String>()
            .name("schema")
            .description("message schema")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> ROWTIME_FIELD = new PropertyDescriptor.Builder<String>()
            .name("rowtime_field")
            .description("Extract timestamp using current configuration field for flink event time watermark")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> WATERMARK = new PropertyDescriptor.Builder<String>()
            .name("watermark")
            .description("Sets a built-in watermark strategy for rowtime.field attributes which are out-of-order by a bounded time interval. " +
                    "Emits watermarks which are the maximum observed timestamp minus the specified delay.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> OFFSET_RESET = new PropertyDescriptor.Builder<String>()
            .name("offset_reset")
            .description("The consumer's initial offset is only valid for new consumers")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .allowableValues("latest", "earliest", "specific")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> OFFSET_RESET_SPECIFIC = new PropertyDescriptor.Builder<String>()
            .name("offset_reset_specific")
            .description("start consumption from the specified offset , and specify the start offset of each partition at this time. ")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CONSUMER_CONF = new PropertyDescriptor.Builder<String>()
            .name("consumer_conf")
            .description("Specify parameters adding the prefix consumer to the original parameter name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
