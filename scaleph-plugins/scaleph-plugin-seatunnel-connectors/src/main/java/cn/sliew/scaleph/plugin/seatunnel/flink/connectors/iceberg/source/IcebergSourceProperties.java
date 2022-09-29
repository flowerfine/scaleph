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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.iceberg.source;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum IcebergSourceProperties {
    ;

    public static final PropertyDescriptor<String> CATALOG_TYPE = new PropertyDescriptor.Builder<String>()
            .name("catalog_type")
            .description("catalog type")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .allowableValues("hive", "hadoop")
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CATALOG_NAME = new PropertyDescriptor.Builder<String>()
            .name("catalog_name")
            .description("catalog name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> NAMESPACE = new PropertyDescriptor.Builder<String>()
            .name("namespace")
            .description("database name in the backend catalog")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TABLE = new PropertyDescriptor.Builder<String>()
            .name("table")
            .description("iceberg table name in the backend catalog")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();


    public static final PropertyDescriptor<String> URI = new PropertyDescriptor.Builder<String>()
            .name("uri")
            .description("hive metastore thrift uri")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> WAREHOUSE = new PropertyDescriptor.Builder<String>()
            .name("warehouse")
            .description("location for iceberg metadata and data files")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> CASE_SENSITIVE = new PropertyDescriptor.Builder<Boolean>()
            .name("case_sensitive")
            .description("If data columns where selected via fields(Collection), controls whether the match to the schema will be done with case sensitivity.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FIELDS = new PropertyDescriptor.Builder<Boolean>()
            .name("fields")
            .description("Use projection to select data columns and columns order.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> USE_SNAPSHOT_ID = new PropertyDescriptor.Builder<Boolean>()
            .name("use_snapshot_id")
            .description("Instructs this scan to look for use the given snapshot ID.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> START_SNAPSHOT_ID = new PropertyDescriptor.Builder<Long>()
            .name("start_snapshot_id")
            .description("Instructs this scan to look for changes starting from a particular snapshot (exclusive).")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> END_SNAPSHOT_ID = new PropertyDescriptor.Builder<Long>()
            .name("end_snapshot_id")
            .description("Instructs this scan to look for changes up to a particular snapshot (inclusive).")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> START_SNAPSHOT_TIMESTAMP = new PropertyDescriptor.Builder<Long>()
            .name("start_snapshot_timestamp")
            .description("Instructs this scan to look for changes starting from the most recent snapshot for the table as of the timestamp. timestamp – the timestamp in millis since the Unix epoch")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> USE_SNAPSHOT_TIMESTAMP = new PropertyDescriptor.Builder<Long>()
            .name("use_snapshot_timestamp")
            .description("Instructs this scan to look for use the most recent snapshot as of the given time in milliseconds. timestamp – the timestamp in millis since the Unix epoch")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> STREAM_SCAN_STRATEGY = new PropertyDescriptor.Builder<String>()
            .name("stream_scan_strategy")
            .description("Starting strategy for stream mode execution.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .defaultValue("FROM_LATEST_SNAPSHOT")
            .allowableValues("FROM_LATEST_SNAPSHOT", "FROM_EARLIEST_SNAPSHOT", "FROM_SNAPSHOT_ID", "FROM_SNAPSHOT_TIMESTAMP", "TABLE_SCAN_THEN_INCREMENTAL")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
