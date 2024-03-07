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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.maxcompute.source;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum MaxComputeSourceProperties {
    ;

    public static final PropertyDescriptor<Integer> SPLIT_ROW = new PropertyDescriptor.Builder()
            .name("split_row")
            .description("Number of rows per split")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(10000)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> SCHEMA = new PropertyDescriptor.Builder()
            .name("schema")
            .description("schema")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
