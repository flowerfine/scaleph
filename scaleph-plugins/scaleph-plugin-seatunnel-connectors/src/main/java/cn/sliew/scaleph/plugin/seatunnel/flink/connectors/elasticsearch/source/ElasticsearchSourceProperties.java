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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.elasticsearch.source;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.PropertyType;
import cn.sliew.scaleph.plugin.framework.property.Validators;
import com.fasterxml.jackson.databind.JsonNode;

public enum ElasticsearchSourceProperties {
    ;

    public static final PropertyDescriptor<JsonNode> INDEX_LIST = new PropertyDescriptor.Builder()
            .name("index_list")
            .description("The index_list is used to define multi-index synchronization tasks")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> SOURCE = new PropertyDescriptor.Builder()
            .name("source")
            .description("The fields of index.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> ARRAY_COLUMN = new PropertyDescriptor.Builder()
            .name("array_column")
            .description("The fields of array type")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> QUERY = new PropertyDescriptor.Builder()
            .name("query")
            .description("Elasticsearch DSL. You can control the range of data read.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SCROLL_TIME = new PropertyDescriptor.Builder()
            .name("scroll_time")
            .description("Amount of time Elasticsearch will keep the search context alive for scroll requests.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .defaultValue("1m")
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SCROLL_SIZE = new PropertyDescriptor.Builder()
            .name("scroll_size")
            .description("Maximum number of hits to be returned with each Elasticsearch scroll request.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(100)
            .validateAndBuild();

    /**
     * fixme support schema?
     */
    public static final PropertyDescriptor<JsonNode> SCHEMA = new PropertyDescriptor.Builder()
            .name("schema")
            .description("The structure of the data, including field names and field types")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
