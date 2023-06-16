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

import cn.sliew.scaleph.common.dict.job.JobStatus;
import cn.sliew.scaleph.engine.flink.client.json.JobIDDeserializer;
import cn.sliew.scaleph.engine.flink.client.json.JobIDSerializer;
import cn.sliew.scaleph.engine.flink.client.json.JobVertexIDDeserializer;
import cn.sliew.scaleph.engine.flink.client.json.JobVertexIDSerializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;

/**
 * Details about a job.
 */
@Getter
public class JobDetailsInfo {

    public static final String FIELD_NAME_JOB_ID = "jid";

    public static final String FIELD_NAME_JOB_NAME = "name";

    public static final String FIELD_NAME_IS_STOPPABLE = "isStoppable";

    public static final String FIELD_NAME_JOB_STATUS = "state";

    public static final String FIELD_NAME_START_TIME = "start-time";

    public static final String FIELD_NAME_END_TIME = "end-time";

    public static final String FIELD_NAME_DURATION = "duration";

    public static final String FIELD_NAME_MAX_PARALLELISM = "maxParallelism";

    // TODO: For what do we need this???
    public static final String FIELD_NAME_NOW = "now";

    public static final String FIELD_NAME_TIMESTAMPS = "timestamps";

    public static final String FIELD_NAME_JOB_VERTEX_INFOS = "vertices";

    public static final String FIELD_NAME_JOB_VERTICES_PER_STATE = "status-counts";

    public static final String FIELD_NAME_JSON_PLAN = "plan";

    @JsonProperty(FIELD_NAME_JOB_ID)
    @JsonSerialize(using = JobIDSerializer.class)
    private final JobID jobId;

    @JsonProperty(FIELD_NAME_JOB_NAME)
    private final String name;

    @JsonProperty(FIELD_NAME_IS_STOPPABLE)
    private final boolean isStoppable;

    @JsonProperty(FIELD_NAME_JOB_STATUS)
    private final JobStatus jobStatus;

    @JsonProperty(FIELD_NAME_START_TIME)
    private final long startTime;

    @JsonProperty(FIELD_NAME_END_TIME)
    private final long endTime;

    @JsonProperty(FIELD_NAME_DURATION)
    private final long duration;

    @JsonProperty(FIELD_NAME_MAX_PARALLELISM)
    private final long maxParallelism;

    @JsonProperty(FIELD_NAME_NOW)
    private final long now;

    @JsonProperty(FIELD_NAME_TIMESTAMPS)
    private final Map<JobStatus, Long> timestamps;

    @JsonProperty(FIELD_NAME_JOB_VERTEX_INFOS)
    private final Collection<JobVertexDetailsInfo> jobVertexInfos;

    @JsonProperty(FIELD_NAME_JOB_VERTICES_PER_STATE)
    private final Map<ExecutionState, Integer> jobVerticesPerState;

    @JsonProperty(FIELD_NAME_JSON_PLAN)
    private final JobPlanInfo.RawJson jsonPlan;

    @JsonCreator
    public JobDetailsInfo(
            @JsonDeserialize(using = JobIDDeserializer.class) @JsonProperty(FIELD_NAME_JOB_ID)
            JobID jobId,
            @JsonProperty(FIELD_NAME_JOB_NAME) String name,
            @JsonProperty(FIELD_NAME_IS_STOPPABLE) boolean isStoppable,
            @JsonProperty(FIELD_NAME_JOB_STATUS) JobStatus jobStatus,
            @JsonProperty(FIELD_NAME_START_TIME) long startTime,
            @JsonProperty(FIELD_NAME_END_TIME) long endTime,
            @JsonProperty(FIELD_NAME_DURATION) long duration,
            @JsonProperty(FIELD_NAME_MAX_PARALLELISM) long maxParallelism,
            @JsonProperty(FIELD_NAME_NOW) long now,
            @JsonProperty(FIELD_NAME_TIMESTAMPS) Map<JobStatus, Long> timestamps,
            @JsonProperty(FIELD_NAME_JOB_VERTEX_INFOS)
            Collection<JobVertexDetailsInfo> jobVertexInfos,
            @JsonProperty(FIELD_NAME_JOB_VERTICES_PER_STATE)
            Map<ExecutionState, Integer> jobVerticesPerState,
            @JsonProperty(FIELD_NAME_JSON_PLAN) JobPlanInfo.RawJson jsonPlan) {
        this.jobId = Preconditions.checkNotNull(jobId);
        this.name = Preconditions.checkNotNull(name);
        this.isStoppable = isStoppable;
        this.jobStatus = Preconditions.checkNotNull(jobStatus);
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.maxParallelism = maxParallelism;
        this.now = now;
        this.timestamps = Preconditions.checkNotNull(timestamps);
        this.jobVertexInfos = Preconditions.checkNotNull(jobVertexInfos);
        this.jobVerticesPerState = Preconditions.checkNotNull(jobVerticesPerState);
        this.jsonPlan = Preconditions.checkNotNull(jsonPlan);
    }

    @JsonIgnore
    public JobID getJobId() {
        return jobId;
    }

    @JsonIgnore
    public String getName() {
        return name;
    }

    @JsonIgnore
    public boolean isStoppable() {
        return isStoppable;
    }

    @JsonIgnore
    public JobStatus getJobStatus() {
        return jobStatus;
    }

    @JsonIgnore
    public long getStartTime() {
        return startTime;
    }

    @JsonIgnore
    public long getEndTime() {
        return endTime;
    }

    @JsonIgnore
    public long getMaxParallelism() {
        return maxParallelism;
    }

