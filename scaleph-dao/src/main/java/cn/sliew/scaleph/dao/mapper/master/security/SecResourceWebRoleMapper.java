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
import cn.sliew.scaleph.dao.entity.master.security.SecResourceWeb;
import cn.sliew.scaleph.dao.entity.master.security.SecResourceWebRole;
import cn.sliew.scaleph.dao.entity.master.security.SecRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 资源-web与角色关联表 Mapper 接口
 */
@Repository
public interface SecResourceWebRoleMapper extends BaseMapper<SecResourceWebRole> {

    /**
     * 查询 资源-web 关联的角色
     */
    Page<SecRole> selectRelatedRolesByWebResource(Page page,
                                                  @Param("resourceWebId") Long resourceWebId,
                                                  @Param("status") RoleStatus status,
                                                  @Param("name") String name);

    /**
     * 查询 资源-web 未关联的角色
     */
    Page<SecRole> selectUnrelatedRolesByWebResource(Page page,
                                                    @Param("resourceWebId") Long resourceWebId,
                                                    @Param("status") RoleStatus status,
                                                    @Param("name") String name);

    Page<SecResourceWeb> selectAllResourceWebWithAuthorizeStatus(@Param("roleId") Long roleId);
}
