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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.elasticsearch.sink;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum ElasticsearchSinkProperties {
    ;

    public static final PropertyDescriptor<JsonNode> PRIMARY_KEYS = new PropertyDescriptor.Builder()
            .name("primary_keys")
            .description("Primary key fields used to generate the document _id, this is cdc required options.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> KEY_DELIMITER = new PropertyDescriptor.Builder()
            .name("key_delimiter")
            .description("Delimiter for composite keys (\"_\" by default)")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .defaultValue("_")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_RETRY_COUNT = new PropertyDescriptor.Builder()
            .name("max_retry_count")
            .description("one bulk request max try size")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.NON_NEGATIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_BATCH_SIZE = new PropertyDescriptor.Builder()
            .name("max_batch_size")
            .description("batch bulk doc max size")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

}
