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
import cn.sliew.scaleph.security.service.dto.SecUserDTO;
import cn.sliew.scaleph.security.service.param.SecUserParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户基本信息表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface SecUserService {

    int insert(SecUserDTO secUserDTO);

    int update(SecUserDTO secUserDTO);

    int updateByUserName(SecUserDTO secUserDTO);

    int deleteById(Long id);

    int deleteBatch(Map<Integer, ? extends Serializable> map);

    SecUserDTO selectOne(Long id);

    SecUserDTO selectOne(String userName);

    Page<SecUserDTO> listByPage(SecUserParam secUserParam);

    SecUserDTO selectByEmail(String email);

    List<SecUserDTO> listByRole(Long roleId, String userName, String direction);

    List<SecUserDTO> listByDept(Long deptId, String userName, String direction);

    List<SecUserDTO> listByUserName(String userName);

    List<SecRoleDTO> getAllPrivilegeByUserName(String userName);

}
