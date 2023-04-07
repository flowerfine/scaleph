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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.starrocks.source;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.PropertyType;
import cn.sliew.scaleph.plugin.framework.property.Validators;
import com.fasterxml.jackson.databind.JsonNode;

public enum StarRocksSourceProperties {
    ;

    public static final PropertyDescriptor<JsonNode> SCHEMA = new PropertyDescriptor.Builder()
            .name("schema")
            .description("The schema of the starRocks that you want to generate")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SCAN_FILTER = new PropertyDescriptor.Builder()
            .name("scan_filter")
            .description("Filter expression of the query, which is transparently transmitted to StarRocks")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SCAN_CONNECT_TIMEOUT_MS = new PropertyDescriptor.Builder()
            .name("scan_connect_timeout_ms")
            .description("requests connection timeout sent to StarRocks")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(30000)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SCAN_QUERY_TIMEOUT_MS = new PropertyDescriptor.Builder()
            .name("scan_query_timeout_sec")
            .description("Query the timeout time of StarRocks, the default value is 1 hour, -1 means no timeout limit")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(3600)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SCAN_KEEP_ALIVE_MIN = new PropertyDescriptor.Builder()
            .name("scan_keep_alive_min")
            .description("The keep-alive duration of the query task, in minutes.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(10)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> SCAN_BATCH_ROWS = new PropertyDescriptor.Builder()
            .name("scan_batch_rows")
            .description("The maximum number of data rows to read from BE at a time.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(1024)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> SCAN_MEM_LIMIT = new PropertyDescriptor.Builder()
            .name("scan_mem_limit")
            .description("The maximum memory space allowed for a single query in the BE node, in bytes.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .defaultValue(2147483648L)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> REQUEST_TABLET_SIZE = new PropertyDescriptor.Builder()
            .name("request_tablet_size")
            .description("The number of StarRocks Tablets corresponding to an Partition")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(Integer.MAX_VALUE)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_RETRIES = new PropertyDescriptor.Builder()
            .name("max_retries")
            .description("number of retry requests sent to StarRocks")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue(3)
            .validateAndBuild();

}
