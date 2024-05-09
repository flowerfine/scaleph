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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.iceberg.sink;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum IcebergSinkProperties {
    ;

    public static final PropertyDescriptor<JsonNode> ICEBERG_TABEL_WRITE_PROPS = new PropertyDescriptor.Builder()
            .name("iceberg.table.write-props")
            .description("Properties passed through to Iceberg writer initialization.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> ICEBERG_TABEL_AUTO_CREATE_PROPS = new PropertyDescriptor.Builder()
            .name("iceberg.table.auto-create-props")
            .description("Configuration specified by Iceberg during automatic table creation.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<String> ICEBERG_TABEL_PRIMARY_KEYS = new PropertyDescriptor.Builder<String>()
            .name("iceberg.table.primary-keys")
            .description("Default comma-separated list of columns that identify a row in tables (primary key).")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> ICEBERG_TABEL_PARTITION_KEYS = new PropertyDescriptor.Builder<String>()
            .name("iceberg.table.partition-keys")
            .description("Default comma-separated list of partition fields to use when creating tables")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> ICEBERG_TABEL_SCHEMA_EVOLUTION_ENABLED = new PropertyDescriptor.Builder<Boolean>()
            .name("iceberg.table.schema-evolution-enabled")
            .description("Setting to true enables Iceberg tables to support schema evolution during the synchronization process")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> ICEBERG_TABEL_UPSERT_MODE_ENABLED = new PropertyDescriptor.Builder<Boolean>()
            .name("iceberg.table.upsert-mode-enabled")
            .description("Set to true to enable upsert mode, default is false")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> ICEBERG_TABEL_COMMIT_BRANCH = new PropertyDescriptor.Builder<String>()
            .name("iceberg.table.commit-branch")
            .description("Default branch for commits")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();
}
