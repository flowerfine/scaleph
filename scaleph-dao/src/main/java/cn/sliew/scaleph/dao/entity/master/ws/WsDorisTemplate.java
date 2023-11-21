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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * doris template
 */
@Data
@TableName("ws_doris_template")
@Schema(name = "WsDorisTemplate对象", description = "doris template")
public class WsDorisTemplate extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "项目id")
    @TableField("project_id")
    private Long projectId;

    @TableField("`name`")
    private String name;

    @TableField("template_id")
    private String templateId;

    @TableField("namespace")
    private String namespace;

    @Schema(description = "session handler")
    @TableField("`admin`")
    private String admin;

    @Schema(description = "fe spec")
    @TableField("fe_spec")
    private String feSpec;

    @Schema(description = "be spec")
    @TableField("be_spec")
    private String beSpec;

    @Schema(description = "cn spec")
    @TableField("cn_spec")
    private String cnSpec;

    @Schema(description = "broker spec")
    @TableField("broker_spec")
    private String brokerSpec;

    @TableField("remark")
    private String remark;
}
