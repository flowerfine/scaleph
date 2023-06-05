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

package cn.sliew.scaleph.security.service;

import cn.sliew.scaleph.security.service.dto.SecRoleDTO;
import cn.sliew.scaleph.security.service.param.SecRoleAddParam;
import cn.sliew.scaleph.security.service.param.SecRoleListParam;
import cn.sliew.scaleph.security.service.param.SecRoleUpdateParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface SecRoleService {

    int insert(SecRoleAddParam param);

    int update(SecRoleUpdateParam param);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);

    SecRoleDTO selectOne(Long id);

    SecRoleDTO selectOne(String roleCode);

    List<SecRoleDTO> listAll();

    Page<SecRoleDTO> listByPage(SecRoleListParam param);

    List<SecRoleDTO> selectRoleByDept(String grant, Long deptId);
}
