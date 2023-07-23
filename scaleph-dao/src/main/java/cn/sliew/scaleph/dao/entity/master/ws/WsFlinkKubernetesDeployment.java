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

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * flink kubernetes deployment
 * </p>
 */
@Data
@EqualsAndHashCode
@TableName("ws_flink_kubernetes_deployment")
public class WsFlinkKubernetesDeployment extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("project_id")
    private Long projectId;

    @TableField("cluster_credential_id")
    private Long clusterCredentialId;

    @TableField("`name`")
    private String name;

    @TableField("deployment_id")
    private String deploymentId;

    @TableField("namespace")
    private String namespace;

    @TableField("kubernetes_options")
    private String kubernetesOptions;

    @TableField("job_manager")
    private String jobManager;

    @TableField("task_manager")
    private String taskManager;

    @TableField("pod_template")
    private String podTemplate;

    @TableField("flink_configuration")
    private String flinkConfiguration;

    @TableField("log_configuration")
    private String logConfiguration;

    @TableField("ingress")
    private String ingress;

    @TableField("additional_dependencies")
    private String additionalDependencies;

    @TableField("remark")
    private String remark;

}
