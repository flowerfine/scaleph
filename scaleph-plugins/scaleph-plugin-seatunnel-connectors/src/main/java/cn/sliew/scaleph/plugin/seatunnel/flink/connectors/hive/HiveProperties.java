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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.hive;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum HiveProperties {
    ;

    public static final PropertyDescriptor<String> TABLE_NAME = new PropertyDescriptor.Builder<String>()
            .name("table_name")
            .description("Target Hive table name eg: db1.table1")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> METASTORE_URI = new PropertyDescriptor.Builder<String>()
            .name("metastore_uri")
            .description("Hive metastore uri")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();


    public static final PropertyDescriptor<String> SCHEMA = new PropertyDescriptor.Builder<String>()
            .name("schema")
            .description("the schema fields of upstream data")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();


    public static final PropertyDescriptor<String> PARTITION_BY = new PropertyDescriptor.Builder<String>()
            .name("partition_by")
            .description(
                    "required if hive sink table have partitions,Partition data based on selected fields")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();


    public static final PropertyDescriptor<String> SINK_COLUMNS = new PropertyDescriptor.Builder<String>()
            .name("sink_columns")
            .description(
                    "Which columns need be write to hive, default value is all of the columns get from Transform or Source. "
                            + "The order of the fields determines the order in which the file is actually written.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> IS_ENABLE_TRANSACTION = new PropertyDescriptor.Builder<String>()
            .name("is_enable_transaction")
            .description(
                    "If is_enable_transaction is true, we will ensure that data will not be lost or duplicated when it is written to the target directory.\n"
                            + "Only support true now.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(true)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SAVE_MODE = new PropertyDescriptor.Builder<String>()
            .name("save_mode")
            .description(
                    "Storage mode, we need support overwrite and append. append is now supported.\n"
                            + "Streaming Job not support overwrite.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .defaultValue("append")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
