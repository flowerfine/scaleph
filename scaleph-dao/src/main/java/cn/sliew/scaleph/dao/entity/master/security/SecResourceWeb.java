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
import lombok.Data;

/**
 * 资源-web
 */
@Data
@TableName("sec_resource_web")
public class SecResourceWeb extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("`type`")
    private ResourceType type;

    @TableField("pid")
    private Long pid;
    
    @TableField("`name`")
    private String name;

    @TableField("`path`")
    private String path;

    @TableField("redirect")
    private String redirect;

    @TableField("layout")
    private Boolean layout;

    @TableField("icon")
    private String icon;

    @TableField("`component`")
    private String component;
}
