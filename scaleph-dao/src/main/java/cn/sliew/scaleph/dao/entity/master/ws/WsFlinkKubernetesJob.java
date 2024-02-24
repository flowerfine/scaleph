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

import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.FlinkRuntimeExecutionMode;
import cn.sliew.scaleph.common.dict.flink.kubernetes.DeploymentKind;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * flink kubernetes job
 */
@Data
@TableName("ws_flink_kubernetes_job")
public class WsFlinkKubernetesJob extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("project_id")
    private Long projectId;

    @TableField("`name`")
    private String name;

    @TableField("job_id")
    private String jobId;

    @TableField("execution_mode")
    private FlinkRuntimeExecutionMode executionMode;

    @TableField("deployment_kind")
    private DeploymentKind deploymentKind;

    @TableField("flink_deployment_id")
    private Long flinkDeploymentId;

    @TableField(exist = false)
    private WsFlinkKubernetesDeployment flinkDeployment;

    @TableField("flink_session_cluster_id")
    private Long flinkSessionClusterId;

    @TableField(exist = false)
    private WsFlinkKubernetesSessionCluster flinkSessionCluster;

    @TableField("`type`")
    private FlinkJobType type;

    @TableField("artifact_flink_jar_id")
    private Long artifactFlinkJarId;

    @TableField(exist = false)
    private WsArtifactFlinkJar artifactFlinkJar;

    @TableField("artifact_flink_sql_id")
    private Long artifactFlinkSqlId;

    @TableField(exist = false)
    private WsArtifactFlinkSql artifactFlinkSql;

    @TableField("artifact_flink_cdc_id")
    private Long artifactFlinkCDCId;

    @TableField(exist = false)
    private WsArtifactFlinkCDC artifactFlinkCDC;

    @TableField("artifact_seatunnel_id")
    private Long artifactSeaTunnelId;

    @TableField(exist = false)
    private WsArtifactSeaTunnel artifactSeaTunnel;

    @TableField(exist = false)
    private WsFlinkKubernetesJobInstance jobInstance;

    @TableField("remark")
    private String remark;
}
