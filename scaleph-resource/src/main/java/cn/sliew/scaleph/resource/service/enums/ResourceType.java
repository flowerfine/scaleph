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

package cn.sliew.scaleph.resource.service.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ResourceType {

    SEATUNNEL_RELEASE("seatunnel_release", "SeaTunnel Release"),
    FLINK_RELEASE("flink_release", "Flink Release"),
    CLUSTER_CREDENTIAL("cluster_credential", "Cluster Credential"),

    // todo
    JAR("jar", "Additional Dependency Jar"),
    SCHEMA("schema", "Schema"),
    DATASOURCE("datasource", "DataSource"),

    ;
    @JsonValue
    @EnumValue
    private String code;
    private String value;

    ResourceType(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
