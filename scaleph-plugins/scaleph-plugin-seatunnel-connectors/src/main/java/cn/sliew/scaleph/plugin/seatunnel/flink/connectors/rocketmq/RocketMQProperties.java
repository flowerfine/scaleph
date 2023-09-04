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

    public static final PropertyDescriptor<Boolean> ACL_ENABLED = new PropertyDescriptor.Builder()
            .name("acl.enabled")
            .description("If true, access control is enabled, and access key and secret key need to be configured.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(false)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> ACCESS_KEY = new PropertyDescriptor.Builder()
            .name("access.key")
            .description("When ACL_ENABLED is true, access key cannot be empty.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SECRET_KEY = new PropertyDescriptor.Builder()
            .name("secret.key")
            .description("When ACL_ENABLED is true, secret key cannot be empty.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FORMAT = new PropertyDescriptor.Builder()
            .name("format")
            .description(
                    "Data format. The default format is json. Optional text format. The default field separator is \", \". "
                            + "If you customize the delimiter, add the \"field.delimiter\" option.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .allowableValues("json", "text")
            .defaultValue("json")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FIELD_DELIMITER = new PropertyDescriptor.Builder()
            .name("field.delimiter")
            .description("Customize the field delimiter for data format.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
