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

public enum FaultToleranceProperties {
    ;

    public static final PropertyDescriptor<String> RESTART_STRATEGY = new PropertyDescriptor.Builder<>()
            .name("execution.restart.strategy")
            .description("Defines the restart strategy to use in case of job failures")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .allowableValues("none", "off", "disable", "fixeddelay", "fixed-delay", "failurerate", "failure-rate", "exponentialdelay", "exponential-delay")
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RESTART_ATTEMPTS = new PropertyDescriptor.Builder<>()
            .name("execution.restart.attempts")
            .description("The number of times that Flink retries the execution before the job is declared as failed if restart-strategy has been set to fixed-delay.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RESTART_DELAY_BETWEEN_ATTEMPTS = new PropertyDescriptor.Builder<>()
            .name("execution.restart.delayBetweenAttempts")
            .description("Delay between two consecutive restart attempts if restart-strategy has been set to fixed-delay")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RESTART_FAILURE_RATE = new PropertyDescriptor.Builder<>()
            .name("execution.restart.failureRate")
            .description("Maximum number of restarts in given time interval before failing a job if restart-strategy has been set to failure-rate")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RESTART_FAILURE_INTERVAL = new PropertyDescriptor.Builder<>()
            .name("execution.restart.failureInterval")
            .description("Time interval for measuring failure rate if restart-strategy has been set to failure-rate.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RESTART_DELAY_INTERVAL = new PropertyDescriptor.Builder<>()
            .name("execution.restart.delayInterval")
            .description("Delay between two consecutive restart attempts if restart-strategy has been set to failure-rate.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final List<PropertyDescriptor> SUPPORTED_PROPERTIES = Arrays.asList(RESTART_STRATEGY,
            RESTART_ATTEMPTS, RESTART_DELAY_BETWEEN_ATTEMPTS, RESTART_FAILURE_RATE, RESTART_FAILURE_INTERVAL,
            RESTART_DELAY_INTERVAL);
}
