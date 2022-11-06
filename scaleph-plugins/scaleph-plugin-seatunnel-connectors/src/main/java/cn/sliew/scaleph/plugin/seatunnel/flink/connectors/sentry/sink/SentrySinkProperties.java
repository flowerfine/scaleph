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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.sentry.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum SentrySinkProperties {
    ;

    public static final PropertyDescriptor<String> DSN = new PropertyDescriptor.Builder()
            .name("dsn")
            .description("The DSN tells the SDK where to send the events to.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> ENV = new PropertyDescriptor.Builder()
            .name("env")
            .description("specify the environment")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> RELEASE = new PropertyDescriptor.Builder()
            .name("release")
            .description("specify the release")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> ENABLE_EXTERNAL_CONFIGURATION = new PropertyDescriptor.Builder()
            .name("enableExternalConfiguration")
            .description("if loading properties from external sources is enabled.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CACHE_DIR_PATH = new PropertyDescriptor.Builder()
            .name("cacheDirPath")
            .description("the cache dir path for caching offline events")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_CACHE_ITEMS = new PropertyDescriptor.Builder()
            .name("maxCacheItems")
            .description("The max cache items for capping the number of events Default is 30")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.NON_NEGATIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> FLUSH_TIMEOUT_MILLIS = new PropertyDescriptor.Builder()
            .name("flushTimeoutMillis")
            .description("Controls how many seconds to wait before flushing down")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_QUEUE_SIZE = new PropertyDescriptor.Builder()
            .name("maxQueueSize")
            .description("Max queue size before flushing events/envelopes to the disk")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

}
