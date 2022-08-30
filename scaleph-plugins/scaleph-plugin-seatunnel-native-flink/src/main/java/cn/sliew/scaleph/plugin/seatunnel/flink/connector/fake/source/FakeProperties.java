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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.fake.source;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.PropertyType;

public enum FakeProperties {
    ;

    public static final PropertyDescriptor<Integer> MOCK_DATA_SCHEMA = new PropertyDescriptor.Builder<String>()
            .name("mock_data_schema")
            .description("config mock data's schema. Each is column_config option")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MOCK_DATA_SIZE = new PropertyDescriptor.Builder<String>()
            .name("mock_data_size")
            .description("config mock data size")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MOCK_DATA_INTERVAL = new PropertyDescriptor.Builder<String>()
            .name("mock_data_interval")
            .description("config the data can mock with interval, the unit is second")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .validateAndBuild();

}
