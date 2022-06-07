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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.clickhouse.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum ClickHouseSinkProperties {
    ;

    public static final PropertyDescriptor<Boolean> SPLIT_MODE = new PropertyDescriptor.Builder<Boolean>()
            .name("split_mode")
            .description("This mode only support clickhouse table which engine is 'Distributed'." +
                    "And internal_replication option should be true. " +
                    "They will split distributed table data in seatunnel and perform write directly on each shard. " +
                    "The shard weight define is clickhouse will be counted.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SHARDING_KEY = new PropertyDescriptor.Builder<String>()
            .name("sharding_key")
            .description("When use split_mode, which node to send data to is a problem, the default is random selection, but the 'sharding_key' parameter can be used to specify the field for the sharding algorithm. " +
                    "This option only worked when 'split_mode' is true.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CLICKHOUSE_XXX = new PropertyDescriptor.Builder<String>()
            .name("clickhouse.*")
            .description("Specify parameters adding the prefix clickhouse to the original parameter name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> BULK_SIZE = new PropertyDescriptor.Builder<Long>()
            .name("bulk_size")
            .description("The number of rows written through Clickhouse-jdbc each time, the default is 20000")
            .type(PropertyType.INT)
            .defaultValue(20000)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RETRY = new PropertyDescriptor.Builder<Integer>()
            .name("retry")
            .description("The number of retries, the default is 1")
            .type(PropertyType.INT)
            .defaultValue(1)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> RETRY_CODES = new PropertyDescriptor.Builder<String>()
            .name("retry_codes")
            .description("When an exception occurs, the ClickHouse exception error code of the operation will be retried")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
