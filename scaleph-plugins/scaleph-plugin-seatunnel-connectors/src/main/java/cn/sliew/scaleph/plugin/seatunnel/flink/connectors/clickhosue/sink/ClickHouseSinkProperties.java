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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.sink;

import cn.sliew.scaleph.plugin.framework.property.*;
import org.codehaus.jackson.JsonNode;

public enum ClickHouseSinkProperties {
    ;

    public static final PropertyDescriptor<String> TABLE = new PropertyDescriptor.Builder()
            .name("table")
            .description("table name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> BULK_SIZE = new PropertyDescriptor.Builder()
            .name("bulk_size")
            .description(
                    "The number of rows written through Clickhouse-jdbc each time, the default is 20000")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(20000)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> SPLIT_MODE = new PropertyDescriptor.Builder()
            .name("split_mode")
            .description(
                    "This mode only support clickhouse table which engine is 'Distributed'.And internal_replication option should be true. They will split distributed table data in seatunnel and perform write directly on each shard. "
                            + "The shard weight define is clickhouse will be counted.\n")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(false)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SHARDING_KEY = new PropertyDescriptor.Builder()
            .name("sharding_key")
            .description(
                    "When use split_mode, which node to send data to is a problem, the default is random selection, "
                            + "but the 'sharding_key' parameter can be used to specify the field for the sharding algorithm. "
                            + "This option only worked when 'split_mode' is true.\n")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PRIMARY_KEY = new PropertyDescriptor.Builder()
            .name("primary_key")
            .description("Mark the primary key column from clickhouse table, and based on primary key execute INSERT/UPDATE/DELETE to clickhouse table")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> SUPPORT_UPSERT = new PropertyDescriptor.Builder()
            .name("support_upsert")
            .description("Mark the primary key column from clickhouse table, and based on primary key execute INSERT/UPDATE/DELETE to clickhouse table")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(false)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> ALLOW_EXPERIMENTAL_LIGHTWEIGHT_DELETE = new PropertyDescriptor.Builder()
            .name("allow_experimental_lightweight_delete")
            .description("Allow experimental lightweight delete based on *MergeTree table engine")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(false)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

}
