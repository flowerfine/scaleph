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

import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelEngineType;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelVersion;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * artifact seatunnel
 */
@Data
@TableName("ws_artifact_seatunnel")
@Schema(name = "WsArtifactSeaTunnel", description = "artifact seatunnel")
public class WsArtifactSeaTunnel extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "作业artifact id")
    @TableField("artifact_id")
    private Long artifactId;

    @TableField(exist = false)
    private WsArtifact artifact;

    @Schema(description = "seatunnel 引擎")
    @TableField("seatunnel_engine")
    private SeaTunnelEngineType seaTunnelEngine;

    @Schema(description = "flink 版本")
    @TableField("flink_version")
    private FlinkVersion flinkVersion;

    @Schema(description = "seatunnel 版本")
    @TableField("seatunnel_version")
    private SeaTunnelVersion seaTunnelVersion;

    @TableField("dag_id")
    private Long dagId;

    @Schema(description = "current artifact")
    @TableField("`current`")
    private YesOrNo current;
}
