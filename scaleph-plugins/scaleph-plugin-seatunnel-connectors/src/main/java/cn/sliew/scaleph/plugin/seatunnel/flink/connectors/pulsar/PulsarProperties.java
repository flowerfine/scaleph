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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.pulsar;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum PulsarProperties {
    ;

    public static final PropertyDescriptor<String> CLIENT_SERVICE_URL = new PropertyDescriptor.Builder()
            .name("client.service-url")
            .description("Service URL provider for Pulsar service.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> ADMIN_SERVICE_URL = new PropertyDescriptor.Builder()
            .name("admin.service-url")
            .description("The Pulsar service HTTP URL for the admin endpoint.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> AUTH_PLUGIN_CLASS = new PropertyDescriptor.Builder()
            .name("auth.plugin-class")
            .description("Name of the authentication plugin.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> AUTH_PARAMS = new PropertyDescriptor.Builder()
            .name("auth.params")
            .description("Parameters for the authentication plugin.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> PULSAR_CONFIG = new PropertyDescriptor.Builder()
            .name("pulsar.config")
            .description("In addition to the above parameters that must be specified by the Pulsar producer client.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TOPIC = new PropertyDescriptor.Builder()
            .name("topic")
            .description("Topic name(s) to read data from when the table is used as source. It also supports topic list for source by separating topic by semicolon")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FORMAT = new PropertyDescriptor.Builder()
            .name("format")
            .description("Data format.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .defaultValue("json")
            .allowableValues("json", "text")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    /**
     * todo does this in use ?
     */
    public static final PropertyDescriptor<String> FIELD_DELIMITER = new PropertyDescriptor.Builder<String>()
            .name("field_delimiter")
            .description("The separator between columns in a row of data. Only needed by text and csv file format")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
