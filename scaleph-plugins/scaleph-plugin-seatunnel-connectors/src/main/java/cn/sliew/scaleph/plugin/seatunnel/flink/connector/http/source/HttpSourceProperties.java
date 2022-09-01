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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.http.source;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum HttpSourceProperties {
    ;

    public static final PropertyDescriptor<String> URL = new PropertyDescriptor.Builder<String>()
            .name("url")
            .description("http request url")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> METHOD = new PropertyDescriptor.Builder<String>()
            .name("method")
            .description("http request method")
            .type(PropertyType.STRING)
            .allowableValues("GET", "POST")
            .defaultValue("GET")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> HEADERS = new PropertyDescriptor.Builder<String>()
            .name("headers")
            .description("http headers")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PARAMS = new PropertyDescriptor.Builder<String>()
            .name("params")
            .description("http params")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> BODY = new PropertyDescriptor.Builder<String>()
            .name("body")
            .description("http body")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
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

    public static final PropertyDescriptor<String> SCHEMA = new PropertyDescriptor.Builder<String>()
            .name("schema")
            .description("the upstream data schema")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();
}
