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

package cn.sliew.scaleph.engine.flink.client.v1.model.job;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * {@code JobExceptionInfo} holds the information for single failure which caused a (maybe partial)
 * job restart.
 */
@Data
public class JobExceptionsInfo {

    /**
     * @deprecated Use {@link JobExceptionsInfoWithHistory#getExceptionHistory()}'s entries instead.
     */
    @Deprecated
    @JsonProperty("root-exception")
    private String rootException;

    /**
     * @deprecated Use {@link JobExceptionsInfoWithHistory#getExceptionHistory()}'s entries instead.
     */
    @Deprecated
    @JsonProperty("timestamp")
    private Long rootTimestamp;

    /**
     * @deprecated Use {@link JobExceptionsInfoWithHistory#getExceptionHistory()}'s entries instead.
     */
    @Deprecated
    @JsonProperty("all-exceptions")
    private List<ExecutionExceptionInfo> allExceptions;

    /**
     * @deprecated Use {@link JobExceptionsInfoWithHistory#getExceptionHistory()}'s entries instead.
     */
    @Deprecated
    @JsonProperty("truncated")
    private boolean truncated;


    // ---------------------------------------------------------------------------------
    // Static helper classes
    // ---------------------------------------------------------------------------------

    /**
     * Nested class to encapsulate the task execution exception.
     *
     * @deprecated {@code ExecutionExceptionInfo} will be replaced by {@link
     * JobExceptionsInfoWithHistory.ExceptionInfo} as part of the effort of deprecating {@link
     * JobExceptionsInfo#allExceptions}.
     */
    @Deprecated
    @Data
    public static final class ExecutionExceptionInfo {

        @JsonProperty("exception")
        private String exception;

        @JsonProperty("task")
        private String task;

        @JsonProperty("location")
        private String location;

        @JsonProperty("timestamp")
        private long timestamp;

        @JsonProperty("taskManagerId")
        private String taskManagerId;
    }
}
