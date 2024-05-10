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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.cdc.mongodb.source;

import cn.sliew.scaleph.plugin.framework.property.*;

import java.util.List;

public enum MongoDBCDCSourceProperties {
    ;

    public static final PropertyDescriptor<String> HOSTS = new PropertyDescriptor.Builder()
            .name("hosts")
            .description("The comma-separated list of hostname and port pairs of the MongoDB servers.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> USERNAME = new PropertyDescriptor.Builder()
            .name("username")
            .description("Name of the database user to be used when connecting to MongoDB")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PASSWORD = new PropertyDescriptor.Builder()
            .name("password")
            .description("Password to be used when connecting to MongoDB")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<List<String>> DATABASE = new PropertyDescriptor.Builder()
            .name("database")
            .description("Name of the database to watch for changes")
            .type(PropertyType.OBJECT)
            .parser(Parsers.STRING_ARRAY_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<List<String>> COLLECTION = new PropertyDescriptor.Builder()
            .name("collection")
            .description("Name of the collection in the database to watch for changes")
            .type(PropertyType.OBJECT)
            .parser(Parsers.STRING_ARRAY_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CONNECTION_OPTIONS = new PropertyDescriptor.Builder()
            .name("connection.options")
            .description("The ampersand-separated connection options of MongoDB")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> BATCH_SIZE = new PropertyDescriptor.Builder()
            .name("batch.size")
            .description("The cursor batch size")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(1024)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> POLL_MAX_BATCH_SIZE = new PropertyDescriptor.Builder()
            .name("poll.max.batch.size")
            .description("Maximum number of change stream documents to include in a single batch when polling for new data")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(1024)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> POLL_AWAIT_TIME_MS = new PropertyDescriptor.Builder()
            .name("poll.await.time.ms")
            .description("The amount of time to wait before checking for new results on the change stream.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(1000)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> HEARTBEAT_INTERVAL_MS = new PropertyDescriptor.Builder()
            .name("heartbeat.interval.ms")
            .description("The length of time in milliseconds between sending heartbeat messages. Use 0 to disable.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(0)
            .addValidator(Validators.NON_NEGATIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> INCREMENTAL_SNAPSHOT_CHUNK_SIZE_MB = new PropertyDescriptor.Builder()
            .name("incremental.snapshot.chunk.size.mb")
            .description("The chunk size mb of incremental snapshot.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(64)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();
}
