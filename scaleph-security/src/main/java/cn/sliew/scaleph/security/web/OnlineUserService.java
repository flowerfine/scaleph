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

import cn.sliew.scaleph.cache.util.RedisUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.security.service.SecRoleService;
import cn.sliew.scaleph.security.service.SecUserService;
import cn.sliew.scaleph.security.service.dto.SecPrivilegeDTO;
import cn.sliew.scaleph.security.service.dto.SecRoleDTO;
import cn.sliew.scaleph.security.vo.OnlineUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 用户在线信息service
 *
 * @author gleiyu
 */
@Service
public class OnlineUserService {

    @Autowired
    private SecurityProperties properties;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SecUserService secUserService;
    @Autowired
    private SecRoleService secRoleService;

    /**
     * 存储登录用户信息到redis中
     *
     * @param userInfo 登录用户
     * @param token    jwt token
     */
    public void insert(UserDetailInfo userInfo, String token) {
        OnlineUserVO onlineUser = new OnlineUserVO();
        onlineUser.setToken(token);
        onlineUser.setEmail(userInfo.getUser().getEmail());
        onlineUser.setIpAddress(userInfo.getLoginIpAddress());
        onlineUser.setLoginTime(userInfo.getLoginTime());
        onlineUser.setUserName(userInfo.getUsername());
        onlineUser.setRemember(userInfo.getRemember());
        Set<String> roles = new TreeSet<>();
        Set<String> privileges = new TreeSet<>();
        for (SecRoleDTO r : userInfo.getUser().getRoles()) {
            roles.add(r.getRoleCode().toLowerCase());
            if (r.getPrivileges() == null) {
                continue;
            }
            for (SecPrivilegeDTO p : r.getPrivileges()) {
                privileges.add(p.getPrivilegeCode().toLowerCase());
            }
        }
        onlineUser.setRoles(new ArrayList<>(roles));
        onlineUser.setPrivileges(new ArrayList<>(privileges));
        //单点登录，踢出之前登录的用户数据
        String userToken =
            (String) redisUtil.get(Constants.ONLINE_USER_KEY + onlineUser.getUserName());
        if (userToken != null) {
            redisUtil.delKeys(Constants.ONLINE_USER_KEY + onlineUser.getUserName());
            redisUtil.delKeys(Constants.ONLINE_TOKEN_KEY + userToken);
        }
        long time = onlineUser.getRemember() ? properties.getLongTokenValidityInSeconds() / 1000 :
            properties.getTokenValidityInSeconds() / 1000;
        redisUtil.set(Constants.ONLINE_USER_KEY + onlineUser.getUserName(), token, time);
        redisUtil.set(Constants.ONLINE_TOKEN_KEY + token, onlineUser, time);
    }

    /**
     * 登出用户
     *
     * @param userName 用户名
     */

    public void logoutByUserName(String userName) {
        String userToken = (String) redisUtil.get(Constants.ONLINE_USER_KEY + userName);
        redisUtil.delKeys(Constants.ONLINE_TOKEN_KEY + userToken);
        redisUtil.delKeys(Constants.ONLINE_USER_KEY + userName);
    }

    /**
     * 踢出用户
     *
     * @param token 用户token
     */

    public void logoutByToken(String token) {
        OnlineUserVO onlineUser =
            (OnlineUserVO) this.redisUtil.get(Constants.ONLINE_TOKEN_KEY + token);
        redisUtil.delKeys(Constants.ONLINE_TOKEN_KEY + token);
        redisUtil.delKeys(Constants.ONLINE_USER_KEY + onlineUser.getUserName());
    }


    public OnlineUserVO getAllPrivilegeByToken(String token) {
        OnlineUserVO onlineUser =
            (OnlineUserVO) this.redisUtil.get(Constants.ONLINE_TOKEN_KEY + token);
        long now = System.currentTimeMillis();
        long time = this.redisUtil.getExipre(Constants.ONLINE_TOKEN_KEY + token);
        if (onlineUser != null && onlineUser.getPrivileges() != null &&
            onlineUser.getRoles() != null) {
            onlineUser.setExpireTime((time * 1000) + now);
            return onlineUser;
        } else if (onlineUser != null) {
            //缓存中信息失效，从数据库中获取权限信息并刷新缓存
            String userName = onlineUser.getUserName();
            List<SecRoleDTO> roleList = this.secUserService.getAllPrivilegeByUserName(userName);
            Set<String> roles = new TreeSet<>();
            Set<String> privileges = new TreeSet<>();
            for (SecRoleDTO role : roleList) {
                roles.add(role.getRoleCode().toLowerCase());
                if (role.getPrivileges() == null) {
                    continue;
                }
                for (SecPrivilegeDTO privilege : role.getPrivileges()) {
                    privileges.add(privilege.getPrivilegeCode().toLowerCase());
                }
            }
            onlineUser.setRoles(new ArrayList<>(roles));
            onlineUser.setPrivileges(new ArrayList<>(privileges));
            onlineUser.setExpireTime((time * 1000) + now);
            return onlineUser;
        } else {
            return null;
        }
    }


    @Async
    public void disableOnlineCacheRole(Long roleId) {
        SecRoleDTO secRoleDTO = secRoleService.selectOne(roleId);
        String code = secRoleDTO.getRoleCode();
        if (!StringUtils.isEmpty(code)) {
            List<String> keys = redisUtil.scan(Constants.ONLINE_TOKEN_KEY + "*");
            for (String key : keys) {
                OnlineUserVO onlineUser = (OnlineUserVO) redisUtil.get(key);
                if (onlineUser != null && onlineUser.getRoles() != null) {
                    for (String r : onlineUser.getRoles()) {
                        if (code.equalsIgnoreCase(r)) {
                            //清空权限数据
                            onlineUser.setPrivileges(null);
                            onlineUser.setRoles(null);
                            redisUtil.set(key, onlineUser);
                        }
                    }
                }
            }
        }
    }


    @Async
    public void disableOnlineCacheUser(String userName) {
        String token = (String) redisUtil.get(Constants.ONLINE_USER_KEY + userName);
        if (!StringUtils.isEmpty(token)) {
            OnlineUserVO onlineUser =
                (OnlineUserVO) redisUtil.get(Constants.ONLINE_TOKEN_KEY + token);
            if (onlineUser != null) {
                onlineUser.setPrivileges(null);
                onlineUser.setRoles(null);
                redisUtil.set(Constants.ONLINE_TOKEN_KEY + token, onlineUser);
            }
        }
    }
}
