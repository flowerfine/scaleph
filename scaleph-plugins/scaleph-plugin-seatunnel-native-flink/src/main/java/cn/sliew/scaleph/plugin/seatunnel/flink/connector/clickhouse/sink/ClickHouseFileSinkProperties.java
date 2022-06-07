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

package cn.sliew.scaleph.plugin.seatunnel.flink.connector.clickhouse.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum ClickHouseFileSinkProperties {
    ;

    public static final PropertyDescriptor<String> SHARDING_KEY = new PropertyDescriptor.Builder<String>()
            .name("sharding_key")
            .description("When use split_mode, which node to send data to is a problem, the default is random selection, but the 'sharding_key' parameter can be used to specify the field for the sharding algorithm. " +
                    "This option only worked when 'split_mode' is true.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CLICKHOUSE_LOCAL_PATH = new PropertyDescriptor.Builder<String>()
            .name("clickhouse_local_path")
            .description("The address of the clickhouse-local. Since each task needs to be called, clickhouse-local should be located in the same path of each node.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> TMP_BATCH_CACHE_LINE = new PropertyDescriptor.Builder<Integer>()
            .name("tmp_batch_cache_line")
            .description("SeaTunnel will use memory map technology to write temporary data to the file to cache the data that the user needs to write to clickhouse. " +
                    "This parameter is used to configure the number of data pieces written to the file each time. " +
                    "Most of the time you don't need to modify it.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> COPY_METHOD = new PropertyDescriptor.Builder<String>()
            .name("copy_method")
            .description("Specifies the method used to transfer files, the default is scp, optional scp and rsync")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> NODE_FREE_PASSWORD = new PropertyDescriptor.Builder<Boolean>()
            .name("node_free_password")
            .description("Because seatunnel need to use scp or rsync for file transfer, seatunnel need clickhouse server-side access. " +
                    "If each spark node and clickhouse server are configured with password-free login, you can configure this option to true, otherwise you need to configure the corresponding node password in the node_pass configuration")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> NODE_PASS = new PropertyDescriptor.Builder<String>()
            .name("node_pass")
            .description("Because seatunnel need to use scp or rsync for file transfer, seatunnel need clickhouse server-side access. " +
                    "If each spark node and clickhouse server are configured with password-free login, you can configure this option to true, otherwise you need to configure the corresponding node password in the node_pass configuration")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> NODE_PASS_NODE_ADDRESS = new PropertyDescriptor.Builder<String>()
            .name("node_pass.node_address")
            .description("The address corresponding to the clickhouse server")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> NODE_PASS_NODE_PASSWORD = new PropertyDescriptor.Builder<String>()
            .name("node_pass.node_password")
            .description("The password corresponding to the clickhouse server, only support root user yet.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();


}
