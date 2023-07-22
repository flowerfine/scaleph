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

import cn.sliew.scaleph.common.dict.flink.kubernetes.ResourceLifecycleState;
import cn.sliew.scaleph.common.dict.flink.kubernetes.UpgradeMode;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * flink kubernetes job instance
 * </p>
 */
@Data
@TableName("ws_flink_kubernetes_job_instance")
public class WsFlinkKubernetesJobInstance extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("ws_flink_kubernetes_job_id")
    private Long wsFlinkKubernetesJobId;

    @TableField(exist = false)
    private WsFlinkKubernetesJob wsFlinkKubernetesJob;

    @TableField("instance_id")
    private String instanceId;

    @TableField("parallelism")
    private Integer parallelism;

    @TableField("upgrade_mode")
    private UpgradeMode upgradeMode;

    @TableField("allow_non_restored_state")
    private Boolean allowNonRestoredState;

    @TableField("job_manager")
    private String jobManager;

    @TableField("task_manager")
    private String taskManager;

    @TableField("user_flink_configuration")
    private String userFlinkConfiguration;

    @TableField(value = "`state`", updateStrategy = FieldStrategy.IGNORED)
    private ResourceLifecycleState state;

    @TableField(value = "`error`", updateStrategy = FieldStrategy.IGNORED)
    private String error;

    @TableField(value = "cluster_info", updateStrategy = FieldStrategy.IGNORED)
    private String clusterInfo;

    @TableField(value = "task_manager_info", updateStrategy = FieldStrategy.IGNORED)
    private String taskManagerInfo;

    @TableField("start_time")
    private Date startTime;

    @TableField("end_time")
    private Date endTime;

    @TableField("duration")
    private Long duration;

}
