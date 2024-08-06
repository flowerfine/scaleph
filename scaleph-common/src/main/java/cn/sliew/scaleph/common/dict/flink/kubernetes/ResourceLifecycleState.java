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
public enum ResourceLifecycleState implements DictInstance {

    CREATED("CREATED", "CREATED", "The resource was created in Kubernetes but not yet handled by the operator"),
    SUSPENDED("SUSPENDED", "SUSPENDED", "The resource (job) has been suspended"),
    UPGRADING("UPGRADING", "UPGRADING", "The resource is being upgraded"),
    DEPLOYED("DEPLOYED", "DEPLOYED", "The resource is deployed/submitted to Kubernetes, but it’s not yet considered to be stable and might be rolled back in the future"),
    STABLE("STABLE", "STABLE", "The resource deployment is considered to be stable and won’t be rolled back"),
    ROLLING_BACK("ROLLING_BACK", "ROLLING_BACK", "The resource is being rolled back to the last stable spec"),
    ROLLED_BACK("ROLLED_BACK", "ROLLED_BACK", "The resource is deployed with the last stable spec"),
    FAILED("FAILED", "FAILED", "The job terminally failed"),
    ;

    @JsonCreator
    public static ResourceLifecycleState of(String value) {
        return Arrays.stream(values())
                .filter(instance -> instance.getValue().equals(value))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(ResourceLifecycleState.class, value));
    }

    @EnumValue
    private String value;
    private String label;
    private String remark;

    ResourceLifecycleState(String value, String label, String remark) {
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
