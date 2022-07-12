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

package cn.sliew.scaleph.engine.flink.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.apache.flink.api.common.JobStatus;

/**
 * @see JobStatus
 */
@Getter
public enum FlinkJobStatus {

    SUBMITED(0, "SUBMITED"),
    SUBMIT_FAILED(1, "SUBMIT FAILED"),

    INITIALIZING(2, "INITIALIZING"),
    CREATED(3, "CREATED"),
    RUNNING(4, "RUNNING"),
    FAILING(5, "FAILING"),
    FAILED(6, "FAILED"),
    CANCELLING(7, "CANCELLING"),
    CANCELED(8, "CANCELED"),
    FINISHED(9, "FINISHED"),
    RESTARTING(10, "RESTARTING"),
    SUSPENDED(11, "SUSPENDED"),
    RECONCILING(12, "RECONCILING"),
    ;

    @EnumValue
    @JsonValue
    private int code;
    private String desc;

    FlinkJobStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
