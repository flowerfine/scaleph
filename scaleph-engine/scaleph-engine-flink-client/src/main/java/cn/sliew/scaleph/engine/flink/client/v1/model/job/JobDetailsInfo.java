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

import cn.sliew.scaleph.engine.flink.client.v1.model.job.metrics.IOMetricsInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;

import java.util.Collection;
import java.util.Map;

@Data
public class JobDetailsInfo {

    @JsonProperty("jid")
    private String jobId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("isStoppable")
    private boolean isStoppable;
    @JsonProperty("state")
    private JobStatus jobStatus;
    @JsonProperty("start-time")
    private long startTime;
    @JsonProperty("end-time")
    private long endTime;
    @JsonProperty("duration")
    private long duration;
    @JsonProperty("maxParallelism")
    private long maxParallelism;
    @JsonProperty("now")
    private long now;
    @JsonProperty("timestamps")
    private Map<JobStatus, Long> timestamps;
    @JsonProperty("vertices")
    private Collection<JobVertexDetailsInfo> jobVertexInfos;
    @JsonProperty("status-counts")
    private Map<ExecutionState, Integer> jobVerticesPerState;
    @JsonProperty("plan")
    @JsonRawValue
    private String jsonPlan;

    @Data
    public static final class JobVertexDetailsInfo {
        @JsonProperty("id")
        private String jobVertexID;
        @JsonProperty("name")
        private String name;
        @JsonProperty("maxParallelism")
        private int maxParallelism;
        @JsonProperty("parallelism")
        private int parallelism;
        @JsonProperty("status")
        private ExecutionState executionState;
        @JsonProperty("start-time")
        private long startTime;
        @JsonProperty("end-time")
        private long endTime;
        @JsonProperty("duration")
        private long duration;
        @JsonProperty("tasks")
        private Map<ExecutionState, Integer> tasksPerState;
        @JsonProperty("metrics")
        private IOMetricsInfo jobVertexMetrics;
    }
}
