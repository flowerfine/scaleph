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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kudu;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum KuduProperties {
    ;
    public static final PropertyDescriptor<String> KUDU_MASTER = new PropertyDescriptor.Builder()
            .name("kudu_masters")
            .description("The address of kudu master")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> ENABLE_KERBEROS = new PropertyDescriptor.Builder()
            .name("enable_kerberos")
            .description("Kerberos principal enable.")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> KERBEROS_PRINCIPAL = new PropertyDescriptor.Builder()
            .name("kerberos_principal")
            .description("Kerberos principal.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> KERBEROS_KEYTAB = new PropertyDescriptor.Builder()
            .name("kerberos_keytab")
            .description("Kerberos keytab.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> KERBEROS_KRB5CONF = new PropertyDescriptor.Builder()
            .name("kerberos_krb5conf")
            .description("Kerberos krb5 conf.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TABLE_NAME = new PropertyDescriptor.Builder()
            .name("table_name")
            .description("The name of kudu table")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> CLIENT_WORKER_COUNT = new PropertyDescriptor.Builder()
            .name("client_worker_count")
            .description("Kudu worker count. Default value is twice the current number of cpu cores.")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> CLIENT_DEFAULT_OPERATION_TIMEOUT_MS = new PropertyDescriptor.Builder()
            .name("client_default_operation_timeout_ms")
            .description("Kudu normal operation timeout.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> CLIENT_DEFAULT_ADMIN_OPERATION_TIMEOUT_MS = new PropertyDescriptor.Builder()
            .name("client_default_admin_operation_timeout_ms")
            .description("Kudu admin operation timeout.")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

}
