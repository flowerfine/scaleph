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
public class WsFlinkCheckpoint extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("flink_job_instance_id")
    private Long flinkJobInstanceId;

    @TableField("flink_checkpoint_id")
    private Long flinkCheckpointId;

    @TableField("checkpoint_type")
    private FlinkCheckpointType checkpointType;

    @TableField("`status`")
    private FlinkCheckpointStatus status;

    @TableField("`savepoint`")
    private boolean savepoint;

    @TableField("trigger_timestamp")
    private Long triggerTimestamp;

    @TableField("duration")
    private Long duration;

    @TableField("discarded")
    private boolean discarded;

    @TableField("external_path")
    private String externalPath;

    @TableField("state_size")
    private Long stateSize;

    @TableField("processed_data")
    private Long processedData;

    @TableField("persisted_data")
    private Long persistedData;

    @TableField("alignment_buffered")
    private Long alignmentBuffered;

    @TableField("num_subtasks")
    private Integer numSubtasks;

    @TableField("num_acknowledged_subtasks")
    private Integer numAcknowledgedSubtasks;

    @TableField("latest_ack_timestamp")
    private Long latestAckTimestamp;

}
