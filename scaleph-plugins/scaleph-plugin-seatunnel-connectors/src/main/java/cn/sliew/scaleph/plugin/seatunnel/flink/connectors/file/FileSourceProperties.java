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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum FileSourceProperties {
    ;

    public static final PropertyDescriptor<String> TYPE = new PropertyDescriptor.Builder<String>()
            .name("type")
            .description("We support the following file types: text, csv, parquet, orc, json")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .allowableValues("text", "csv", "parquet", "orc", "json")
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> SCHEMA = new PropertyDescriptor.Builder()
            .name("schema")
            .description("The schema information of upstream data.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DELIMITER = new PropertyDescriptor.Builder<String>()
            .name("delimiter")
            .description("The separator between columns in a row of data. Only needed by text and csv file format")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> PARSE_PARTITION_FROM_PATH = new PropertyDescriptor.Builder()
            .name("parse_partition_from_path")
            .description("Control whether parse the partition keys and values from file path")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue("true")
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DATE_FORMAT = new PropertyDescriptor.Builder<String>()
            .name("date_format")
            .description("Date type format, used to tell connector how to convert string to date")
            .type(PropertyType.STRING)
            .allowableValues("yyyy-MM-dd", "yyyy.MM.dd", "yyyy/MM/dd")
            .defaultValue("yyyy-MM-dd")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TIME_FORMAT = new PropertyDescriptor.Builder<String>()
            .name("time_format")
            .description("Time type format, used to tell connector how to convert string to time")
            .type(PropertyType.STRING)
            .allowableValues("HH:mm:ss", "HH:mm:ss.SSS")
            .defaultValue("HH:mm:ss")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DATETIME_FORMAT = new PropertyDescriptor.Builder<String>()
            .name("datetime_format")
            .description("Datetime type format, used to tell connector how to convert string to datetime")
            .type(PropertyType.STRING)
            .allowableValues("yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyyMMddHHmmss")
            .defaultValue("yyyy-MM-dd HH:mm:ss")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();
}
