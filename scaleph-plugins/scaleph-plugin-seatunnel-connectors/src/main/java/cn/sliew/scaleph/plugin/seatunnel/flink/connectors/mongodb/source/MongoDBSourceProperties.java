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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.mongodb.source;

import cn.sliew.scaleph.plugin.framework.property.*;
import com.fasterxml.jackson.databind.JsonNode;

@SuppressWarnings("unchecked")
public enum MongoDBSourceProperties {
    ;

    public static final PropertyDescriptor<JsonNode> SCHEMA = new PropertyDescriptor.Builder<>()
            .name("schema")
            .description("The schema information of upstream data.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> MATCH_QUERY = new PropertyDescriptor.Builder<>()
            .name("match.query")
            .description("match.query is a JSON string that specifies the selection criteria using query operators for the documents to be returned from the collection.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> MATCH_PROJECTION = new PropertyDescriptor.Builder<>()
            .name("match.projection")
            .description("In MongoDB, Projection is used to control the fields contained in the query results")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PARTITION_SPLIT_KEY = new PropertyDescriptor.Builder<>()
            .name("partition.split-key")
            .description("The key of Mongodb fragmentation.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .defaultValue("_id")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> PARTITION_SPLIT_SIZE = new PropertyDescriptor.Builder<>()
            .name("partition.split-size")
            .description("The size of Mongodb fragment.")
            .type(PropertyType.LONG)
            .parser(Parsers.LONG_PARSER)
            .defaultValue(67108864L) // default 64M
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> CURSOR_NO_TIMEOUT = new PropertyDescriptor.Builder<>()
            .name("cursor.no-timeout")
            .description("MongoDB server normally times out idle cursors after an inactivity period (10 minutes) to prevent excess memory use. " +
                    "Set this option to true to prevent that. However, " +
                    "if the application takes longer than 30 minutes to process the current batch of documents, " +
                    "the session is marked as expired and closed.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(true)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> FETCH_SIZE = new PropertyDescriptor.Builder<>()
            .name("fetch.size")
            .description("Set the number of documents obtained from the server for each batch." +
                    " Setting the appropriate batch size can improve query performance and avoid the memory pressure" +
                    " caused by obtaining a large amount of data at one time.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(2048)
            .addValidator(Validators.INTEGER_VALIDATOR, Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> MAX_TIME_MIN = new PropertyDescriptor.Builder<>()
            .name("max.time-min")
            .description("This parameter is a MongoDB query option that limits the maximum execution time for query operations. " +
                    "The value of maxTimeMin is in Minute." +
                    " If the execution time of the query exceeds the specified time limit, " +
                    "MongoDB will terminate the operation and return an error.")
            .type(PropertyType.LONG)
            .parser(Parsers.LONG_PARSER)
            .defaultValue(600L)
            .addValidator(Validators.LONG_VALIDATOR, Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> FLAT_SYNC_STRING = new PropertyDescriptor.Builder<>()
            .name("flat.sync-string")
            .description("By utilizing flatSyncString, only one field attribute value can be set," +
                    " and the field type must be a String." +
                    " This operation will perform a string mapping on a single MongoDB data entry.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(true)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();



}
