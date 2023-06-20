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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import javax.annotation.Nullable;
import java.util.Map;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "className")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CheckpointStatistics.CompletedCheckpointStatistics.class, name = "completed"),
        @JsonSubTypes.Type(value = CheckpointStatistics.FailedCheckpointStatistics.class, name = "failed"),
        @JsonSubTypes.Type(value = CheckpointStatistics.PendingCheckpointStatistics.class, name = "in_progress")
})
public class CheckpointStatistics {

    @JsonProperty("id")
    private long id;
    @JsonProperty("status")
    private CheckpointStatsStatus status;
    @JsonProperty("is_savepoint")
    private boolean savepoint;
    @JsonProperty("savepointFormat")
    @Nullable
    private String savepointFormat;
    @JsonProperty("trigger_timestamp")
    private long triggerTimestamp;
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
    @JsonProperty("checkpoint_type")
    private RestAPICheckpointType checkpointType;
    @JsonProperty("tasks")
    private Map<String, TaskCheckpointStatistics> checkpointStatisticsPerTask;

    @Data
    public static final class PendingCheckpointStatistics extends CheckpointStatistics {

    }

    @Data
    public static final class FailedCheckpointStatistics extends CheckpointStatistics {

        @JsonProperty("failure_timestamp")
        private long failureTimestamp;
        @JsonProperty("failure_message")
        @Nullable
        private String failureMessage;
    }

    @Data
    public static final class CompletedCheckpointStatistics extends CheckpointStatistics {

        @JsonProperty("external_path")
        @Nullable
        private String externalPath;
        @JsonProperty("discarded")
        private boolean discarded;
    }

    enum RestAPICheckpointType {
        CHECKPOINT,
        SAVEPOINT,
        SYNC_SAVEPOINT;

        public static RestAPICheckpointType valueOf(SnapshotType checkpointType) {
            if (checkpointType.isSavepoint()) {
                SavepointType savepointType = (SavepointType) checkpointType;
                return savepointType.isSynchronous() ? SYNC_SAVEPOINT : SAVEPOINT;
            } else {
                return CHECKPOINT;
            }
        }
    }
}
