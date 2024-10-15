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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.email.sink;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.amazonaws.services.dynamodbv2.xspec.BOOL;

public enum EmailSinkProperties {
    ;

    public static final PropertyDescriptor<String> EMAIL_HOST = new PropertyDescriptor.Builder()
            .name("email_host")
            .description("SMTP server host")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> EMAIL_TRANSPORT_PROTOCOL = new PropertyDescriptor.Builder()
            .name("email_transport_protocol")
            .description("The protocol to load the session.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> EMAIL_FROM_ADDRESS = new PropertyDescriptor.Builder()
            .name("email_from_address")
            .description("Sender Email Address")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<BOOL> EMAIL_SMTP_AUTH = new PropertyDescriptor.Builder()
            .name("email_smtp_auth")
            .description("Whether to authenticate the customer")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> EMAIL_SMTP_PORT = new PropertyDescriptor.Builder()
            .name("email_smtp_port")
            .description("Select port for authentication.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> EMAIL_AUTHORIZATION_CODE = new PropertyDescriptor.Builder()
            .name("email_authorization_code")
            .description("authorization code,You can obtain the authorization code from the mailbox Settings.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> EMAIL_TO_ADDRESS = new PropertyDescriptor.Builder()
            .name("email_to_address")
            .description("Receiver Email Address")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> EMAIL_MESSAGE_HEADLINE = new PropertyDescriptor.Builder()
            .name("email_message_headline")
            .description("The subject line of the entire message.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> EMAIL_MESSAGE_CONTENT = new PropertyDescriptor.Builder()
            .name("email_message_content")
            .description("The body of the entire message.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
