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

package cn.sliew.scaleph.plugin.datasource.jdbc;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum JdbcPoolProperties {
    ;

    public static final PropertyDescriptor<String> JDBC_URL = new PropertyDescriptor.Builder()
            .name("jdbcUrl")
            .description("database connection url")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DRIVER_CLASS_NAME = new PropertyDescriptor.Builder()
            .name("driverClassName")
            .description("fully-qualified class name of the JDBC driver. Example: com.mysql.cj.jdbc.Driver")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<String> USERNAME = new PropertyDescriptor.Builder()
            .name("username")
            .description("database username")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PASSWORD = new PropertyDescriptor.Builder()
            .name("password")
            .description("password for the database user")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .properties(Property.Required, Property.Sensitive)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MININUM_IDLE = new PropertyDescriptor.Builder()
            .name("minimumIdle")
            .description(
                    "This property controls the minimum number of idle connections that HikariCP tries to maintain in the pool. If the idle connections dip below this value and total "
                            + "connections in the pool are less than 'Max Total Connections', HikariCP will make a best effort to add additional connections quickly and efficiently. It is recommended "
                            + "that this property to be set equal to 'Max Total Connections'.")
            .type(PropertyType.INT)
            .defaultValue(descriptor -> "10")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAXIMUM_POOL_SIZE = new PropertyDescriptor.Builder()
            .name("maximumPoolSize")
            .description(
                    "This property controls the maximum size that the pool is allowed to reach, including both idle and in-use connections. Basically this value will determine the "
                            + "maximum number of actual connections to the database backend. A reasonable value for this is best determined by your execution environment. When the pool reaches "
                            + "this size, and no idle connections are available, the service will block for up to connectionTimeout milliseconds before timing out.")
            .type(PropertyType.INT)
            .defaultValue(descriptor -> "10")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> IDLE_TIMEOUT = new PropertyDescriptor.Builder()
            .name("idleTimeout")
            .description("This property controls the maximum amount of time that a connection is allowed to sit idle in the pool")
            .type(PropertyType.INT)
            .defaultValue(descriptor -> "600000")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<String> VALIDATION_QUERY = new PropertyDescriptor.Builder()
            .name("connectionInitSql")
            .description("Validation Query used to validate connections before returning them. "
                    + "When connection is invalid, it gets dropped and new valid connection will be returned. "
                    + "NOTE: Using validation might have some performance penalty.")
            .type(PropertyType.STRING)
            .defaultValue(descriptor -> "SELECT 1")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();
}
