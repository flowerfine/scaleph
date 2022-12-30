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

package cn.sliew.scaleph.engine.flink.service.dto;

import cn.sliew.scaleph.common.dict.flink.FlinkCheckpointStatus;
import cn.sliew.scaleph.common.dict.flink.FlinkCheckpointType;
import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * flink checkpoint
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WsFlinkCheckpoint对象", description = "flink checkpoint")
public class WsFlinkCheckpointDTO extends BaseDTO {

    @ApiModelProperty("flink job instance id")
    private Long flinkJobInstanceId;

    @ApiModelProperty("flink checkpoint id")
    private Long flinkCheckpointId;

    @ApiModelProperty("checkpoint type")
    private FlinkCheckpointType checkpointType;

    @ApiModelProperty("checkpoint status")
    private FlinkCheckpointStatus status;

    @ApiModelProperty("is savepoint")
    private boolean savepoint;

    @ApiModelProperty("checkpoint trigger timestamp")
    private Long triggerTimestamp;

    @ApiModelProperty("checkpoint duration")
    private Long duration;

    @ApiModelProperty("is discarded")
    private boolean discarded;

    @ApiModelProperty("checkpoint path")
    private String externalPath;

    @ApiModelProperty("state size")
    private Long stateSize;

    @ApiModelProperty("processed data size")
    private Long processedData;

    @ApiModelProperty("persisted data size")
    private Long persistedData;

    @ApiModelProperty("checkpoint alignment buffered size")
    private Long alignmentBuffered;

    @ApiModelProperty("subtask nums")
    private Integer numSubtasks;

    @ApiModelProperty("acknowledged subtask nums")
    private Integer numAcknowledgedSubtasks;

    @ApiModelProperty("latest acknowledged subtask timestamp")
    private Long latestAckTimestamp;

}
