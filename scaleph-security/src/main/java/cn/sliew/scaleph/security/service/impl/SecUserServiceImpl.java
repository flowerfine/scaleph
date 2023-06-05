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
import cn.sliew.scaleph.dao.mapper.master.security.SecUserMapper;
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
@Service
public class SecUserServiceImpl implements SecUserService {

    @Autowired
    private SecUserMapper secUserMapper;

    @Override
    public int insert(SecUserDTO secUserDTO) {
        SecUser secUser = SecUserConvert.INSTANCE.toDo(secUserDTO);
        return this.secUserMapper.insert(secUser);
    }

    @Override
    public int update(SecUserDTO secUserDTO) {
        SecUser secUser = SecUserConvert.INSTANCE.toDo(secUserDTO);
        return this.secUserMapper.updateById(secUser);
    }

    @Override
    public int updateByUserName(SecUserDTO secUserDTO) {
        SecUser secUser = SecUserConvert.INSTANCE.toDo(secUserDTO);
        return this.secUserMapper.update(secUser, new LambdaQueryWrapper<SecUser>()
                .eq(SecUser::getUserName, secUser.getUserName())
        );
    }

    @Override
    public int deleteById(Long id) {
        SecUser secUser = new SecUser();
        secUser.setId(id);
        secUser.setStatus(UserStatus.DELETED);
        return this.secUserMapper.updateById(secUser);
    }

    @Override
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        List<Integer> list = new ArrayList<>();
        for (Map.Entry<Integer, ? extends Serializable> entry : map.entrySet()) {
            list.add((Integer) entry.getValue());
        }

        return this.secUserMapper.batchUpdateUserStatus(list, UserStatusEnum.LOGOFF.getValue());

    }

    @Override
    public SecUserDTO selectOne(Long id) {
        SecUser secUser = this.secUserMapper.selectById(id);
        return SecUserConvert.INSTANCE.toDto(secUser);
    }

    @Override
    public SecUserDTO selectOne(String userName) {
        SecUser secUser = this.secUserMapper.selectOne(
                Wrappers.lambdaQuery(SecUser.class).eq(SecUser::getUserName, userName));
        return SecUserConvert.INSTANCE.toDto(secUser);
    }

    @Override
    public Page<SecUserDTO> listByPage(SecUserParam secUserParam) {
        SecUser secUser = new SecUser();
        secUser.setUserName(secUserParam.getUserName());
        secUser.setNickName(secUserParam.getNickName());
        secUser.setEmail(secUserParam.getEmail());
        secUser.setStatus(secUserParam.getUserStatus());
        Page<SecUserDTO> result = new Page<>();
        Page<SecUser> list = this.secUserMapper.selectPage(
                new Page<>(secUserParam.getCurrent(), secUserParam.getPageSize()),
                secUserParam.getDeptId(),
                secUserParam.getRoleId(),
                secUser
        );
        List<SecUserDTO> dtoList = SecUserConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public SecUserDTO selectByEmail(String email) {
        SecUser secUser =
                this.secUserMapper.selectOne(new LambdaQueryWrapper<SecUser>().eq(SecUser::getEmail, email));
        return SecUserConvert.INSTANCE.toDto(secUser);
    }

    @Override
    public List<SecUserDTO> listByRole(Long roleId, String userName, String direction) {
        List<SecUser> list =
                this.secUserMapper.selectByRoleOrDept("", String.valueOf(roleId), userName, direction);
        return SecUserConvert.INSTANCE.toDto(list);
    }

    @Override
    public List<SecUserDTO> listByDept(Long deptId, String userName, String direction) {
        List<SecUser> list =
                this.secUserMapper.selectByRoleOrDept(String.valueOf(deptId), "", userName, direction);
        return SecUserConvert.INSTANCE.toDto(list);
    }

    @Override
    public List<SecUserDTO> listByUserName(String userName) {
        List<SecUser> list = this.secUserMapper.selectList(new LambdaQueryWrapper<SecUser>()
                .like(SecUser::getUserName, userName));
        return SecUserConvert.INSTANCE.toDto(list);
    }

    @Override
    public List<SecRoleDTO> getAllPrivilegeByUserName(String userName) {
        List<SecRole> list = this.secUserMapper.selectAllPrivilege(userName);
        return SecRoleConvert.INSTANCE.toDto(list);
    }
}
