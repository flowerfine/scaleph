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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.elasticsearch;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum ElasticsearchProperties {
    ;

    public static final PropertyDescriptor<JsonNode> HOSTS = new PropertyDescriptor.Builder()
            .name("hosts")
            .description("cluster http address, the format is host:port")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> USERNAME = new PropertyDescriptor.Builder()
            .name("username")
            .description("cluster username")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PASSWORD = new PropertyDescriptor.Builder()
            .name("password")
            .description("cluster password")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();


    public static final PropertyDescriptor<Boolean> TLS_VERIFY_CERTIFICATE = new PropertyDescriptor.Builder()
            .name("tls_verify_certificate")
            .description("Enable certificates validation for HTTPS endpoints")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .defaultValue(true)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> TLS_VERIFY_HOSTNAMES = new PropertyDescriptor.Builder()
            .name("tls_verify_hostnames")
            .description("Enable hostname validation for HTTPS endpoints")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .defaultValue(true)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TLS_KEYSTORE_PATH = new PropertyDescriptor.Builder()
            .name("tls_keystore_path")
            .description("The path to the PEM or JKS key store")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TLS_KEYSTORE_PASSWORD = new PropertyDescriptor.Builder()
            .name("tls_keystore_password")
            .description("The key password for the key store specified")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TLS_TRUSTSTORE_PATH = new PropertyDescriptor.Builder()
            .name("tls_truststore_path")
            .description("The path to PEM or JKS trust store")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TLS_TRUSTSTORE_PASSWORD = new PropertyDescriptor.Builder()
            .name("tls_truststore_password")
            .description("The key password for the trust store specified")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> INDEX = new PropertyDescriptor.Builder()
            .name("index")
            .description("cluster index name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();
}
