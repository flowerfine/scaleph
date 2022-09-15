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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.jdbc.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum JdbcSinkProperties {
    ;

    public static final PropertyDescriptor<String> QUERY = new PropertyDescriptor.Builder<String>()
            .name("query")
            .description("upsert statement")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_RETRIES = new PropertyDescriptor.Builder<Integer>()
            .name("max_retries")
            .description("The number of retries to submit failed (executeBatch)")
            .type(PropertyType.INT)
            .defaultValue(3)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> BATCH_SIZE = new PropertyDescriptor.Builder<Integer>()
            .name("batch_size")
            .description("The number of records writen per batch")
            .type(PropertyType.INT)
            .defaultValue(300)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> BATCH_INTERVAL_MS = new PropertyDescriptor.Builder<Integer>()
            .name("batch_interval_ms")
            .description("For batch writing, when the number of buffers reaches the number of batch_size or the time reaches batch_interval_ms, the data will be flushed into the database")
            .type(PropertyType.INT)
            .defaultValue(1000)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> IS_EXACTLY_ONCE = new PropertyDescriptor.Builder<Boolean>()
            .name("is_exactly_once")
            .description("Whether to enable exactly-once semantics, which will use Xa transactions. If on, you need to set xa_data_source_class_name.")
            .type(PropertyType.BOOLEAN)
            .defaultValue(false)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> XA_DATA_SOURCE_CLASS_NAME = new PropertyDescriptor.Builder<String>()
            .name("xa_data_source_class_name")
            .description("The xa data source class name of the database Driver, for example, mysql is com.mysql.cj.jdbc.MysqlXADataSource and postgresql is org.postgresql.xa.PGXADataSource")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_COMMIT_ATTEMPTS = new PropertyDescriptor.Builder<Integer>()
            .name("max_commit_attempts")
            .description("The xa data source class name of the database Driver, for example, mysql is com.mysql.cj.jdbc.MysqlXADataSource and postgresql is org.postgresql.xa.PGXADataSource")
            .type(PropertyType.INT)
            .defaultValue(3)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> TRANSACTION_TIMEOUT_SEC = new PropertyDescriptor.Builder<Integer>()
            .name("transaction_timeout_sec")
            .description("The timeout after the transaction is opened, the default is -1 (never timeout). Note that setting the timeout may affect exactly-once semantics")
            .type(PropertyType.INT)
            .defaultValue(-1)
            .parser(Parsers.INTEGER_PARSER)
            .validateAndBuild();
}
