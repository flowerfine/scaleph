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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.influxdb.sink;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public enum InfluxDBSinkProperties {
    ;

    public static final PropertyDescriptor<String> MEASUREMENT = new PropertyDescriptor.Builder()
            .name("measurement")
            .description("The name of influxDB measurement")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> KEY_TIME = new PropertyDescriptor.Builder()
            .name("key_time")
            .description("Specify field-name of the influxDB measurement timestamp in SeaTunnelRow. If not specified, use processing-time as timestamp")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> KEY_TAGS = new PropertyDescriptor.Builder()
            .name("key_tags")
            .description("Specify field-name of the influxDB measurement tags in SeaTunnelRow. If not specified, include all fields with influxDB measurement field")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> BATCH_SIZE = new PropertyDescriptor.Builder()
            .name("batch_size")
            .description("For batch writing, when the number of buffers reaches the number of batch_size or the time reaches batch_interval_ms, the data will be flushed into the influxDB")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> BATCH_INTERVAL_MS = new PropertyDescriptor.Builder()
            .name("batch_interval_ms")
            .description("For batch writing, when the number of buffers reaches the number of batch_size or the time reaches batch_interval_ms, the data will be flushed into the influxDB")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_RETRIES = new PropertyDescriptor.Builder()
            .name("max_retries")
            .description("The number of retries to flush failed")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RETRY_BACKOFF_MULTIPLIER_MS = new PropertyDescriptor.Builder()
            .name("retry_backoff_multiplier_ms")
            .description("Using as a multiplier for generating the next delay for backoff")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_RETRY_BACKOFF_MS = new PropertyDescriptor.Builder()
            .name("max_retry_backoff_ms")
            .description("The amount of time to wait before attempting to retry a request to influxDB")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();
}
