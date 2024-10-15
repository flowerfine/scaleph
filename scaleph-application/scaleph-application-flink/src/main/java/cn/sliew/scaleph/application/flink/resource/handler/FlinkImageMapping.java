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

package cn.sliew.scaleph.application.flink.resource.handler;

import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.common.dict.flink.kubernetes.OperatorFlinkVersion;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum FlinkImageMapping {

    JAR_1_15(FlinkJobType.JAR, OperatorFlinkVersion.v1_15, FlinkVersionMapping.V_1_15, "flink:1.15"),
    JAR_1_16(FlinkJobType.JAR, OperatorFlinkVersion.v1_16, FlinkVersionMapping.V_1_16, "flink:1.16"),
    JAR_1_17(FlinkJobType.JAR, OperatorFlinkVersion.v1_17, FlinkVersionMapping.V_1_17, "flink:1.17"),
    JAR_1_18(FlinkJobType.JAR, OperatorFlinkVersion.v1_18, FlinkVersionMapping.V_1_18, "flink:1.18"),
    JAR_1_19(FlinkJobType.JAR, OperatorFlinkVersion.v1_19, FlinkVersionMapping.V_1_19, "flink:1.19"),

    SQL_1_17(FlinkJobType.SQL, OperatorFlinkVersion.v1_17, FlinkVersionMapping.V_1_17, "ghcr.io/flowerfine/scaleph-sql-template:1.17"),
    SQL_1_18(FlinkJobType.SQL, OperatorFlinkVersion.v1_18, FlinkVersionMapping.V_1_18, "ghcr.io/flowerfine/scaleph-sql-template:1.18"),

    SEATUNNEL_1_16(FlinkJobType.SEATUNNEL, OperatorFlinkVersion.v1_16, FlinkVersionMapping.V_1_16, "ghcr.io/flowerfine/scaleph-seatunnel:2.3.8-flink-1.16"),
    FLINK_CDC_1_18(FlinkJobType.FLINK_CDC, OperatorFlinkVersion.v1_18, FlinkVersionMapping.V_1_18, "ghcr.io/flowerfine/scaleph-flink-cdc:3.0.0-flink-1.18"),
    ;

    private FlinkJobType jobType;
    private OperatorFlinkVersion majorVersion;
    private FlinkVersionMapping fullVersion;
    private String image;

    FlinkImageMapping(FlinkJobType jobType, OperatorFlinkVersion majorVersion, FlinkVersionMapping fullVersion, String image) {
        this.jobType = jobType;
        this.majorVersion = majorVersion;
        this.fullVersion = fullVersion;
        this.image = image;
    }

    public static FlinkImageMapping of(FlinkJobType jobType, OperatorFlinkVersion majorVersion) {
        for (FlinkImageMapping mapping : values()) {
            if (mapping.getJobType() == jobType && mapping.getMajorVersion() == majorVersion) {
                return mapping;
            }
        }
        throw new IllegalStateException("unknown flink image mapping for job: " + jobType.getValue() + ", flink version: " + majorVersion.getValue());
    }

    public static List<FlinkImageMapping> of(FlinkVersion flinkVersion) {
        List<FlinkImageMapping> mappings = new ArrayList<>();
        for (FlinkImageMapping mapping : values()) {
            FlinkVersionMapping flinkVersionMapping = mapping.getFullVersion();
            for (FlinkVersion version : flinkVersionMapping.getSupportVersions()) {
                if (version == flinkVersion) {
                    mappings.add(mapping);
                }
            }
        }
        return mappings;
    }

}
