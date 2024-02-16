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
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * flink artifact sql
 * </p>
 */
@Data
@TableName("ws_flink_artifact_sql")
public class WsFlinkArtifactSql extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("flink_artifact_id")
    private Long flinkArtifactId;

    @TableField(exist = false)
    private WsArtifact wsArtifact;

    @TableField("flink_version")
    private FlinkVersion flinkVersion;

    @TableField("script")
    private String script;

    @TableField("`current`")
    private YesOrNo current;
}
