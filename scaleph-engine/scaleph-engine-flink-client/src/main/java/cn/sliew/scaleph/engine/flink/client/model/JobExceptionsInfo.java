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

package cn.sliew.scaleph.engine.flink.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import java.util.List;

/**
 * {@code JobExceptionInfo} holds the information for single failure which caused a (maybe partial)
 * job restart.
 */
public class JobExceptionsInfo {

    public static final String FIELD_NAME_ROOT_EXCEPTION = "root-exception";
    public static final String FIELD_NAME_TIMESTAMP = "timestamp";
    public static final String FIELD_NAME_ALL_EXCEPTIONS = "all-exceptions";
    public static final String FIELD_NAME_TRUNCATED = "truncated";

    /**
     * @deprecated Use {@link JobExceptionsInfoWithHistory#getExceptionHistory()}'s entries instead.
     */
    @Deprecated
    @JsonProperty(FIELD_NAME_ROOT_EXCEPTION)
    private final String rootException;

    /**
     * @deprecated Use {@link JobExceptionsInfoWithHistory#getExceptionHistory()}'s entries instead.
     */
    @Deprecated
    @JsonProperty(FIELD_NAME_TIMESTAMP)
    private final Long rootTimestamp;

    /**
     * @deprecated Use {@link JobExceptionsInfoWithHistory#getExceptionHistory()}'s entries instead.
     */
    @Deprecated
    @JsonProperty(FIELD_NAME_ALL_EXCEPTIONS)
    private final List<ExecutionExceptionInfo> allExceptions;

    /**
     * @deprecated Use {@link JobExceptionsInfoWithHistory#getExceptionHistory()}'s entries instead.
     */
    @Deprecated
    @JsonProperty(FIELD_NAME_TRUNCATED)
    private final boolean truncated;

    @JsonCreator
    public JobExceptionsInfo(
            @JsonProperty(FIELD_NAME_ROOT_EXCEPTION) String rootException,
            @JsonProperty(FIELD_NAME_TIMESTAMP) Long rootTimestamp,
            @JsonProperty(FIELD_NAME_ALL_EXCEPTIONS) List<ExecutionExceptionInfo> allExceptions,
            @JsonProperty(FIELD_NAME_TRUNCATED) boolean truncated) {
        this.rootException = rootException;
        this.rootTimestamp = rootTimestamp;
        this.allExceptions = Preconditions.checkNotNull(allExceptions);
        this.truncated = truncated;
    }

    @JsonIgnore
    public String getRootException() {
        return rootException;
    }

    @JsonIgnore
    public Long getRootTimestamp() {
        return rootTimestamp;
    }

    @JsonIgnore
    public List<ExecutionExceptionInfo> getAllExceptions() {
        return allExceptions;
    }

    @JsonIgnore
    public boolean isTruncated() {
        return truncated;
    }

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
    public static final class ExecutionExceptionInfo {
        public static final String FIELD_NAME_EXCEPTION = "exception";
        public static final String FIELD_NAME_TASK = "task";
        public static final String FIELD_NAME_LOCATION = "location";
        public static final String FIELD_NAME_TIMESTAMP = "timestamp";
        public static final String FIELD_NAME_TASK_MANAGER_ID = "taskManagerId";

        @JsonProperty(FIELD_NAME_EXCEPTION)
        private final String exception;

        @JsonProperty(FIELD_NAME_TASK)
        private final String task;

        @JsonProperty(FIELD_NAME_LOCATION)
        private final String location;

        @JsonProperty(FIELD_NAME_TIMESTAMP)
        private final long timestamp;

        @JsonProperty(FIELD_NAME_TASK_MANAGER_ID)
        private final String taskManagerId;

        @JsonCreator
        public ExecutionExceptionInfo(
                @JsonProperty(FIELD_NAME_EXCEPTION) String exception,
                @JsonProperty(FIELD_NAME_TASK) String task,
                @JsonProperty(FIELD_NAME_LOCATION) String location,
                @JsonProperty(FIELD_NAME_TIMESTAMP) long timestamp,
                @JsonProperty(FIELD_NAME_TASK_MANAGER_ID) String taskManagerId) {
            this.exception = Preconditions.checkNotNull(exception);
            this.task = Preconditions.checkNotNull(task);
            this.location = Preconditions.checkNotNull(location);
            this.timestamp = timestamp;
            this.taskManagerId = taskManagerId;
        }
    }
}
