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

public enum CheckpointProperties {
    ;

    public static final PropertyDescriptor<Integer> CHECKPOINT_INTERVAL = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.interval")
            .description("This setting defines the base interval")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CHECKPOINT_MODE = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.mode")
            .description("The checkpointing mode (exactly-once vs. at-least-once).")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CHECKPOINT_DATA_URI = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.data-uri")
            .description("The directory to write checkpoints to")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> CHECKPOINT_TIMEOUT = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.timeout")
            .description("The maximum time that a checkpoint may take before being discarded.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_CONCURRENT_CHECKPOINTS = new PropertyDescriptor.Builder<>()
            .name("execution.max-concurrent-checkpoints")
            .description("The maximum number of checkpoint attempts that may be in progress at the same time. If this value is n, then no checkpoints will be triggered while n checkpoint attempts are currently in flight. For the next checkpoint to be triggered, one checkpoint attempt would need to finish or expire.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CHECKPOINT_CLEANUP_MODE = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.cleanup-mode")
            .description("The mode defines how an externalized checkpoint should be cleaned up on job cancellation")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MIN_PAUSE_BETWEEN_CHECKPOINTS = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.min-pause")
            .description("The minimal pause between checkpointing attempts")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> FAIL_ON_CHECKPOINTING_ERRORS = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.fail-on-error")
            .description("The tolerable checkpoint consecutive failure number. If set to 0, that means we do not tolerance any checkpoint failure.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final List<PropertyDescriptor> SUPPORTED_PROPERTIES = Arrays.asList(CHECKPOINT_INTERVAL,
            CHECKPOINT_MODE, CHECKPOINT_DATA_URI, CHECKPOINT_TIMEOUT, MAX_CONCURRENT_CHECKPOINTS,
            CHECKPOINT_CLEANUP_MODE, MIN_PAUSE_BETWEEN_CHECKPOINTS, FAIL_ON_CHECKPOINTING_ERRORS);
}
