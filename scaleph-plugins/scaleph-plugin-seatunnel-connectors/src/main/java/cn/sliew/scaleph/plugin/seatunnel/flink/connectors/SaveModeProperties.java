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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.PropertyType;
import cn.sliew.scaleph.plugin.framework.property.Validators;

public enum SaveModeProperties {
    ;

    public static final PropertyDescriptor<String> SCHEMA_SAVE_MODE = new PropertyDescriptor.Builder()
            .name("schema_save_mode")
            .description("the schema save mode")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .defaultValue("CREATE_SCHEMA_WHEN_NOT_EXIST")
            .allowableValues("CREATE_SCHEMA_WHEN_NOT_EXIST", "RECREATE_SCHEMA", "ERROR_WHEN_SCHEMA_NOT_EXIST")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DATA_SAVE_MODE = new PropertyDescriptor.Builder()
            .name("data_save_mode")
            .description("the data save mode")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .defaultValue("APPEND_DATA")
            .allowableValues("APPEND_DATA", "DROP_DATA", "CUSTOM_PROCESSING", "ERROR_WHEN_DATA_EXISTS")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

}
