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

public enum IdleStateRetentionProperties {
    ;

    public static final PropertyDescriptor<Integer> MIN_STATE_RETENTION_TIME = new PropertyDescriptor.Builder<>()
            .name("execution.query.state.min-retention")
            .description("Specifies a minimum time interval for how long idle state (i.e. state which was not updated), will be retained")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_STATE_RETENTION_TIME = new PropertyDescriptor.Builder<>()
            .name("execution.query.state.max-retention")
            .description("Specifies a maximum time interval for how long idle state (i.e. state which was not updated), will be retained")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final List<PropertyDescriptor> SUPPORTED_PROPERTIES = Arrays.asList(MIN_STATE_RETENTION_TIME, MAX_STATE_RETENTION_TIME);

}
