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
import com.fasterxml.jackson.databind.JsonNode;

public enum JdbcSinkProperties {
    ;

    public static final PropertyDescriptor<String> DATABASE = new PropertyDescriptor.Builder<String>()
            .name("database")
            .description("Use this database and table-name auto-generate sql and receive upstream input datas write to database")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TABLE = new PropertyDescriptor.Builder<String>()
            .name("table")
            .description("Use this database and table-name auto-generate sql and receive upstream input datas write to database")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> SUPPORT_UPSERT_BY_QUERY_PRIMARY_KEY_EXIST = new PropertyDescriptor.Builder<Boolean>()
            .name("support_upsert_by_query_primary_key_exist")
            .description("Choose to use INSERT sql, UPDATE sql to process update events(INSERT, UPDATE_AFTER) based on query primary key exists")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> GENERATE_SINK_SQL = new PropertyDescriptor.Builder<Boolean>()
            .name("generate_sink_sql")
            .description("Generate sql statements based on the database table you want to write to")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<JsonNode> PRIMARY_KEYS = new PropertyDescriptor.Builder<JsonNode>()
            .name("primary_keys")
            .description("This option is used to support operations such as insert, delete, and update when automatically generate sql.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.JSON_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> QUERY = new PropertyDescriptor.Builder<String>()
            .name("query")
            .description("upsert statement")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
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

    public static final PropertyDescriptor<Boolean> AUTO_COMMIT = new PropertyDescriptor.Builder<Boolean>()
            .name("auto_commit")
            .description("Automatic transaction commit is enabled by default")
            .type(PropertyType.BOOLEAN)
            .defaultValue(true)
            .parser(Parsers.BOOLEAN_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FIELD_IDE = new PropertyDescriptor.Builder<Boolean>()
            .name("field_ide")
            .description("The field \"field_ide\" is used to identify whether the field needs to be converted to uppercase or lowercase when synchronizing from the source to the sink")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SCHEMA_SAVE_MODE = new PropertyDescriptor.Builder<Boolean>()
            .name("schema_save_mode")
            .description("Before the synchronous task is turned on, different treatment schemes are selected for the existing surface structure of the target side.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DATA_SAVE_MODE = new PropertyDescriptor.Builder<Boolean>()
            .name("data_save_mode")
            .description("Before the synchronous task is turned on, different processing schemes are selected for data existing data on the target side.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CUSTOM_SQL = new PropertyDescriptor.Builder<Boolean>()
            .name("custom_sql")
            .description("When data_save_mode selects CUSTOM_PROCESSING, you should fill in the CUSTOM_SQL parameter.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> ENABLE_UPSERT = new PropertyDescriptor.Builder<Boolean>()
            .name("enable_upsert")
            .description("Enable upsert by primary_keys exist.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> USE_COPY_STATEMENT = new PropertyDescriptor.Builder<Boolean>()
            .name("use_copy_statement")
            .description("Use COPY ${table} FROM STDIN statement to import data. Only drivers with getCopyAPI() method connections are supported. e.g.: Postgresql driver org.postgresql.Driver..")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .validateAndBuild();
}
