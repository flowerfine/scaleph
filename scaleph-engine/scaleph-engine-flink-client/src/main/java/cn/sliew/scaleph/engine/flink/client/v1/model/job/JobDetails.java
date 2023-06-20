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

import java.util.Map;
import java.util.Set;

/**
 * An actor message with a detailed overview of the current status of a job.
 */
@Data
public class JobDetails {

    @JsonProperty("jid")
    private String jobId;
    @JsonProperty("name")
    private String jobName;
    @JsonProperty("start-time")
    private long startTime;
    @JsonProperty("end-time")
    private long endTime;
    @JsonProperty("duration")
    private long duration;
    @JsonProperty("state")
    private JobStatus status;
    @JsonProperty("last-modification")
    private long lastUpdateTime;
    private int[] tasksPerState;
    @JsonProperty("total")
    private int numTasks;
    @JsonProperty("tasks")
    private transient Map<String, Integer> lazyTaskInfo = null;

    /**
     * The map holds the attempt number of the current execution attempt in the Execution, which is
     * considered as the representing execution for the subtask of the vertex. The keys and values
     * are JobVertexID -> SubtaskIndex -> CurrenAttempts info.
     *
     * <p>The field is excluded from the json. Any usage from the web UI and the history server is
     * not allowed.
     */
    private final Map<String, Map<Integer, CurrentAttempts>> currentExecutionAttempts;

    /**
     * The CurrentAttempts holds the attempt number of the current representative execution attempt,
     * and the attempt numbers of all the running attempts.
     */
    @Data
    public static final class CurrentAttempts {

        private int representativeAttempt;
        private Set<Integer> currentAttempts;
    }
}
