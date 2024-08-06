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

package cn.sliew.scaleph.common.dict.flink;

import cn.sliew.carp.framework.common.dict.DictInstance;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FlinkVersion implements DictInstance {

    V_1_15_0("1.15.0", "1.15.0"),
    V_1_15_1("1.15.1", "1.15.1"),
    V_1_15_2("1.15.2", "1.15.2"),
    V_1_15_3("1.15.3", "1.15.3"),
    V_1_15_4("1.15.4", "1.15.4"),

    V_1_16_0("1.16.0", "1.16.0"),
    V_1_16_1("1.16.1", "1.16.1"),
    V_1_16_2("1.16.2", "1.16.2"),
    V_1_16_3("1.16.3", "1.16.3"),

    V_1_17_0("1.17.0", "1.17.0"),
    V_1_17_1("1.17.1", "1.17.1"),
    V_1_17_2("1.17.2", "1.17.2"),

    V_1_18_0("1.18.0", "1.18.0"),
    V_1_18_1("1.18.1", "1.18.1"),

    V_1_19_0("1.19.0", "1.19.0"),
    ;

    @JsonCreator
    public static FlinkVersion of(String value) {
        return Arrays.stream(values())
                .filter(instance -> instance.getValue().equals(value))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(FlinkVersion.class, value));
    }

    public static FlinkVersion current() {
        return values()[values().length - 1];
    }

    @EnumValue
    private String value;
    private String label;

    FlinkVersion(String value, String label) {
        this.value = value;
        this.label = label;
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
