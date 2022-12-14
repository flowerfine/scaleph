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

import java.util.List;

import cn.sliew.scaleph.common.dict.security.RoleStatus;
import cn.sliew.scaleph.common.dict.security.RoleType;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sec_role", resultMap = "SecRoleMap")
@ApiModel(value = "SecRole对象", description = "角色表")
public class SecRole extends BaseDO {

    private static final long serialVersionUID = 2621684597930016649L;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色类型")
    private RoleType roleType;

    @ApiModelProperty(value = "角色状态")
    private RoleStatus roleStatus;

    @ApiModelProperty(value = "角色备注")
    private String roleDesc;

    @ApiModelProperty(value = "权限信息")
    private List<SecPrivilege> privileges;
}
