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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.doris.source;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.PropertyType;
import cn.sliew.scaleph.plugin.framework.property.Validators;
import com.fasterxml.jackson.databind.JsonNode;

public enum DorisSourceProperties {
    ;

    public static final PropertyDescriptor<String> DORIS_READ_FIELD = new PropertyDescriptor.Builder()
            .name("doris.read.field")
            .description("Use the 'doris.read.field' parameter to select the doris table columns to read")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DORIS_FILTER_QUERY = new PropertyDescriptor.Builder()
            .name("doris.filter.query")
            .description("Data filtering in doris. the format is \"field = value\"")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> DORIS_REQUEST_QUERY_TIMEOUT_S = new PropertyDescriptor.Builder()
            .name("doris.request.query.timeout.s")
            .description("Timeout period of Doris scan data, expressed in seconds.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> DORIS_EXEC_MEM_LIMIT = new PropertyDescriptor.Builder()
            .name("doris.exec.mem.limit")
            .description("Maximum memory that can be used by a single be scan request. The default memory is 2G (2147483648).")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> DORIS_REQUEST_RETRIES = new PropertyDescriptor.Builder()
            .name("doris.request.retries")
            .description("Number of retries to send requests to Doris FE.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> DORIS_REQUEST_READ_TIMEOUT_MS = new PropertyDescriptor.Builder()
            .name("doris.request.read.timeout.ms")
            .description("read timeout")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> DORIS_REQUEST_CONNECT_TIMEOUT_MS = new PropertyDescriptor.Builder()
            .name("doris.request.connect.timeout.ms")
            .description("connect timeout")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();
}
