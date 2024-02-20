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

package cn.sliew.scaleph.workspace.flink.service.dto;

import cn.sliew.scaleph.common.dict.flink.FlinkCheckpointStatus;
import cn.sliew.scaleph.common.dict.flink.FlinkCheckpointType;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * flink checkpoint
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "WsFlinkCheckpoint对象", description = "flink checkpoint")
public class WsFlinkCheckpointDTO extends BaseDTO {

    @Schema(description = "flink job instance id")
    private Long flinkJobInstanceId;

    @Schema(description = "flink checkpoint id")
    private Long flinkCheckpointId;

    @Schema(description = "checkpoint type")
    private FlinkCheckpointType checkpointType;

    @Schema(description = "checkpoint status")
    private FlinkCheckpointStatus status;

    @Schema(description = "is savepoint")
    private boolean savepoint;

    @Schema(description = "checkpoint trigger timestamp")
    private Long triggerTimestamp;

    @Schema(description = "checkpoint duration")
    private Long duration;

    @Schema(description = "is discarded")
    private boolean discarded;

    @Schema(description = "checkpoint path")
    private String externalPath;

    @Schema(description = "state size")
    private Long stateSize;

    @Schema(description = "processed data size")
    private Long processedData;

    @Schema(description = "persisted data size")
    private Long persistedData;

    @Schema(description = "checkpoint alignment buffered size")
    private Long alignmentBuffered;

    @Schema(description = "subtask nums")
    private Integer numSubtasks;

    @Schema(description = "acknowledged subtask nums")
    private Integer numAcknowledgedSubtasks;

    @Schema(description = "latest acknowledged subtask timestamp")
    private Long latestAckTimestamp;

}
