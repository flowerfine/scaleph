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
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * flink kubernetes job
 * </p>
 */
@Getter
@Setter
@TableName("ws_flink_kubernetes_job")
@ApiModel(value = "WsFlinkKubernetesJob对象", description = "flink kubernetes job")
public class WsFlinkKubernetesJob extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("flink_deployment_mode")
    private String flinkDeploymentMode;

    @TableField("flink_deployment_id")
    private String flinkDeploymentId;

    @TableField("`name`")
    private String name;

    @TableField("job_id")
    private String jobId;

    @TableField("artifact_id")
    private Long artifactId;

    @TableField("remark")
    private String remark;

}
