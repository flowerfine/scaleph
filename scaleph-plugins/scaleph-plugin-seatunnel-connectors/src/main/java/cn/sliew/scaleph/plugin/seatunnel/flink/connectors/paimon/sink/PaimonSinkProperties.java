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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.paimon.sink;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum PaimonSinkProperties {
    ;

    public static final PropertyDescriptor<String> PAIMON_TABLE_PRIMARY_KEYS = new PropertyDescriptor.Builder()
            .name("paimon.table.primary-keys")
            .description("Default comma-separated list of columns (primary key) that identify a row in tables.(Notice: The partition field needs to be included in the primary key fields)")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PAIMON_TABLE_PARTITION_KEYS = new PropertyDescriptor.Builder()
            .name("paimon.table.partition-keys")
            .description("Default comma-separated list of partition fields to use when creating tables.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> PAIMON_TABLE_WRITE_PROPS = new PropertyDescriptor.Builder()
            .name("paimon.table.write-props")
            .description("Properties passed through to paimon table initialization")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
