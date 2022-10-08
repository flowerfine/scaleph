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

package cn.sliew.scaleph.engine.flink.service.enums;

import cn.sliew.scaleph.common.dict.flink.FlinkArtifactType;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Arrays;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FlinkVersionMapping {

    v1_13_0(FlinkVersion.V_1_13_0, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.v1_13, org.apache.flink.FlinkVersion.v1_13),
    v1_13_1(FlinkVersion.V_1_13_1, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.v1_13, org.apache.flink.FlinkVersion.v1_13),
    v1_13_2(FlinkVersion.V_1_13_2, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.v1_13, org.apache.flink.FlinkVersion.v1_13),
    v1_13_3(FlinkVersion.V_1_13_3, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.v1_13, org.apache.flink.FlinkVersion.v1_13),
    v1_13_5(FlinkVersion.V_1_13_5, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.v1_13, org.apache.flink.FlinkVersion.v1_13),
    v1_13_6(FlinkVersion.V_1_13_6, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.v1_13, org.apache.flink.FlinkVersion.v1_13),

    v1_14_0(FlinkVersion.V_1_14_0, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.v1_14, org.apache.flink.FlinkVersion.v1_14),
    v1_14_2(FlinkVersion.V_1_14_2, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.v1_14, org.apache.flink.FlinkVersion.v1_14),
    v1_14_3(FlinkVersion.V_1_14_3, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.v1_14, org.apache.flink.FlinkVersion.v1_14),
    v1_14_4(FlinkVersion.V_1_14_4, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.v1_14, org.apache.flink.FlinkVersion.v1_14),
    v1_14_5(FlinkVersion.V_1_14_5, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.v1_14, org.apache.flink.FlinkVersion.v1_14),
    v1_14_6(FlinkVersion.V_1_14_6, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.v1_14, org.apache.flink.FlinkVersion.v1_14),

    v1_15_0(FlinkVersion.V_1_15_0, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.v1_15, org.apache.flink.FlinkVersion.v1_15),
    v1_15_1(FlinkVersion.V_1_15_1, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.v1_15, org.apache.flink.FlinkVersion.v1_15),
    v1_15_2(FlinkVersion.V_1_15_2, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.v1_15, org.apache.flink.FlinkVersion.v1_15),
    ;

    @JsonCreator
    public static FlinkVersionMapping of(String value) {
        return Arrays.stream(values())
                .filter(instance -> instance.getScalephVersion().getValue().equals(value))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(FlinkVersionMapping.class, value));
    }

    private FlinkVersion scalephVersion;
    private org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion operatorVersion;
    private org.apache.flink.FlinkVersion flinkVersion;

    FlinkVersionMapping(FlinkVersion scalephVersion, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion operatorVersion, org.apache.flink.FlinkVersion flinkVersion) {
        this.scalephVersion = scalephVersion;
        this.operatorVersion = operatorVersion;
        this.flinkVersion = flinkVersion;
    }
}
