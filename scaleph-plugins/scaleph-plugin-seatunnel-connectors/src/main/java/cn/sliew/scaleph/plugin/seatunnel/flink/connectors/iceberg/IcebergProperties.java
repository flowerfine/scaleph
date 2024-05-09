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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.iceberg;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

public enum IcebergProperties {
    ;

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

    public static final PropertyDescriptor<JsonNode> ICEBERG_CATALOG_CONFIG = new PropertyDescriptor.Builder()
            .name("iceberg.catalog.config")
            .description("Specify the properties for initializing the Iceberg catalog.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<String> ICEBERG_HADOOP_CONF_PATH = new PropertyDescriptor.Builder()
            .name("iceberg.hadoop-conf-path")
            .description("The specified loading paths for the 'core-site.xml', 'hdfs-site.xml', 'hive-site.xml' files.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> HADOOP_CONFIG = new PropertyDescriptor.Builder()
            .name("hadoop.config")
            .description("Properties passed through to the Hadoop configuration.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> CASE_SENSITIVE = new PropertyDescriptor.Builder<Boolean>()
            .name("case_sensitive")
            .description("If data columns where selected via fields(Collection), controls whether the match to the schema will be done with case sensitivity.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

}
