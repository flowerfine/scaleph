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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.email.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum EmailProperties {
    ;

    public static final PropertyDescriptor<String> EMAIL_FROM_ADDRESS = new PropertyDescriptor.Builder<String>()
            .name("email_from_address")
            .description("email_from_address")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> EMAIL_TO_ADDRESS = new PropertyDescriptor.Builder<String>()
            .name("email_to_address")
            .description("email_to_address")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> EMAIL_AUTHORIZATION_CODE = new PropertyDescriptor.Builder<String>()
            .name("email_authorization_code")
            .description("email_authorization_code")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> EMAIL_MESSAGE_HEADLINE = new PropertyDescriptor.Builder<String>()
            .name("email_message_headline")
            .description("email_message_headline")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> EMAIL_MESSAGE_CONTENT = new PropertyDescriptor.Builder<String>()
            .name("email_message_content")
            .description("email_message_content")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> EMAIL_HOST = new PropertyDescriptor.Builder<String>()
            .name("email_host")
            .description("email_host")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> EMAIL_TRANSPORT_PROTOCOL = new PropertyDescriptor.Builder<String>()
            .name("email_transport_protocol")
            .description("email_transport_protocol")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> EMAIL_SMTP_AUTH = new PropertyDescriptor.Builder<String>()
            .name("email_smtp_auth")
            .description("email_smtp_auth")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
