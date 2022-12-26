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

package cn.sliew.scaleph.dao.entity.master.ws;

import cn.sliew.scaleph.common.dict.flink.FlinkCheckpointStatus;
import cn.sliew.scaleph.common.dict.flink.FlinkCheckpointType;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("ws_flink_checkpoint")
@ApiModel(value = "WsFlinkCheckpoint对象", description = "flink checkpoint")
public class WsFlinkCheckpoint extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("flink job instance id")
    @TableField("flink_job_instance_id")
    private Long flinkJobInstanceId;

    @ApiModelProperty("flink checkpoint id")
    @TableField("flink_checkpoint_id")
    private Long flinkCheckpointId;

    @ApiModelProperty("checkpoint type")
    @TableField("checkpoint_type")
    private FlinkCheckpointType checkpointType;

    @ApiModelProperty("checkpoint status")
    @TableField("`status`")
    private FlinkCheckpointStatus status;

    @ApiModelProperty("is savepoint")
    @TableField("`savepoint`")
    private boolean savepoint;

    @ApiModelProperty("checkpoint trigger timestamp")
    @TableField("trigger_timestamp")
    private Long triggerTimestamp;

    @ApiModelProperty("checkpoint duration")
    @TableField("duration")
    private Long duration;

    @ApiModelProperty("is discarded")
    @TableField("discarded")
    private boolean discarded;

    @ApiModelProperty("checkpoint path")
    @TableField("external_path")
    private String externalPath;

    @ApiModelProperty("state size")
    @TableField("state_size")
    private Long stateSize;

    @ApiModelProperty("processed data size")
    @TableField("processed_data")
    private Long processedData;

    @ApiModelProperty("persisted data size")
    @TableField("persisted_data")
    private Long persistedData;

    @ApiModelProperty("checkpoint alignment buffered size")
    @TableField("alignment_buffered")
    private Long alignmentBuffered;

    @ApiModelProperty("subtask nums")
    @TableField("num_subtasks")
    private Integer numSubtasks;

    @ApiModelProperty("acknowledged subtask nums")
    @TableField("num_acknowledged_subtasks")
    private Integer numAcknowledgedSubtasks;

    @ApiModelProperty("latest acknowledged subtask timestamp")
    @TableField("latest_ack_timestamp")
    private Long latestAckTimestamp;

}
