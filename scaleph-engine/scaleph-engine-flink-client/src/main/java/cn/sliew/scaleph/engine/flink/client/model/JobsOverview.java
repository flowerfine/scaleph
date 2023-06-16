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
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * An overview of how many jobs are in which status.
 */
@Getter
public class JobsOverview {

    public static final String FIELD_NAME_JOBS_RUNNING = "jobs-running";
    public static final String FIELD_NAME_JOBS_FINISHED = "jobs-finished";
    public static final String FIELD_NAME_JOBS_CANCELLED = "jobs-cancelled";
    public static final String FIELD_NAME_JOBS_FAILED = "jobs-failed";

    @JsonProperty(FIELD_NAME_JOBS_RUNNING)
    private final int numJobsRunningOrPending;

    @JsonProperty(FIELD_NAME_JOBS_FINISHED)
    private final int numJobsFinished;

    @JsonProperty(FIELD_NAME_JOBS_CANCELLED)
    private final int numJobsCancelled;

    @JsonProperty(FIELD_NAME_JOBS_FAILED)
    private final int numJobsFailed;

    @JsonCreator
    public JobsOverview(
            @JsonProperty(FIELD_NAME_JOBS_RUNNING) int numJobsRunningOrPending,
            @JsonProperty(FIELD_NAME_JOBS_FINISHED) int numJobsFinished,
            @JsonProperty(FIELD_NAME_JOBS_CANCELLED) int numJobsCancelled,
            @JsonProperty(FIELD_NAME_JOBS_FAILED) int numJobsFailed) {

        this.numJobsRunningOrPending = numJobsRunningOrPending;
        this.numJobsFinished = numJobsFinished;
        this.numJobsCancelled = numJobsCancelled;
        this.numJobsFailed = numJobsFailed;
    }
}
