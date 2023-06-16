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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code JobExceptionsInfoWithHistory} extends {@link JobExceptionsInfo} providing a history of
 * previously caused failures. It's the response type of the {@link JobExceptionsHandler}.
 */
public class JobExceptionsInfoWithHistory extends JobExceptionsInfo {

    public static final String FIELD_NAME_EXCEPTION_HISTORY = "exceptionHistory";

    @JsonProperty(FIELD_NAME_EXCEPTION_HISTORY)
    private final JobExceptionHistory exceptionHistory;

    @JsonCreator
    public JobExceptionsInfoWithHistory(
            @JsonProperty(FIELD_NAME_ROOT_EXCEPTION) String rootException,
            @JsonProperty(FIELD_NAME_TIMESTAMP) Long rootTimestamp,
            @JsonProperty(FIELD_NAME_ALL_EXCEPTIONS) List<ExecutionExceptionInfo> allExceptions,
            @JsonProperty(FIELD_NAME_TRUNCATED) boolean truncated,
            @JsonProperty(FIELD_NAME_EXCEPTION_HISTORY) JobExceptionHistory exceptionHistory) {
        super(rootException, rootTimestamp, allExceptions, truncated);
        this.exceptionHistory = exceptionHistory;
    }

    public JobExceptionsInfoWithHistory(JobExceptionHistory exceptionHistory) {
        this(null, null, Collections.emptyList(), false, exceptionHistory);
    }

    @JsonIgnore
    public JobExceptionHistory getExceptionHistory() {
        return exceptionHistory;
    }

    /**
     * {@code JobExceptionHistory} collects all previously caught errors.
     */
    public static final class JobExceptionHistory {

        public static final String FIELD_NAME_ENTRIES = "entries";
        public static final String FIELD_NAME_TRUNCATED = "truncated";

        @JsonProperty(FIELD_NAME_ENTRIES)
        private final List<RootExceptionInfo> entries;

        @JsonProperty(FIELD_NAME_TRUNCATED)
        private final boolean truncated;

        @JsonCreator
        public JobExceptionHistory(
                @JsonProperty(FIELD_NAME_ENTRIES) List<RootExceptionInfo> entries,
                @JsonProperty(FIELD_NAME_TRUNCATED) boolean truncated) {
            this.entries = entries;
            this.truncated = truncated;
        }

        @JsonIgnore
        public List<RootExceptionInfo> getEntries() {
            return entries;
        }

        @JsonIgnore
        public boolean isTruncated() {
            return truncated;
        }
    }

    /**
     * Json equivalent of {@link
     * org.apache.flink.runtime.scheduler.exceptionhistory.ExceptionHistoryEntry}.
     */
    public static class ExceptionInfo {

        public static final String FIELD_NAME_EXCEPTION_NAME = "exceptionName";
        public static final String FIELD_NAME_EXCEPTION_STACKTRACE = "stacktrace";
        public static final String FIELD_NAME_EXCEPTION_TIMESTAMP = "timestamp";
        public static final String FIELD_NAME_TASK_NAME = "taskName";
        public static final String FIELD_NAME_LOCATION = "location";
        public static final String FIELD_NAME_TASK_MANAGER_ID = "taskManagerId";

        @JsonProperty(FIELD_NAME_EXCEPTION_NAME)
        private final String exceptionName;

        @JsonProperty(FIELD_NAME_EXCEPTION_STACKTRACE)
        private final String stacktrace;

        @JsonProperty(FIELD_NAME_EXCEPTION_TIMESTAMP)
        private final long timestamp;

        @JsonInclude(NON_NULL)
        @JsonProperty(FIELD_NAME_TASK_NAME)
        @Nullable
        private final String taskName;

        @JsonInclude(NON_NULL)
        @JsonProperty(FIELD_NAME_LOCATION)
        @Nullable
        private final String location;

        @JsonInclude(NON_NULL)
        @JsonProperty(FIELD_NAME_TASK_MANAGER_ID)
        @Nullable
        private final String taskManagerId;

        public ExceptionInfo(String exceptionName, String stacktrace, long timestamp) {
            this(exceptionName, stacktrace, timestamp, null, null, null);
        }

        @JsonCreator
        public ExceptionInfo(
                @JsonProperty(FIELD_NAME_EXCEPTION_NAME) String exceptionName,
                @JsonProperty(FIELD_NAME_EXCEPTION_STACKTRACE) String stacktrace,
                @JsonProperty(FIELD_NAME_EXCEPTION_TIMESTAMP) long timestamp,
                @JsonProperty(FIELD_NAME_TASK_NAME) @Nullable String taskName,
                @JsonProperty(FIELD_NAME_LOCATION) @Nullable String location,
                @JsonProperty(FIELD_NAME_TASK_MANAGER_ID) @Nullable String taskManagerId) {
            this.exceptionName = checkNotNull(exceptionName);
            this.stacktrace = checkNotNull(stacktrace);
            this.timestamp = timestamp;
            this.taskName = taskName;
            this.location = location;
            this.taskManagerId = taskManagerId;
        }

        @JsonIgnore
        public String getExceptionName() {
            return exceptionName;
        }

        @JsonIgnore
        public String getStacktrace() {
            return stacktrace;
        }

        @JsonIgnore
        public long getTimestamp() {
            return timestamp;
        }

        @JsonIgnore
        @Nullable
        public String getTaskName() {
            return taskName;
        }

        @JsonIgnore
        @Nullable
        public String getLocation() {
            return location;
        }

        @JsonIgnore
        @Nullable
        public String getTaskManagerId() {
            return taskManagerId;
        }
    }

    /**
     * Json equivalent of {@link
     * org.apache.flink.runtime.scheduler.exceptionhistory.RootExceptionHistoryEntry}.
     */
    public static class RootExceptionInfo extends ExceptionInfo {

        public static final String FIELD_NAME_CONCURRENT_EXCEPTIONS = "concurrentExceptions";

        @JsonProperty(FIELD_NAME_CONCURRENT_EXCEPTIONS)
        private final Collection<ExceptionInfo> concurrentExceptions;

        public RootExceptionInfo(
                String exceptionName,
                String stacktrace,
                long timestamp,
                Collection<ExceptionInfo> concurrentExceptions) {
            this(exceptionName, stacktrace, timestamp, null, null, null, concurrentExceptions);
        }

        @JsonCreator
        public RootExceptionInfo(
                @JsonProperty(FIELD_NAME_EXCEPTION_NAME) String exceptionName,
                @JsonProperty(FIELD_NAME_EXCEPTION_STACKTRACE) String stacktrace,
                @JsonProperty(FIELD_NAME_EXCEPTION_TIMESTAMP) long timestamp,
                @JsonProperty(FIELD_NAME_TASK_NAME) @Nullable String taskName,
                @JsonProperty(FIELD_NAME_LOCATION) @Nullable String location,
                @JsonProperty(FIELD_NAME_TASK_MANAGER_ID) @Nullable String taskManagerId,
                @JsonProperty(FIELD_NAME_CONCURRENT_EXCEPTIONS)
                Collection<ExceptionInfo> concurrentExceptions) {
            super(exceptionName, stacktrace, timestamp, taskName, location, taskManagerId);
            this.concurrentExceptions = concurrentExceptions;
        }

        @JsonIgnore
        public Collection<ExceptionInfo> getConcurrentExceptions() {
            return concurrentExceptions;
        }
    }
}
