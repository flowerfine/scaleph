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

package cn.sliew.scaleph.dao.mapper.master.security;

import cn.sliew.scaleph.common.dict.security.RoleStatus;
import cn.sliew.scaleph.common.dict.security.UserStatus;
import cn.sliew.scaleph.dao.entity.master.security.SecRole;
import cn.sliew.scaleph.dao.entity.master.security.SecUser;
import cn.sliew.scaleph.dao.entity.master.security.SecUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Repository
public interface SecUserRoleMapper extends BaseMapper<SecUserRole> {

    /**
     * 查询角色关联的用户
     */
    Page<SecUser> selectRelatedUsersByRole(Page page,
                                           @Param("roleId") Long roleId,
                                           @Param("status") UserStatus status,
                                           @Param("userName") String userName);

    /**
     * 查询角色未关联的用户
     */
    Page<SecUser> selectUnrelatedUsersByRole(Page page,
                                             @Param("roleId") Long roleId,
                                             @Param("status") UserStatus status,
                                             @Param("userName") String userName);

    /**
     * 查询用户关联的角色
     */
    List<SecRole> selectRelatedRolesByUser(@Param("userId") Long userId,
                                           @Param("status") RoleStatus status,
                                           @Param("name") String name);

    /**
     * 查询用户未关联的角色
     */
    List<SecRole> selectUnrelatedRolesByUser(@Param("userId") Long userId,
                                             @Param("status") RoleStatus status,
                                             @Param("name") String name);
}
