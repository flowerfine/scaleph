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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.jdbc.source;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum JdbcSourceProperties {
    ;

    public static final PropertyDescriptor<String> QUERY = new PropertyDescriptor.Builder<String>()
            .name("query")
            .description("query statement")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TABLE_PATH = new PropertyDescriptor.Builder<String>()
            .name("table_path")
            .description("The path to the full path of table, you can use this configuration instead of query")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> TABLE_LIST = new PropertyDescriptor.Builder<String>()
            .name("table_list")
            .description("The path to the full path of table, you can use this configuration instead of query")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> WHERE_CONDITION = new PropertyDescriptor.Builder<String>()
            .name("where_condition")
            .description("Common row filter conditions for all tables/queries")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PARTITION_COLUMN = new PropertyDescriptor.Builder<String>()
            .name("partition_column")
            .description("The column name for parallelism's partition, only support numeric type.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> PARTITION_UPPER_BOUND = new PropertyDescriptor.Builder<Long>()
            .name("partition_upper_bound")
            .description(
                    "The partition_column max value for scan, if not set SeaTunnel will query database get max value.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> PARTITION_LOWER_BOUND = new PropertyDescriptor.Builder<Long>()
            .name("partition_lower_bound")
            .description(
                    "The partition_column min value for scan, if not set SeaTunnel will query database get min value.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> PARTITION_NUM = new PropertyDescriptor.Builder<Integer>()
            .name("partition_num")
            .description("The number of partition count, only support positive integer. default value is job parallelism")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SPLIT_SIZE = new PropertyDescriptor.Builder<Integer>()
            .name("split.size")
            .description("How many rows in one split")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Double> SPLIT_EVEN_DISTRIBUTION_FACTOR_LOWER_BOUND = new PropertyDescriptor.Builder<Integer>()
            .name("split.even-distribution.factor.lower-bound")
            .description("The lower bound of the chunk key distribution factor")
            .type(PropertyType.INT)
            .parser(Parsers.DOUBLE_PARSER)
            .addValidator(Validators.DOUBLE_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Double> SPLIT_EVEN_DISTRIBUTION_FACTOR_UPPER_BOUND = new PropertyDescriptor.Builder<Integer>()
            .name("split.even-distribution.factor.upper-bound")
            .description("The upper bound of the chunk key distribution factor")
            .type(PropertyType.INT)
            .parser(Parsers.DOUBLE_PARSER)
            .addValidator(Validators.DOUBLE_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SPLIT_SAMPLE_SHARDING_THRESHOLD = new PropertyDescriptor.Builder<Integer>()
            .name("split.sample-sharding.threshold")
            .description("This configuration specifies the threshold of estimated shard count to trigger the sample sharding strategy")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SPLIT_INVERSE_SAMPLING_RATE = new PropertyDescriptor.Builder<Integer>()
            .name("split.inverse-sampling.rate")
            .description("The inverse of the sampling rate used in the sample sharding strategy.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> FETCH_SIZE = new PropertyDescriptor.Builder<Integer>()
            .name("fetch_size")
            .description("The number of records writen per batch")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(0)
            .addValidator(Validators.NON_NEGATIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

}
