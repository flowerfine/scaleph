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

package cn.sliew.scaleph.dao.entity.master.dataservice;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 数据服务 配置
 * </p>
 */
@Getter
@Setter
@TableName("dataservice_config")
@Schema(name = "DataserviceConfig对象", description = "数据服务 配置")
public class DataserviceConfig extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "name")
    @TableField("`name`")
    private String name;

    @Schema(description = "uri path")
    @TableField("path")
    private String path;

    @Schema(description = "http method")
    @TableField("method")
    private String method;

    @Schema(description = "http content type")
    @TableField("content_type")
    private String contentType;

    @Schema(description = "status, disabled or enabled")
    @TableField("`status`")
    private String status;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;


}
