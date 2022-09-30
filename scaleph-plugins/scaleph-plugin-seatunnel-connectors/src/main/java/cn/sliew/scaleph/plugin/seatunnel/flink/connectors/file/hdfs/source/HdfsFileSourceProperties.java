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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.hdfs.source;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.Property;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.PropertyType;
import cn.sliew.scaleph.plugin.framework.property.Validators;

public enum HdfsFileSourceProperties {
    ;


    public static final PropertyDescriptor<String> PATH = new PropertyDescriptor.Builder<String>()
        .name("path")
        .description("The source file path.")
        .type(PropertyType.STRING)
        .parser(Parsers.STRING_PARSER)
        .properties(Property.Required)
        .addValidator(Validators.NON_BLANK_VALIDATOR)
        .validateAndBuild();


    public static final PropertyDescriptor<String> TYPE = new PropertyDescriptor.Builder<String>()
        .name("type")
        .description("File type, supported as the following file types:text csv parquet orc json")
        .type(PropertyType.STRING)
        .parser(Parsers.STRING_PARSER)
        .allowableValues("text", "csv", "parquet", "orc", "json")
        .properties(Property.Required)
        .addValidator(Validators.NON_BLANK_VALIDATOR)
        .validateAndBuild();

    public static final PropertyDescriptor<String> FS_DEFAULT_FS = new PropertyDescriptor.Builder<String>()
        .name("fs.defaultFS")
        .description("Hdfs cluster address..")
        .type(PropertyType.STRING)
        .parser(Parsers.STRING_PARSER)
        .properties(Property.Required)
        .addValidator(Validators.NON_BLANK_VALIDATOR)
        .validateAndBuild();


    public static final PropertyDescriptor<String> SCHEMA = new PropertyDescriptor.Builder<String>()
        .name("schema")
        .description(
            "If you assign file type to json, you should also assign schema option to tell connector how to parse data to the row you want."
                + "If you assign file type to parquet orc, schema option not required, connector can find the schema of upstream data automaticall."
                + "If you assign file type to text csv, schema option not supported temporarily, but the subsequent features will support.")
        .type(PropertyType.STRING)
        .parser(Parsers.STRING_PARSER)
        .addValidator(Validators.NON_BLANK_VALIDATOR)
        .validateAndBuild();

}
