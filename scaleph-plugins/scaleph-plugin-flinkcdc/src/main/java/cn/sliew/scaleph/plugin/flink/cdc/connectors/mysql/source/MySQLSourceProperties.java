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

package cn.sliew.scaleph.plugin.flink.cdc.connectors.mysql.source;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public enum MySQLSourceProperties {
    ;

    public static final PropertyDescriptor<String> HOSTNAME = new PropertyDescriptor.Builder()
            .name("hostname")
            .description("IP address or hostname of the MySQL database server.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> PORT = new PropertyDescriptor.Builder()
            .name("port")
            .description("Integer port number of the MySQL database server.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(3306)
            .validateAndBuild();

    public static final PropertyDescriptor<String> USERNAME = new PropertyDescriptor.Builder()
            .name("username")
            .description("Name of the MySQL database to use when connecting to the MySQL database server.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PASSWORD = new PropertyDescriptor.Builder()
            .name("password")
            .description("Password to use when connecting to the MySQL database server.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TABLES = new PropertyDescriptor.Builder()
            .name("tables")
            .description("Table name of the MySQL database to monitor. The table-name also supports regular expressions to monitor multiple tables that satisfy the regular expressions.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> SCHEMA_CHANGE_ENABLED = new PropertyDescriptor.Builder()
            .name("schema-change.enabled")
            .description("Whether to send schema change events, so that downstream sinks can respond to schema changes and achieve table structure synchronization.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .defaultValue(true)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SERVER_ID = new PropertyDescriptor.Builder()
            .name("server-id")
            .description("A numeric ID or a numeric ID range of this database client, The numeric ID syntax is like '5400', the numeric ID range syntax is like '5400-5408', The numeric ID range syntax is recommended when 'scan.incremental.snapshot.enabled' enabled")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> SCAN_INCREMENTAL_CLOSE_IDLE_READER_ENABLED = new PropertyDescriptor.Builder()
            .name("scan.incremental.close-idle-reader.enabled")
            .description("Whether to close idle readers at the end of the snapshot phase.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .defaultValue(false)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SCAN_INCREMENTAL_SNAPSHOT_CHUNK_SIZE = new PropertyDescriptor.Builder()
            .name("scan.incremental.snapshot.chunk.size")
            .description("The chunk size (number of rows) of table snapshot, captured tables are split into multiple chunks when read the snapshot of table.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(8096)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SCAN_SNAPSHOT_FETCH_SIZE = new PropertyDescriptor.Builder()
            .name("scan.snapshot.fetch.size")
            .description("The maximum fetch size for per poll when read table snapshot.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(1024)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SCAN_STARTUP_MODE = new PropertyDescriptor.Builder()
            .name("scan.startup.mode")
            .description("Optional startup mode for MySQL CDC consumer")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .allowableValues("initial", "earliest-offset", "latest-offset", "specific-offset", "timestamp")
            .defaultValue("initial")
            .validateAndBuild();

    public static final PropertyDescriptor<String> SCAN_STARTUP_SPECIFIC_OFFSET_FILE = new PropertyDescriptor.Builder()
            .name("scan.startup.specific-offset.file")
            .description("Optional binlog file name used in case of \"specific-offset\" startup mode")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> SCAN_STARTUP_SPECIFIC_OFFSET_POS = new PropertyDescriptor.Builder()
            .name("scan.startup.specific-offset.pos")
            .description("Optional binlog file position used in case of \"specific-offset\" startup mode")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SCAN_STARTUP_SPECIFIC_OFFSET_GTID_SET = new PropertyDescriptor.Builder()
            .name("scan.startup.specific-offset.gtid-set")
            .description("Optional GTID set used in case of \"specific-offset\" startup mode")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> SCAN_STARTUP_SPECIFIC_OFFSET_SKIP_EVENTS = new PropertyDescriptor.Builder()
            .name("scan.startup.specific-offset.skip-events")
            .description("Optional number of events to skip after the specific starting offset")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> SCAN_STARTUP_SPECIFIC_OFFSET_SKIP_ROWS = new PropertyDescriptor.Builder()
            .name("scan.startup.specific-offset.skip-rows")
            .description("Optional number of rows to skip after the specific starting offset")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CONNECT_TIMEOUT = new PropertyDescriptor.Builder()
            .name("connect.timeout")
            .description("The maximum time that the connector should wait after trying to connect to the MySQL database server before timing out.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .defaultValue("30s")
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> CONNECT_MAX_RETRIES = new PropertyDescriptor.Builder()
            .name("connect.max-retries")
            .description("Optional number of rows to skip after the specific starting offset")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(3)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> CONNECT_POOL_SIZE = new PropertyDescriptor.Builder()
            .name("connection.pool.size")
            .description("The connection pool size.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(20)
            .validateAndBuild();

    public static final PropertyDescriptor<String> HEARTBEAT_INTERVAL = new PropertyDescriptor.Builder()
            .name("heartbeat.interval")
            .description("The interval of sending heartbeat event for tracing the latest available binlog offsets.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .defaultValue("30s")
            .validateAndBuild();

    public static final PropertyDescriptor<ObjectNode> JDBC_PROPERTIES = new PropertyDescriptor.Builder()
            .name("jdbc.properties.")
            .description("Option to pass custom JDBC URL properties")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<ObjectNode> DEBEZIUM = new PropertyDescriptor.Builder()
            .name("debezium.")
            .description("Pass-through Debezium's properties to Debezium Embedded Engine which is used to capture data changes from MySQL server")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
