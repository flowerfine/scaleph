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
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * doris instance
 */
@Data
@TableName("ws_doris_operator_instance")
public class WsDorisOperatorInstance extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("project_id")
    private Long projectId;

    @TableField("cluster_credential_id")
    private Long clusterCredentialId;

    @TableField("`name`")
    private String name;

    @TableField("instance_id")
    private String instanceId;

    @TableField("namespace")
    private String namespace;

    @TableField("`admin`")
    private String admin;

    @TableField("fe_spec")
    private String feSpec;

    @TableField("be_spec")
    private String beSpec;

    @TableField("cn_spec")
    private String cnSpec;

    @TableField("broker_spec")
    private String brokerSpec;

    @TableField("deployed")
    private YesOrNo deployed;

    @TableField("fe_status")
    private String feStatus;

    @TableField("be_status")
    private String beStatus;

    @TableField("cn_status")
    private String cnStatus;

    @TableField("broker_status")
    private String brokerStatus;

    @TableField("remark")
    private String remark;
}
