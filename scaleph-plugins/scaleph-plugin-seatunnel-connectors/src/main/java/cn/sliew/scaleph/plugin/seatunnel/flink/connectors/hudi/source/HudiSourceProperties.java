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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.hudi.source;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum HudiSourceProperties {
    ;

    public static final PropertyDescriptor<String> TABLE_PATH = new PropertyDescriptor.Builder<String>()
            .name("table.path")
            .description("The hdfs root path of hudi table,such as 'hdfs://nameserivce/data/hudi/hudi_table/'.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TABLE_TYPE = new PropertyDescriptor.Builder<String>()
            .name("table.type")
            .description("The type of hudi table. Now we only support 'cow', 'mor' is not support yet.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .allowableValues("cow")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CONF_FILES = new PropertyDescriptor.Builder<String>()
            .name("conf.files")
            .description("The environment conf file path list(local path), which used to init hdfs client to read hudi table file. The example is '/home/test/hdfs-site.xml;/home/test/core-site.xml;/home/test/yarn-site.xml'.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> USE_KERBEROS = new PropertyDescriptor.Builder<Boolean>()
            .name("use.kerberos")
            .description("Whether to enable Kerberos, default is false.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .defaultValue("false")
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> KERBEROS_PRINCIPAL = new PropertyDescriptor.Builder<String>()
            .name("kerberos.principal")
            .description("When use kerberos, we should set kerberos princal such as 'test_user@xxx'.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> KERBEROS_PRINCIPAL_FILE = new PropertyDescriptor.Builder<String>()
            .name("kerberos.principal.file")
            .description("When use kerberos, we should set kerberos princal file such as '/home/test/test_user.keytab'.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
