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

package cn.sliew.scaleph.dao.entity.master.security;

import cn.sliew.scaleph.common.dict.security.ResourceType;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 资源-web
 */
@Data
@TableName("sec_resource_web")
@Schema(name = "SecResourceWeb对象", description = "资源-web")
public class SecResourceWeb extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "资源类型。导航，菜单，页面，按钮")
    @TableField("`type`")
    private ResourceType type;

    @Schema(description = "上级资源id")
    @TableField("pid")
    private Long pid;

    @Schema(description = "层级")
    @TableField("`level`")
    private Integer level;

    @Schema(description = "前端名称")
    @TableField("`name`")
    private String name;

    @Schema(description = "前端路由路径")
    @TableField("`path`")
    private String path;

    @Schema(description = "前端重定向路径")
    @TableField("redirect")
    private String redirect;

    @Schema(description = "前端全局布局显示。只在一级生效")
    @TableField("layout")
    private Boolean layout;

    @Schema(description = "前端 icon")
    @TableField("icon")
    private String icon;

    @Schema(description = "前端组件")
    @TableField("`component`")
    private String component;
}
