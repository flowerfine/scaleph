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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.hbase.sink;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum HbaseSinkProperties {
    ;

    public static final PropertyDescriptor<JsonNode> ROWKEY_COLUMN = new PropertyDescriptor.Builder()
            .name("rowkey_column")
            .description("The column name list of row keys")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> FAMILY_NAME = new PropertyDescriptor.Builder()
            .name("family_name")
            .description("The family name mapping of fields")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> ROWKEY_DELIMITER = new PropertyDescriptor.Builder()
            .name("rowkey_delimiter")
            .description("The delimiter of joining multi row keys, default \"\"")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> VERSION_COLUMN = new PropertyDescriptor.Builder()
            .name("version_column")
            .description("The version column name, you can use it to assign timestamp for hbase record")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> NULL_MODE = new PropertyDescriptor.Builder()
            .name("null_mode")
            .description("The mode of writing null value")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> WAL_WRITE = new PropertyDescriptor.Builder()
            .name("wal_write")
            .description("The wal log write flag")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> WRITE_BUFFER_SIZE = new PropertyDescriptor.Builder()
            .name("write_buffer_size")
            .description("The write buffer size of hbase client")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(1024 * 1024 * 8)
            .validateAndBuild();

    public static final PropertyDescriptor<String> ENCODING = new PropertyDescriptor.Builder()
            .name("encoding")
            .description("The encoding of string field")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> HBASE_EXTRA_CONFIG = new PropertyDescriptor.Builder()
            .name("hbase_extra_config")
            .description("The extra configuration of hbase")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
