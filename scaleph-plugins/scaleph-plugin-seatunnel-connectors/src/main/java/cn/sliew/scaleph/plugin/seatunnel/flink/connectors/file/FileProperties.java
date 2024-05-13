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

public enum FileProperties {
    ;

    public static final PropertyDescriptor<String> PATH = new PropertyDescriptor.Builder<String>()
            .name("path")
            .description("The source dir path started with file://")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SHEET_NAME = new PropertyDescriptor.Builder()
            .name("sheet_name")
            .description("Reader the sheet of the workbook,Only used when file_format is excel.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> XML_ROOT_TAG = new PropertyDescriptor.Builder()
            .name("xml_root_tag")
            .description("Specifies the tag name of the data rows within the XML file, only used when file_format is xml.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> XML_ROW_TAG = new PropertyDescriptor.Builder()
            .name("xml_row_tag")
            .description("Specifies the tag name of the data rows within the XML file, only used when file_format is xml.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> XML_USE_ATTR_FORMAT = new PropertyDescriptor.Builder()
            .name("xml_use_attr_format")
            .description("Specifies whether to process data using the tag attribute format, only used when file_format is xml.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FIELD_DELIMITER = new PropertyDescriptor.Builder<String>()
            .name("field_delimiter")
            .description("The separator between columns in a row of data. Only needed by text and csv file format")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> COMPRESS_CODEC = new PropertyDescriptor.Builder()
            .name("compress_codec")
            .description("The compress codec of files")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> ENCODING = new PropertyDescriptor.Builder()
            .name("encoding")
            .description("Only used when file_format_type is json,text,csv,xml")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();
}
