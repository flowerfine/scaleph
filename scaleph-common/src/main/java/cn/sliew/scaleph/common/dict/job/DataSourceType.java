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

package cn.sliew.scaleph.common.dict.job;

import cn.sliew.scaleph.common.dict.DictDefinition;
import cn.sliew.scaleph.common.dict.DictInstance;
import cn.sliew.scaleph.common.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DataSourceType implements DictInstance {

    JDBC("JDBC", "JDBC"),
    MYSQL("Mysql", "Mysql"),
    ORACLE("Oracle", "Oracle"),
    POSTGRESQL("PostGreSQL", "PostGreSQL"),
    KAFKA("Kafka", "Kafka"),
    DORIS("Doris", "Doris"),
    CLICKHOUSE("ClickHouse", "ClickHouse"),
    ELASTICSEARCH("Elasticsearch", "Elasticsearch"),
    DRUID("Druid", "Druid"),
    ;

    @JsonCreator
    public static DataSourceType of(String value) {
        return Arrays.stream(values())
                .filter(instance -> instance.getValue().equals(value))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(DataSourceType.class, value));
    }

    @EnumValue
    private String value;
    private String label;

    DataSourceType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.DATASOURCE_TYPE;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
