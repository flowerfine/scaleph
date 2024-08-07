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

package cn.sliew.scaleph.common.dict.flink.kubernetes;

import cn.sliew.carp.framework.common.dict.DictInstance;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SavepointFormatType implements DictInstance {

    CANONICAL("CANONICAL", "CANONICAL", "A canonical, common for all state backends format."),
    NATIVE("NATIVE", "NATIVE", "A format specific for the chosen state backend."),
    UNKNOWN("UNKNOWN", "UNKNOWN", "unknown"),
    ;

    @JsonCreator
    public static SavepointFormatType of(String value) {
        return Arrays.stream(values())
                .filter(instance -> instance.getValue().equals(value))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(SavepointFormatType.class, value));
    }

    @EnumValue
    private String value;
    private String label;
    private String remark;

    SavepointFormatType(String value, String label, String remark) {
        this.value = value;
        this.label = label;
        this.remark = remark;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getRemark() {
        return remark;
    }


}