    @JsonIgnore
    public long getDuration() {
        return duration;
    }

    @JsonIgnore
    public long getNow() {
        return now;
    }

    @JsonIgnore
    public Map<JobStatus, Long> getTimestamps() {
        return timestamps;
    }

    @JsonIgnore
    public Collection<JobVertexDetailsInfo> getJobVertexInfos() {
        return jobVertexInfos;
    }

    @JsonIgnore
    public Map<ExecutionState, Integer> getJobVerticesPerState() {
        return jobVerticesPerState;
    }

    @JsonIgnore
    public String getJsonPlan() {
        return jsonPlan.toString();
    }

    // ---------------------------------------------------
    // Static inner classes
    // ---------------------------------------------------

    /**
     * Detailed information about a job vertex.
     */
    @Schema(name = "JobDetailsVertexInfo")
    public static final class JobVertexDetailsInfo {

        public static final String FIELD_NAME_JOB_VERTEX_ID = "id";

        public static final String FIELD_NAME_JOB_VERTEX_NAME = "name";

        public static final String FIELD_NAME_MAX_PARALLELISM = "maxParallelism";

        public static final String FIELD_NAME_PARALLELISM = "parallelism";

        public static final String FIELD_NAME_JOB_VERTEX_STATE = "status";

        public static final String FIELD_NAME_JOB_VERTEX_START_TIME = "start-time";

        public static final String FIELD_NAME_JOB_VERTEX_END_TIME = "end-time";

        public static final String FIELD_NAME_JOB_VERTEX_DURATION = "duration";

        public static final String FIELD_NAME_TASKS_PER_STATE = "tasks";

        public static final String FIELD_NAME_JOB_VERTEX_METRICS = "metrics";

        @JsonProperty(FIELD_NAME_JOB_VERTEX_ID)
        @JsonSerialize(using = JobVertexIDSerializer.class)
        private final JobVertexID jobVertexID;

        @JsonProperty(FIELD_NAME_JOB_VERTEX_NAME)
        private final String name;

        @JsonProperty(FIELD_NAME_MAX_PARALLELISM)
        private final int maxParallelism;

        @JsonProperty(FIELD_NAME_PARALLELISM)
        private final int parallelism;

        @JsonProperty(FIELD_NAME_JOB_VERTEX_STATE)
        private final ExecutionState executionState;

        @JsonProperty(FIELD_NAME_JOB_VERTEX_START_TIME)
        private final long startTime;

        @JsonProperty(FIELD_NAME_JOB_VERTEX_END_TIME)
        private final long endTime;

        @JsonProperty(FIELD_NAME_JOB_VERTEX_DURATION)
        private final long duration;

        @JsonProperty(FIELD_NAME_TASKS_PER_STATE)
        private final Map<ExecutionState, Integer> tasksPerState;

        @JsonProperty(FIELD_NAME_JOB_VERTEX_METRICS)
        private final IOMetricsInfo jobVertexMetrics;

        @JsonCreator
        public JobVertexDetailsInfo(
                @JsonDeserialize(using = JobVertexIDDeserializer.class)
                @JsonProperty(FIELD_NAME_JOB_VERTEX_ID)
                JobVertexID jobVertexID,
                @JsonProperty(FIELD_NAME_JOB_VERTEX_NAME) String name,
                @JsonProperty(FIELD_NAME_MAX_PARALLELISM) int maxParallelism,
                @JsonProperty(FIELD_NAME_PARALLELISM) int parallelism,
                @JsonProperty(FIELD_NAME_JOB_VERTEX_STATE) ExecutionState executionState,
                @JsonProperty(FIELD_NAME_JOB_VERTEX_START_TIME) long startTime,
                @JsonProperty(FIELD_NAME_JOB_VERTEX_END_TIME) long endTime,
                @JsonProperty(FIELD_NAME_JOB_VERTEX_DURATION) long duration,
                @JsonProperty(FIELD_NAME_TASKS_PER_STATE)
                Map<ExecutionState, Integer> tasksPerState,
                @JsonProperty(FIELD_NAME_JOB_VERTEX_METRICS) IOMetricsInfo jobVertexMetrics) {
            this.jobVertexID = Preconditions.checkNotNull(jobVertexID);
            this.name = Preconditions.checkNotNull(name);
            this.maxParallelism = maxParallelism;
            this.parallelism = parallelism;
            this.executionState = Preconditions.checkNotNull(executionState);
            this.startTime = startTime;
            this.endTime = endTime;
            this.duration = duration;
            this.tasksPerState = Preconditions.checkNotNull(tasksPerState);
            this.jobVertexMetrics = Preconditions.checkNotNull(jobVertexMetrics);
        }

        @JsonIgnore
        public JobVertexID getJobVertexID() {
            return jobVertexID;
        }

        @JsonIgnore
        public String getName() {
            return name;
        }

        @JsonIgnore
        public int getMaxParallelism() {
            return maxParallelism;
        }

        @JsonIgnore
        public int getParallelism() {
            return parallelism;
        }

        @JsonIgnore
        public ExecutionState getExecutionState() {
            return executionState;
        }

        @JsonIgnore
        public long getStartTime() {
            return startTime;
        }

        @JsonIgnore
        public long getEndTime() {
            return endTime;
        }

        @JsonIgnore
        public long getDuration() {
            return duration;
        }

        @JsonIgnore
        public Map<ExecutionState, Integer> getTasksPerState() {
            return tasksPerState;
        }

        @JsonIgnore
        public IOMetricsInfo getJobVertexMetrics() {
            return jobVertexMetrics;
        }
    }
}
