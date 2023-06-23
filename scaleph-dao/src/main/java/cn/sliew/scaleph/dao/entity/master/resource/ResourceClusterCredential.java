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

package cn.sliew.scaleph.dao.entity.master.resource;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 集群凭证
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("resource_cluster_credential")
public class ResourceClusterCredential extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("`name`")
    private String name;

    @TableField("`context`")
    private String context;

    @TableField("`file_name`")
    private String fileName;

    @TableField("`path`")
    private String path;

    @TableField("remark")
    private String remark;

}
