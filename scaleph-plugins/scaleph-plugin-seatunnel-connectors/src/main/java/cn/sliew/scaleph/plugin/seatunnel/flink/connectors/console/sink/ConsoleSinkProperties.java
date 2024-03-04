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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.console.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum ConsoleSinkProperties {
    ;

    public static final PropertyDescriptor<Boolean> LOG_PRINT_DATA = new PropertyDescriptor.Builder()
            .name("log.print.data")
            .description("Flag to determine whether data should be printed in the logs.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> LOG_PRINT_DELAY_MS = new PropertyDescriptor.Builder()
            .name("log.print.delay.ms")
            .description("Delay in milliseconds between printing each data item to the logs.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.INTEGER_VALIDATOR)
            .validateAndBuild();

}
