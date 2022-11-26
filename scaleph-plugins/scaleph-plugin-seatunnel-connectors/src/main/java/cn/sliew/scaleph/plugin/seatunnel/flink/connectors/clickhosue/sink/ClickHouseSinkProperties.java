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

    public static final PropertyDescriptor<String> TABLE = new PropertyDescriptor.Builder<String>()
            .name("table")
            .description("table name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FIELDS = new PropertyDescriptor.Builder<String>()
            .name("fields")
            .description(
                    "The data field that needs to be output to ClickHouse , if not configured, it will be automatically adapted according to the sink table schema .\n"
                            + "\n")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> CLICKHOUSE_CONF = new PropertyDescriptor.Builder<JsonNode>()
            .name("clickhouse.")
            .description(
                    "clickhouse.* The way to specify the parameter is to add the prefix clickhouse. to the original parameter name. For example, the way to specify socket_timeout is: clickhouse.socket_timeout = 50000 . "
                            + "If these non-essential parameters are not specified, they will use the default values given by clickhouse-jdbc.\n")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> BULK_SIZE = new PropertyDescriptor.Builder<Long>()
            .name("bulk_size")
            .description(
                    "The number of rows written through Clickhouse-jdbc each time, the default is 20000")
            .type(PropertyType.INT)
            .defaultValue("20000")
            .parser(Parsers.LONG_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> SPLIT_MODE = new PropertyDescriptor.Builder<Boolean>()
            .name("split_mode")
            .description(
                    "This mode only support clickhouse table which engine is 'Distributed'.And internal_replication option should be true. They will split distributed table data in seatunnel and perform write directly on each shard. "
                            + "The shard weight define is clickhouse will be counted.\n")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SHARDING_KEY = new PropertyDescriptor.Builder<String>()
            .name("sharding_key")
            .description(
                    "When use split_mode, which node to send data to is a problem, the default is random selection, "
                            + "but the 'sharding_key' parameter can be used to specify the field for the sharding algorithm. "
                            + "This option only worked when 'split_mode' is true.\n")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
