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

import cn.sliew.scaleph.common.dict.DictInstance;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FlinkVersion implements DictInstance {

    V_1_13_0("1.13.0", "1.13.0"),
    V_1_13_1("1.13.1", "1.13.1"),
    V_1_13_2("1.13.2", "1.13.2"),
    V_1_13_3("1.13.3", "1.13.3"),
    V_1_13_5("1.13.5", "1.13.5"),
    V_1_13_6("1.13.6", "1.13.6"),

    V_1_14_0("1.14.0", "1.14.0"),
    V_1_14_2("1.14.2", "1.14.2"),
    V_1_14_3("1.14.3", "1.14.3"),
    V_1_14_4("1.14.4", "1.14.4"),
    V_1_14_5("1.14.5", "1.14.5"),
    V_1_14_6("1.14.6", "1.14.6"),

    V_1_15_0("1.15.0", "1.15.0"),
    V_1_15_1("1.15.1", "1.15.1"),
    V_1_15_2("1.15.2", "1.15.2"),
    ;

    @JsonCreator
    public static FlinkVersion of(String value) {
        return Arrays.stream(values())
                .filter(instance -> instance.getValue().equals(value))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(FlinkVersion.class, value));
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
