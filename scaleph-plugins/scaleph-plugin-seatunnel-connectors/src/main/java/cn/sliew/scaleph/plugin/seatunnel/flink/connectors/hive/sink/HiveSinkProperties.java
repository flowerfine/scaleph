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

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.hive.sink;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum HiveSinkProperties {
    ;

    public static final PropertyDescriptor<Boolean> ABORT_DROP_PARTITION_METADATA = new PropertyDescriptor.Builder()
            .name("abort_drop_partition_metadata")
            .description("Target Hive table name eg: db1.table1")
            .type(PropertyType.BOOLEAN)
            .parser(Parsers.BOOLEAN_PARSER)
            .addValidator(Validators.BOOLEAN_VALIDATOR)
            .validateAndBuild();
}
