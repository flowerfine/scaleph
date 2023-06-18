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

import javax.annotation.Nullable;
import java.util.List;

@Data
public class CheckpointingStatistics {

    @JsonProperty("counts")
    private Counts counts;
    @JsonProperty("summary")
    private Summary summary;
    @JsonProperty("latest")
    private LatestCheckpoints latestCheckpoints;
    @JsonProperty("history")
    private List<CheckpointStatistics> history;

    @Data
    public static final class RestoredCheckpointStatistics {

        @JsonProperty("id")
        private long id;
        @JsonProperty("restore_timestamp")
        private long restoreTimestamp;
        @JsonProperty("is_savepoint")
        private boolean savepoint;
        @JsonProperty("external_path")
        @Nullable
        private String externalPath;
    }

    @Data
    public static final class LatestCheckpoints {

        @JsonProperty("completed")
        @Nullable
        private CheckpointStatistics.CompletedCheckpointStatistics completedCheckpointStatistics;
        @JsonProperty("savepoint")
        @Nullable
        private CheckpointStatistics.CompletedCheckpointStatistics savepointStatistics;
        @JsonProperty("failed")
        @Nullable
        private CheckpointStatistics.FailedCheckpointStatistics failedCheckpointStatistics;
        @JsonProperty("restored")
        @Nullable
        private RestoredCheckpointStatistics restoredCheckpointStatistics;
    }

    @Data
    public static final class Summary {

        @JsonProperty("checkpointed_size")
        private StatsSummaryDto checkpointedSize;
        @JsonProperty("state_size")
        private StatsSummaryDto stateSize;
        @JsonProperty("end_to_end_duration")
        private StatsSummaryDto duration;
        @JsonProperty("alignment_buffered")
        private StatsSummaryDto alignmentBuffered;
        @JsonProperty("processed_data")
        private StatsSummaryDto processedData;
        @JsonProperty("persisted_data")
        private StatsSummaryDto persistedData;
    }

    @Data
    public static final class Counts {

        @JsonProperty("restored")
        private long numberRestoredCheckpoints;
        @JsonProperty("total")
        private long totalNumberCheckpoints;
        @JsonProperty("in_progress")
        private int numberInProgressCheckpoints;
        @JsonProperty("completed")
        private long numberCompletedCheckpoints;
        @JsonProperty("failed")
        private long numberFailedCheckpoints;
    }
}
