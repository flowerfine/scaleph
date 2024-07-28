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

package cn.sliew.scaleph.security.service.impl;

import cn.sliew.scaleph.common.dict.security.UserStatus;
import cn.sliew.scaleph.common.enums.UserStatusEnum;
import cn.sliew.scaleph.dao.entity.master.security.SecRole;
import cn.sliew.scaleph.dao.entity.master.security.SecUser;
import cn.sliew.scaleph.security.service.SecUserService;
import cn.sliew.scaleph.security.service.convert.SecRoleConvert;
import cn.sliew.scaleph.security.service.convert.SecUserConvert;
import cn.sliew.scaleph.security.service.dto.SecRoleDTO;
import cn.sliew.scaleph.security.service.dto.SecUserDTO;
import cn.sliew.scaleph.security.service.param.SecUserParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户基本信息表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
//@Service
public class SecUserServiceImpl implements SecUserService {

    @Override
    public int insert(SecUserDTO secUserDTO) {
        return 0;
    }

    @Override
    public int update(SecUserDTO secUserDTO) {
        return 0;
    }

    @Override
    public int updateByUserName(SecUserDTO secUserDTO) {
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        return 0;
    }

    @Override
    public SecUserDTO selectOne(Long id) {
        return null;
    }

    @Override
    public SecUserDTO selectOne(String userName) {
        return null;
    }

    @Override
    public Page<SecUserDTO> listByPage(SecUserParam secUserParam) {
        return null;
    }

    @Override
    public SecUserDTO selectByEmail(String email) {
        return null;
    }

    @Override
    public List<SecUserDTO> listByRole(Long roleId, String userName, String direction) {
        return null;
    }

    @Override
    public List<SecUserDTO> listByDept(Long deptId, String userName, String direction) {
        return null;
    }

    @Override
    public List<SecUserDTO> listByUserName(String userName) {
        return null;
    }

    @Override
    public List<SecRoleDTO> getAllPrivilegeByUserName(String userName) {
        return null;
    }
}
