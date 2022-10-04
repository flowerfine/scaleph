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

package cn.sliew.scaleph.security.web;

import cn.sliew.scaleph.common.enums.UserStatusEnum;
import cn.sliew.scaleph.security.service.SecUserService;
import cn.sliew.scaleph.security.service.dto.SecRoleDTO;
import cn.sliew.scaleph.security.service.dto.SecUserDTO;
import cn.sliew.scaleph.system.util.I18nUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gleiyu
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SecUserService secUserService;

    /**
     * 根据用户名查询登录用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        SecUserDTO secUserDTO = secUserService.selectOne(userName);
        if (secUserDTO == null) {
            throw new BadCredentialsException(I18nUtil.get("response.error.login.password"));
        }

        boolean flag = secUserDTO.getUserStatus() != null
                &&
                !(secUserDTO.getUserStatus().getValue().equals(UserStatusEnum.UNBIND_EMAIL.getValue()) ||
                        secUserDTO.getUserStatus().getValue().equals(UserStatusEnum.BIND_EMAIL.getValue()));
        if (flag) {
            throw new BadCredentialsException(I18nUtil.get("response.error.login.disable"));
        }

        UserDetailInfo user = new UserDetailInfo();
        user.setUser(secUserDTO);
        //查询用户角色权限信息
        List<SecRoleDTO> privileges = secUserService.getAllPrivilegeByUserName(userName);
        user.setAuthorities(toGrantedAuthority(privileges));
        return user;
    }

    private List<GrantedAuthority> toGrantedAuthority(List<SecRoleDTO> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        }
        return roles.stream()
                .flatMap(role -> role.getPrivileges().stream())
                .map(privilege -> new SimpleGrantedAuthority(privilege.getPrivilegeCode()))
                .collect(Collectors.toList());
    }
}
