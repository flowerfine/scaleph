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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.doris.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum DorisSinkProperties {
    ;
    public static final PropertyDescriptor<String> FENODES = new PropertyDescriptor.Builder<String>()
            .name("fenodes")
            .description("Doris FE http address")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<String> USER = new PropertyDescriptor.Builder<String>()
            .name("user")
            .description("Doris username")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PASSWORD = new PropertyDescriptor.Builder<String>()
            .name("password")
            .description("Doris password")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DATABASE = new PropertyDescriptor.Builder<String>()
            .name("database")
            .description("Doris database name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TABLE = new PropertyDescriptor.Builder<String>()
            .name("table")
            .description("Doris table name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> BATCH_SIZE = new PropertyDescriptor.Builder<Integer>()
            .name("batch_size")
            .description("Maximum number of lines in a single write Doris,default value is 5000.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> INTERVAL = new PropertyDescriptor.Builder<Integer>()
            .name("interval")
            .description("The flush interval millisecond, after which the asynchronous thread will write the data in the cache to Doris." +
                    "Set to 0 to turn off periodic writing.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_RETRIES = new PropertyDescriptor.Builder<Integer>()
            .name("max_retries")
            .description("Number of retries after writing Doris failed")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DORIS_CONF = new PropertyDescriptor.Builder<String>()
            .name("doris_conf")
            .description("The doris stream load parameters.you can use 'doris.' prefix + stream_load properties.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> PARALLELISM = new PropertyDescriptor.Builder()
            .name("parallelism")
            .description("The flink operator parallelism")
            .type(PropertyType.INT)
            .defaultValue(1)
            .parser(Parsers.INTEGER_PARSER)
            .validateAndBuild();

}
