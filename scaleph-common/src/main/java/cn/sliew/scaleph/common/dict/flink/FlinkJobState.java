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

import cn.sliew.scaleph.common.dict.DictDefinition;
import cn.sliew.scaleph.common.dict.DictInstance;
import cn.sliew.scaleph.common.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FlinkJobState implements DictInstance {

    SUBMITING("SUBMITING", "提交中"),
    SUBMITED("SUBMITED", "已提交"),
    SUBMIT_FAILED("SUBMIT_FAILED", "提交失败"),

    INITIALIZING("INITIALIZING", "初始化"),
    CREATED("CREATED", "已创建"),
    RUNNING("RUNNING", "运行中"),
    FAILING("FAILING", "失败中"),
    FAILED("FAILED", "已失败"),
    CANCELLING("CANCELLING", "取消中"),
    CANCELED("CANCELED", "已取消"),
    FINISHED("FINISHED", "已完成"),
    RESTARTING("RESTARTING", "重启中"),
    SUSPENDED("SUSPENDED", "已暂停"),
    RECONCILING("RECONCILING", "调节中"),
    ;

    @JsonCreator
    public static FlinkJobState of(String value) {
        return Arrays.stream(values())
                .filter(instance -> instance.getValue().equals(value))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(FlinkJobState.class, value));
    }

    @EnumValue
    private String value;
    private String label;

    FlinkJobState(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.FLINK_JOB_STATUS;
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
