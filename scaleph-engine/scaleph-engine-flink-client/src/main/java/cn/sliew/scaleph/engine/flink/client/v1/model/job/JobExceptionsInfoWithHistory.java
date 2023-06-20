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

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * {@code JobExceptionsInfoWithHistory} extends {@link JobExceptionsInfo} providing a history of
 * previously caused failures. It's the response type of the {@link JobExceptionsHandler}.
 */
@Data
public class JobExceptionsInfoWithHistory extends JobExceptionsInfo {

    @JsonProperty("exceptionHistory")
    private JobExceptionHistory exceptionHistory;

    /**
     * {@code JobExceptionHistory} collects all previously caught errors.
     */
    @Data
    public static final class JobExceptionHistory {

        @JsonProperty("entries")
        private List<RootExceptionInfo> entries;

        @JsonProperty("truncated")
        private boolean truncated;
    }

    /**
     * Json equivalent of {@link
     * org.apache.flink.runtime.scheduler.exceptionhistory.ExceptionHistoryEntry}.
     */
    @Data
    public static class ExceptionInfo {

        @JsonProperty("exceptionName")
        private String exceptionName;

        @JsonProperty("stacktrace")
        private String stacktrace;

        @JsonProperty("timestamp")
        private long timestamp;

        @JsonProperty("taskName")
        @Nullable
        private String taskName;

        @JsonProperty("location")
        @Nullable
        private String location;

        @JsonProperty("taskManagerId")
        @Nullable
        private String taskManagerId;
    }

    /**
     * Json equivalent of {@link
     * org.apache.flink.runtime.scheduler.exceptionhistory.RootExceptionHistoryEntry}.
     */
    @Data
    public static class RootExceptionInfo extends ExceptionInfo {

        @JsonProperty("concurrentExceptions")
        private Collection<ExceptionInfo> concurrentExceptions;
    }
}
