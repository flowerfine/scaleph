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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kudu.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum KuduSinkProperties {
    ;

    public static final PropertyDescriptor<String> SAVE_MODE = new PropertyDescriptor.Builder()
            .name("save_mode")
            .description("Storage mode, we need support overwrite and append")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SESSION_FLUSH_MODE = new PropertyDescriptor.Builder()
            .name("session_flush_mode")
            .description("Kudu flush mode")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> BATCH_SIZE = new PropertyDescriptor.Builder()
            .name("batch_size")
            .description("The flush max size (includes all append, upsert and delete records), over this number of records, will flush data. The default value is 100")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> BUFFER_FLUSH_INTERVAL = new PropertyDescriptor.Builder()
            .name("buffer_flush_interval")
            .description("The flush interval mills, over this time, asynchronous threads will flush data.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> IGNORE_NOT_FOUND = new PropertyDescriptor.Builder()
            .name("ignore_not_found")
            .description("If true, ignore all not found rows.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> IGNORE_NOT_DUPLICATE = new PropertyDescriptor.Builder()
            .name("ignore_not_duplicate")
            .description("If true, ignore all dulicate rows.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();
}
