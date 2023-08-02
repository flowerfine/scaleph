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

import cn.sliew.scaleph.common.dict.flink.kubernetes.SavepointFormatType;
import cn.sliew.scaleph.common.dict.flink.kubernetes.SavepointTriggerType;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * flink kubernetes job instance savepoint
 */
@Data
@TableName("ws_flink_kubernetes_job_instance_savepoint")
public class WsFlinkKubernetesJobInstanceSavepoint extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("ws_flink_kubernetes_job_instance_id")
    private Long wsFlinkKubernetesJobInstanceId;

    @TableField("time_stamp")
    private Long timeStamp;

    @TableField("location")
    private String location;

    @TableField("trigger_type")
    private SavepointTriggerType triggerType;

    @TableField("format_type")
    private SavepointFormatType formatType;
}
