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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.redis;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum RedisProperties {
    ;

    public static final PropertyDescriptor<String> HOST = new PropertyDescriptor.Builder()
            .name("host")
            .description("redis host")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> PORT = new PropertyDescriptor.Builder()
            .name("port")
            .description("redis host")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_NEGATIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> USER = new PropertyDescriptor.Builder()
            .name("user")
            .description("redis authentication user, you need it when you connect to an encrypted cluster")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> AUTH = new PropertyDescriptor.Builder()
            .name("auth")
            .description("Redis authentication password, you need it when you connect to an encrypted cluster")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
