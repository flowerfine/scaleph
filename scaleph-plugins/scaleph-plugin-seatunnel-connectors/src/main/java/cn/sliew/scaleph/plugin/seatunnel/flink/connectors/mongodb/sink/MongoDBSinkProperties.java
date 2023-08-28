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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.mongodb.sink;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.PropertyType;
import cn.sliew.scaleph.plugin.framework.property.Validators;

import java.util.Collections;
import java.util.List;

public enum MongoDBSinkProperties {
    ;

    public static final PropertyDescriptor<Integer> BUFFER_FLUSH_MAX_ROWS = new PropertyDescriptor.Builder()
            .name("buffer-flush.max-rows")
            .description("Specifies the maximum number of buffered rows per batch request.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(1000)
            .addValidator(Validators.INTEGER_VALIDATOR, Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> BUFFER_FLUSH_INTERVAL = new PropertyDescriptor.Builder()
            .name("buffer-flush.interval")
            .description("Specifies the maximum interval of buffered rows per batch request, the unit is millisecond.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(30000)
            .addValidator(Validators.LONG_VALIDATOR, Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RETRY_MAX = new PropertyDescriptor.Builder()
            .name("retry.max")
            .description("Specifies the max number of retry if writing records to database failed.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(3)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RETRY_INTERVAL = new PropertyDescriptor.Builder()
            .name("retry.interval")
            .description("Specifies the retry time interval if writing records to database failed, the unit is millisecond.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .defaultValue(1000)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> UPSERT_ENABLE = new PropertyDescriptor.Builder<>()
            .name("upsert-enable")
            .description("Whether to write documents via upsert mode.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<List<String>> PRIMARY_KEY = new PropertyDescriptor.Builder<>()
            .name("primary-key")
            .description("The primary keys for upsert/update. Keys are in [\"id\",\"name\",...] format for properties.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.STRING_ARRAY_PARSER)
            .defaultValue(Collections.EMPTY_LIST)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> TRANSACTION = new PropertyDescriptor.Builder<>()
            .name("transaction")
            .description("Whether to use transactions in MongoSink (requires MongoDB 4.2+).")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue(false)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

}
