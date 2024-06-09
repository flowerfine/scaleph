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

import cn.sliew.scaleph.application.flink.service.dto.FlinkVersionOption;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.common.dict.flink.kubernetes.OperatorFlinkVersion;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum FlinkVersionMapping {

    V_1_19(OperatorFlinkVersion.v1_19, FlinkVersion.V_1_19_0, FlinkVersion.V_1_19_0),
    V_1_18(OperatorFlinkVersion.v1_18, FlinkVersion.V_1_18_0, FlinkVersion.V_1_18_1, FlinkVersion.V_1_18_0),
    V_1_17(OperatorFlinkVersion.v1_17, FlinkVersion.V_1_17_2, FlinkVersion.V_1_17_2, FlinkVersion.V_1_17_1, FlinkVersion.V_1_17_0),
    V_1_16(OperatorFlinkVersion.v1_16, FlinkVersion.V_1_16_3, FlinkVersion.V_1_16_3, FlinkVersion.V_1_16_2, FlinkVersion.V_1_16_1, FlinkVersion.V_1_16_0),
    V_1_15(OperatorFlinkVersion.v1_15, FlinkVersion.V_1_15_4, FlinkVersion.V_1_15_4, FlinkVersion.V_1_15_3, FlinkVersion.V_1_15_2, FlinkVersion.V_1_15_1, FlinkVersion.V_1_15_0),
    ;

    private OperatorFlinkVersion majorVersion;
    private FlinkVersion defaultVersion;
    private FlinkVersion[] supportVersions;

    FlinkVersionMapping(OperatorFlinkVersion majorVersion, FlinkVersion defaultVersion, FlinkVersion... supportVersions) {
        this.majorVersion = majorVersion;
        this.defaultVersion = defaultVersion;
        this.supportVersions = supportVersions;
    }

    public static FlinkVersionMapping of(OperatorFlinkVersion majorVersion) {
        for (FlinkVersionMapping mapping : values()) {
            if (mapping.getMajorVersion() == majorVersion) {
                return mapping;
            }
        }
        throw new IllegalStateException("unknown flink version mapping for: " + majorVersion.getValue());
    }

    public static FlinkVersionMapping of(FlinkVersion flinkVersion) {
        for (FlinkVersionMapping mapping : values()) {
            for (FlinkVersion version : mapping.getSupportVersions()) {
                if (version == flinkVersion) {
                    return mapping;
                }
            }
        }
        throw new IllegalStateException("unknown flink version mapping for: " + flinkVersion.getValue());
    }

    public FlinkVersionOption toOption() {
        FlinkVersionOption option = new FlinkVersionOption();
        option.setLabel(getMajorVersion().getValue());
        option.setOptions(Arrays.asList(getSupportVersions()));
        return option;
    }
}
