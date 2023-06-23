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

import cn.sliew.scaleph.common.dict.flink.FlinkDeploymentMode;
import cn.sliew.scaleph.common.dict.flink.FlinkResourceProvider;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.dao.entity.BaseDO;
import cn.sliew.scaleph.dao.entity.master.resource.ResourceClusterCredential;
import cn.sliew.scaleph.dao.entity.master.resource.ResourceFlinkRelease;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * flink 集群配置
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ws_flink_cluster_config")
public class WsFlinkClusterConfig extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("project_id")
    private Long projectId;

    @TableField("`name`")
    private String name;

    @TableField("flink_version")
    private FlinkVersion flinkVersion;

    @TableField("resource_provider")
    private FlinkResourceProvider resourceProvider;

    @TableField("deploy_mode")
    private FlinkDeploymentMode deployMode;

    @TableField(exist = false)
    private ResourceFlinkRelease flinkRelease;

    @TableField("flink_release_id")
    private Long flinkReleaseId;

    @TableField(exist = false)
    private ResourceClusterCredential clusterCredential;

    @TableField("cluster_credential_id")
    private Long clusterCredentialId;

    @TableField("kubernetes_options")
    private String kubernetesOptions;

    @TableField("config_options")
    private String configOptions;

    @TableField("remark")
    private String remark;
}
