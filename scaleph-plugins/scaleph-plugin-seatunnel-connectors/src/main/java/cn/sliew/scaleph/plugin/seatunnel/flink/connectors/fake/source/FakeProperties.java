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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.fake.source;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum FakeProperties {
    ;

    public static final PropertyDescriptor<String> SCHEMA = new PropertyDescriptor.Builder<String>()
        .name("schema")
        .description(
            "Table structure description ,you should assign schema option to tell connector how to parse data to the row you want.")
        .type(PropertyType.STRING)
        .parser(Parsers.STRING_PARSER)
        .properties(Property.Required)
        .addValidator(Validators.NON_BLANK_VALIDATOR)
        .validateAndBuild();

    public static final PropertyDescriptor<String> ROW_NUM = new PropertyDescriptor.Builder<String>()
        .name("row.num")
        .description(
            "Table structure description ,you should assign schema option to tell connector how to parse data to the row you want.")
        .type(PropertyType.LONG)
        .parser(Parsers.LONG_PARSER)
        .addValidator(Validators.NON_BLANK_VALIDATOR)
        .validateAndBuild();


}
