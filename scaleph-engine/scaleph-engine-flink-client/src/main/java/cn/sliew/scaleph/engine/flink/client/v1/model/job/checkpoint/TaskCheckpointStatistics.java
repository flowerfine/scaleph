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

package cn.sliew.scaleph.engine.flink.client.v1.model.job.checkpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskCheckpointStatistics {

    @JsonProperty("id")
    private long checkpointId;
    @JsonProperty("status")
    private CheckpointStatsStatus checkpointStatus;
    @JsonProperty("latest_ack_timestamp")
    private long latestAckTimestamp;
    @JsonProperty("checkpointed_size")
    private long checkpointedSize;
    @JsonProperty("state_size")
    private long stateSize;
    @JsonProperty("end_to_end_duration")
    private long duration;
    @JsonProperty("alignment_buffered")
    private long alignmentBuffered;
    @JsonProperty("processed_data")
    private long processedData;
    @JsonProperty("persisted_data")
    private long persistedData;
    @JsonProperty("num_subtasks")
    private int numSubtasks;
    @JsonProperty("num_acknowledged_subtasks")
    private int numAckSubtasks;
}
