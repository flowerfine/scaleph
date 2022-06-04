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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.security.service.dto.SecRoleDTO;
import cn.sliew.scaleph.security.service.dto.SecUserDTO;
import cn.sliew.scaleph.security.service.param.SecUserParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户基本信息表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface SecUserService {
    /**
     * 新增用户
     *
     * @param secUserDTO user
     * @return int
     */
    int insert(SecUserDTO secUserDTO);

    /**
     * 修改用户
     *
     * @param secUserDTO user
     * @return int
     */
    int update(SecUserDTO secUserDTO);

    /**
     * 修改用户
     *
     * @param secUserDTO user
     * @return int
     */
    int updateByUserName(SecUserDTO secUserDTO);

    /**
     * 逻辑删除用户，修改用户状态为注销
     *
     * @param id id
     * @return int
     */
    int deleteById(Long id);

    /**
     * 逻辑删除用户，修改用户状态为注销
     *
     * @param map ids
     * @return int
     */
    int deleteBatch(Map<Integer, ? extends Serializable> map);

    /**
     * 根据id查询用户
     *
     * @param id user id
     * @return user
     */
    SecUserDTO selectOne(Long id);

    /**
     * 根据用户名查询用户
     *
     * @param userName user name
     * @return user
     */
    SecUserDTO selectOne(String userName);

    /**
     * 分页查询
     *
     * @param secUserParam 查询参数
     * @return page list
     */
    Page<SecUserDTO> listByPage(SecUserParam secUserParam);

    /**
     * 根据email查询用户
     *
     * @param email email
     * @return user
     */
    SecUserDTO selectByEmail(String email);

    /**
     * 根据角色查询用户列表
     *
     * @param roleId    role
     * @param direction 0 1
     * @param userName  username
     * @return list
     */
    List<SecUserDTO> listByRole(Long roleId, String userName, String direction);

    /**
     * 根据dept查询用户列表
     *
     * @param deptId    dept
     * @param direction 0 1
     * @param userName  username
     * @return list
     */
    List<SecUserDTO> listByDept(Long deptId, String userName, String direction);


    /**
     * 根据用户名查询用户列表
     *
     * @param userName dept
     * @return list
     */
    List<SecUserDTO> listByUserName(String userName);

    /**
     * 获取用户名对应的全部角色权限信息
     *
     * @param userName user name
     * @return role list
     */
    List<SecRoleDTO> getAllPrivilegeByUserName(String userName);

}
