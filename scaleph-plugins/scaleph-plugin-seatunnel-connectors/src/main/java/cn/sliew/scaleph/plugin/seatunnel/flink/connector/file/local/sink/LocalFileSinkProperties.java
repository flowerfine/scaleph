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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.file.local.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum LocalFileSinkProperties {
    ;

    public static final PropertyDescriptor<String> PATH = new PropertyDescriptor.Builder<String>()
            .name("path")
            .description("The target dir path started with file://")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FILE_NAME_EXPRESSION = new PropertyDescriptor.Builder<String>()
            .name("file_name_expression")
            .description("file_name_expression describes the file expression which will be created into the path. We can add the variable ${now} or ${uuid} in the file_name_expression, like test_${uuid}_${now}, ${now} represents the current time, and its format can be defined by specifying the option filename_time_format.\n" +
                    "\n" +
                    "Please note that, If is_enable_transaction is true, we will auto add ${transactionId}_ in the head of the file.")
            .type(PropertyType.STRING)
            .defaultValue("${transactionId}")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FILE_FORMAT = new PropertyDescriptor.Builder<String>()
            .name("file_format")
            .description("We supported as the following file types:text, csv, parquet, orc, json")
            .type(PropertyType.STRING)
            .defaultValue("text")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FILENAME_TIME_FORMAT = new PropertyDescriptor.Builder<String>()
            .name("filename_time_format")
            .description("When the format in the file_name_expression parameter is xxxx-${now} , filename_time_format can specify the time format of the path")
            .type(PropertyType.STRING)
            .defaultValue("yyyy.MM.dd")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FIELD_DELIMITER = new PropertyDescriptor.Builder<String>()
            .name("field_delimiter")
            .description("The separator between columns in a row of data. Only needed by text and csv file format")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> ROW_DELIMITER = new PropertyDescriptor.Builder<String>()
            .name("row_delimiter")
            .description("he separator between rows in a file. Only needed by text and csv file format.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PARTITION_BY = new PropertyDescriptor.Builder<String>()
            .name("partition_by")
            .description("Partition data based on selected fields")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();


    public static final PropertyDescriptor<String> PARTITION_DIR_EXPRESSION = new PropertyDescriptor.Builder<String>()
            .name("partition_dir_expression")
            .description("If the partition_by is specified, we will generate the corresponding partition directory based on the partition information, and the final file will be placed in the partition directory.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SINK_COLUMNS = new PropertyDescriptor.Builder<String>()
            .name("sink_columns")
            .description("Which columns need be write to file, default value is all of the columns get from Transform or Source. The order of the fields determines the order in which the file is actually written.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> IS_ENABLE_TRANSACTION = new PropertyDescriptor.Builder<Boolean>()
            .name("is_enable_transaction")
            .description("If is_enable_transaction is true, we will ensure that data will not be lost or duplicated when it is written to the target directory")
            .type(PropertyType.BOOLEAN)
            .allowableValues("true")
            .defaultValue("true")
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> SAVE_MODE = new PropertyDescriptor.Builder<Boolean>()
            .name("save_mode")
            .description("Storage mode, currently supports overwrite")
            .type(PropertyType.BOOLEAN)
            .allowableValues("true")
            .defaultValue("overwrite")
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

}
