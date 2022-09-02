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

package cn.sliew.scaleph.plugin.seatunnel.flink.env;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.PropertyType;
import cn.sliew.scaleph.plugin.framework.property.Validators;

import java.util.Arrays;
import java.util.List;

public enum EnvironmentProperties {
    ;

    public static final PropertyDescriptor<Integer> PARALLELISM = new PropertyDescriptor.Builder<String>()
            .name("execution.parallelism")
            .description("default parallelism")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_PARALLELISM = new PropertyDescriptor.Builder<String>()
            .name("execution.max-parallelism")
            .description("max parallelism")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TIME_CHARACTERISTIC = new PropertyDescriptor.Builder<String>()
            .name("execution.time-characteristic")
            .description("time characteristic")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .allowableValues("event-time", "ingestion-time", "processing-time")
            .validateAndBuild();

    public static final PropertyDescriptor<Long> BUFFER_TIMEOUT_MILLIS =new PropertyDescriptor.Builder<String>()
            .name("execution.buffer.timeout")
            .description("buffer buffer")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> STATE_BACKEND = new PropertyDescriptor.Builder<String>()
            .name("execution.state.backend")
            .description("The state backend to use. This defines the data structure mechanism for taking snapshots.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .allowableValues("filesystem", "rocksdb")
            .validateAndBuild();

    public static final PropertyDescriptor<String> PLANNER = new PropertyDescriptor.Builder<String>()
            .name("execution.planner")
            .description("The planner to use. default is blink")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .allowableValues("blink")
            .validateAndBuild();

    public static final List<PropertyDescriptor> SUPPORTED_PROPERTIES = Arrays.asList(PARALLELISM, MAX_PARALLELISM,
            TIME_CHARACTERISTIC, BUFFER_TIMEOUT_MILLIS, STATE_BACKEND, PLANNER);

}
