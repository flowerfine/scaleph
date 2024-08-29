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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.cdc.oracle.source;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.PropertyType;
import cn.sliew.scaleph.plugin.framework.property.Validators;

import java.util.List;

public enum OracleCDCSourceProperties {
    ;

    public static final PropertyDescriptor<List<String>> SCHEMA = new PropertyDescriptor.Builder()
            .name("schema-names")
            .description("Schema name of the database to monitor.")
            .type(PropertyType.OBJECT)
            .parser(Parsers.STRING_ARRAY_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> USE_SELECT_COUNT = new PropertyDescriptor.Builder()
            .name("use_select_count")
            .description("Use select count for table count rather then other methods in full stage.In this scenario, select count directly is used when it is faster to update statistics using sql from analysis table")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Boolean> SKIP_ANALYZE = new PropertyDescriptor.Builder()
            .name("skip_analyze")
            .description("Skip the analysis of table count in full stage.In this scenario, you schedule analysis table sql to update related table statistics periodically or your table data does not change frequently")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();
}
