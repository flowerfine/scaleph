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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.http.source;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum HttpSourceProperties {
    ;

    public static final PropertyDescriptor<String> METHOD = new PropertyDescriptor.Builder<String>()
            .name("method")
            .description("http request method")
            .type(PropertyType.STRING)
            .allowableValues("GET", "POST")
            .defaultValue("GET")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> BODY = new PropertyDescriptor.Builder()
            .name("body")
            .description("http body")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> PAGEING = new PropertyDescriptor.Builder()
            .name("pageing")
            .description("This parameter is used for paging queries")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FORMAT = new PropertyDescriptor.Builder<String>()
            .name("format")
            .description("the format of upstream data, now only support json text, default json.")
            .type(PropertyType.STRING)
            .allowableValues("json", "text")
            .defaultValue("json")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CONTENT_JSON = new PropertyDescriptor.Builder()
            .name("content_json")
            .description("This parameter can get some json data")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> JSON_FIELD = new PropertyDescriptor.Builder()
            .name("json_field")
            .description("This parameter helps you configure the schema,so this parameter must be used with schema")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> SCHEMA = new PropertyDescriptor.Builder()
            .name("schema")
            .description("the upstream data schema")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> POLL_INTERVAL_MS = new PropertyDescriptor.Builder<Long>()
            .name("poll_interval_ms")
            .description("request http api interval(millis) in stream mode")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> ENABLE_MULTI_LINES = new PropertyDescriptor.Builder<Boolean>()
            .name("enable_multi_lines")
            .description("SeaTunnel enableMultiLines.This parameter can support http splitting response text by line.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

}
