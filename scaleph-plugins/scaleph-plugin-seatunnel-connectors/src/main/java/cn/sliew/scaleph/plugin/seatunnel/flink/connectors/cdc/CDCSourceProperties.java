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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.cdc;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public enum CDCSourceProperties {
    ;

    public static final PropertyDescriptor<String> BASE_URL = new PropertyDescriptor.Builder()
            .name("base-url")
            .description("URL has to be with database, like \"jdbc:mysql://localhost:5432/db\" or \"jdbc:mysql://localhost:5432/db?useSSL=true\".")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> USERNAME = new PropertyDescriptor.Builder()
            .name("username")
            .description("Name of the database to use when connecting to the database server.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PASSWORD = new PropertyDescriptor.Builder()
            .name("password")
            .description("Password to use when connecting to the database server.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required, Property.Sensitive)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<List<String>> DATABASE = new PropertyDescriptor.Builder()
            .name("database-names")
            .description("Database name of the database to monitor.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.STRING_ARRAY_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<List<String>> TABLE = new PropertyDescriptor.Builder()
            .name("table-names")
            .description("Table name of the database to monitor.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.STRING_ARRAY_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> TABLE_CONFIG = new PropertyDescriptor.Builder()
            .name("table-names-config")
            .description("Table config list.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> STARTUP_MODE = new PropertyDescriptor.Builder()
            .name("startup.mode")
            .description("Optional startup mode for MySQL CDC consumer")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .allowableValues("initial", "earliest", "latest", "specific")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    /**
     * fixme support timestamp?
     */
    public static final PropertyDescriptor<Long> STARTUP_TIMESTAMP = new PropertyDescriptor.Builder()
            .name("startup.timestamp")
            .description("Start from the specified epoch timestamp (in milliseconds).")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> STARTUP_SPECIFIC_OFFSET_FILE = new PropertyDescriptor.Builder()
            .name("startup.specific-offset.file")
            .description("Start from the specified binlog file name.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> STARTUP_SPECIFIC_OFFSET_POS = new PropertyDescriptor.Builder()
            .name("startup.specific-offset.pos")
            .description("Start from the specified binlog file position.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> STOP_MODE = new PropertyDescriptor.Builder()
            .name("stop.mode")
            .description("Optional stop mode for MySQL CDC consumer")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .allowableValues("never")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> STOP_TIMESTAMP = new PropertyDescriptor.Builder()
            .name("stop.timestamp")
            .description("Stop from the specified epoch timestamp (in milliseconds).")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> STOP_SPECIFIC_OFFSET_FILE = new PropertyDescriptor.Builder()
            .name("stop.specific-offset.file")
            .description("Stop from the specified binlog file name.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> STOP_SPECIFIC_OFFSET_POS = new PropertyDescriptor.Builder()
            .name("stop.specific-offset.pos")
            .description("Stop from the specified binlog file position.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    /**
     * fixme does all jdbc cdc support this?
     */
    public static final PropertyDescriptor<Integer> INCREMENTAL_PARALLELISM = new PropertyDescriptor.Builder()
            .name("incremental.parallelism")
            .description("The number of parallel readers in the incremental phase.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SNAPSHOT_SPLIT_SIZE = new PropertyDescriptor.Builder()
            .name("snapshot.split.size")
            .description("The split size (number of rows) of table snapshot, captured tables are split into multiple splits when read the snapshot of table.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SNAPSHOT_FETCH_SIZE = new PropertyDescriptor.Builder()
            .name("snapshot.fetch.size")
            .description("The maximum fetch size for per poll when read table snapshot.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SERVER_ID = new PropertyDescriptor.Builder()
            .name("server-id")
            .description("A numeric ID or a numeric ID range of this database client")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SERVER_TIME_ZONE = new PropertyDescriptor.Builder()
            .name("server-time-zone")
            .description("The session time zone in database server.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> CONNECT_TIMEOUT = new PropertyDescriptor.Builder()
            .name("connect.timeout.ms")
            .description("The maximum time that the connector should wait after trying to connect to the database server before timing out.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> CONNECT_MAX_RETRIES = new PropertyDescriptor.Builder()
            .name("connect.max-retries")
            .description("The max retry times that the connector should retry to build database server connection.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> CONNECT_POOL_SIZE = new PropertyDescriptor.Builder()
            .name("connection.pool.size")
            .description("The connection pool size.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Double> CHUNK_KEY_EVEN_DISTRIBUTION_FACTOR_UPPER_BOUND = new PropertyDescriptor.Builder()
            .name("chunk-key.even-distribution.factor.upper-bound")
            .description("chunk-key.even-distribution.factor.upper-bound")
            .type(PropertyType.DOUBLE)
            .parser(Parsers.DOUBLE_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Double> CHUNK_KEY_EVEN_DISTRIBUTION_FACTOR_LOWER_BOUND = new PropertyDescriptor.Builder()
            .name("chunk-key.even-distribution.factor.lower-bound")
            .description("chunk-key.even-distribution.factor.lower-bound")
            .type(PropertyType.DOUBLE)
            .parser(Parsers.DOUBLE_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SAMPLE_SHARDING_THRESHOLD = new PropertyDescriptor.Builder()
            .name("sample-sharding.threshold")
            .description("This configuration specifies the threshold of estimated shard count to trigger the sample sharding strategy")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> INVERSE_SHARDING_RATE = new PropertyDescriptor.Builder()
            .name("inverse-sampling.rate")
            .description("The inverse of the sampling rate used in the sample sharding strategy")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> EXACTLY_ONCE = new PropertyDescriptor.Builder()
            .name("exactly_once")
            .description("Enable exactly once semantic.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> DEBEZIUM = new PropertyDescriptor.Builder()
            .name("debezium")
            .description("Pass-through Debezium's properties to Debezium Embedded Engine which is used to capture data changes from MySQL server.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FORMAT = new PropertyDescriptor.Builder()
            .name("format")
            .description("Optional output format for MySQL CDC")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .validateAndBuild();

}
