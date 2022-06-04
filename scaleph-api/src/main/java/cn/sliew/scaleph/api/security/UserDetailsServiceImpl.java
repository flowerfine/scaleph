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

package cn.sliew.scaleph.api.security;

import java.util.ArrayList;
import java.util.List;

import cn.sliew.scaleph.api.util.I18nUtil;
import cn.sliew.scaleph.common.enums.UserStatusEnum;
import cn.sliew.scaleph.security.service.SecUserService;
import cn.sliew.scaleph.security.service.dto.SecPrivilegeDTO;
import cn.sliew.scaleph.security.service.dto.SecRoleDTO;
import cn.sliew.scaleph.security.service.dto.SecUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author gleiyu
 */
@Slf4j
@Component
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
        boolean flag = secUserDTO.getUserStatus() != null
            &&
            !(secUserDTO.getUserStatus().getValue().equals(UserStatusEnum.UNBIND_EMAIL.getValue()) ||
                secUserDTO.getUserStatus().getValue().equals(UserStatusEnum.BIND_EMAIL.getValue()));
        if (secUserDTO == null) {
            throw new BadCredentialsException(I18nUtil.get("response.error.login.password"));
        } else if (flag) {
            throw new BadCredentialsException(I18nUtil.get("response.error.login.disable"));
        } else {
            UserDetailInfo user = new UserDetailInfo();
            user.setUser(secUserDTO);
            //查询用户角色权限信息
            List<SecRoleDTO> privileges = this.secUserService.getAllPrivilegeByUserName(userName);
            user.setAuthorities(this.toGrantedAuthority(privileges));
            return user;
        }
    }

    private List<GrantedAuthority> toGrantedAuthority(List<SecRoleDTO> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        } else {
            List<GrantedAuthority> list = new ArrayList<>();
            for (SecRoleDTO role : roles) {
                if (role.getPrivileges() == null) {
                    continue;
                }
                for (SecPrivilegeDTO privilege : role.getPrivileges()) {
                    GrantedAuthority grantedAuthority =
                        new SimpleGrantedAuthority(privilege.getPrivilegeCode());
                    list.add(grantedAuthority);
                }
            }
            return list;
        }
    }
}
