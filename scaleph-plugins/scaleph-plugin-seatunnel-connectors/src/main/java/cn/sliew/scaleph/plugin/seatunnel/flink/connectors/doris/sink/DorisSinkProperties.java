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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.doris.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum DorisSinkProperties {
    ;

    public static final PropertyDescriptor<String> SINK_LABEL_PREFIX = new PropertyDescriptor.Builder()
            .name("sink.label-prefix")
            .description("The prefix of Doris stream load label")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> SINK_ENABLE_2PC = new PropertyDescriptor.Builder()
            .name("sink.enable-2pc")
            .description("Whether to enable two-phase commit (2pc), the default is true, to ensure Exactly-Once semantics")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> SINK_ENABLE_DELETE = new PropertyDescriptor.Builder()
            .name("sink.enable-delete")
            .description("Whether to enable deletion.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> DORIS_CONFIG = new PropertyDescriptor.Builder()
            .name("doris.config.")
            .description("The way to specify the parameter is to add the prefix sink.properties. to the original stream load parameter")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

}
