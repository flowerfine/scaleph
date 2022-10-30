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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.redis.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum RedisSinkProperties {
    ;

    public static final PropertyDescriptor<String> KEY = new PropertyDescriptor.Builder()
            .name("key")
            .description("The value of key you want to write to redis.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DATA_TYPE = new PropertyDescriptor.Builder()
            .name("data_type")
            .description("Redis data types")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .allowableValues("key", "hash", "list", "set", "zset")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FORMAT = new PropertyDescriptor.Builder()
            .name("format")
            .description("the format of upstream data, now only support json text, default json.")
            .type(PropertyType.STRING)
            .defaultValue("json")
            .allowableValues("text", "json")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();
}
