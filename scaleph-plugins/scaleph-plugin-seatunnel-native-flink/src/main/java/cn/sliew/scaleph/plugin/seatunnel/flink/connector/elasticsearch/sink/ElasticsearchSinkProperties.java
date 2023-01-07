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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.elasticsearch.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum ElasticsearchSinkProperties {
    ;

    public static final PropertyDescriptor<String> HOSTS = new PropertyDescriptor.Builder<String>()
            .name("hosts")
            .description("Elasticsearch cluster address, the format is host:port , allowing multiple hosts to be specified. Such as [\"host1:9200\", \"host2:9200\"]")
            .type(PropertyType.ARRAY)
            .parser(Parsers.STRING_ARRAY_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> INDEX_TYPE = new PropertyDescriptor.Builder<String>()
            .name("index_type")
            .description("Elasticsearch index type, it is recommended not to specify in elasticsearch 7 and above")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> INDEX = new PropertyDescriptor.Builder<String>()
            .name("index")
            .description("Elasticsearch index name. If you need to generate an index based on time, you can specify a time variable, such as seatunnel-${now} . now represents the current data processing time.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> INDEX_TIME_FORMAT = new PropertyDescriptor.Builder<String>()
            .name("index_time_format")
            .description("When the format in the index parameter is xxxx-${now} , index_time_format can specify the time format of the index name, and the default value is yyyy.MM.dd")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> PARALLELISM = new PropertyDescriptor.Builder()
            .name("parallelism")
            .description("The flink operator parallelism")
            .type(PropertyType.INT)
            .defaultValue(1)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

}
