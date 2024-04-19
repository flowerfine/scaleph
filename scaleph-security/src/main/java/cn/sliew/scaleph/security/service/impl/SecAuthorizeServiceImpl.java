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

import cn.sliew.scaleph.dao.entity.master.security.*;
import cn.sliew.scaleph.dao.mapper.master.security.SecResourceWebRoleMapper;
import cn.sliew.scaleph.dao.mapper.master.security.SecUserRoleMapper;
import cn.sliew.scaleph.security.authentication.UserDetailInfo;
import cn.sliew.scaleph.security.service.SecAuthorizeService;
import cn.sliew.scaleph.security.service.SecResourceWebService;
import cn.sliew.scaleph.security.service.convert.SecResourceWebWithAuthorizeConvert;
import cn.sliew.scaleph.security.service.convert.SecRoleConvert;
import cn.sliew.scaleph.security.service.convert.SecUserConvert;
import cn.sliew.scaleph.security.service.dto.*;
import cn.sliew.scaleph.security.service.param.*;
import cn.sliew.scaleph.security.util.SecurityUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecAuthorizeServiceImpl implements SecAuthorizeService {

    @Autowired
    private SecResourceWebService secResourceWebService;
    @Autowired
    private SecResourceWebRoleMapper secResourceWebRoleMapper;
    @Autowired
    private SecUserRoleMapper secUserRoleMapper;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public List<UmiRoute> getWebRoute() {
        UserDetailInfo userDetails = (UserDetailInfo) userDetailsService.loadUserByUsername(SecurityUtil.getCurrentUserName());
        return buildRouteByPid(0L, userDetails.getUser().getId());
    }

    private List<UmiRoute> buildRouteByPid(Long pid, Long userId) {
        List<SecResourceWebDTO> secResourceWebDTOS = secResourceWebService.listByPidAndUserId(pid, userId, null);
        List<UmiRoute> routes = new ArrayList<>(secResourceWebDTOS.size());
        for (SecResourceWebDTO secResourceWebDTO : secResourceWebDTOS) {
            UmiRoute route = new UmiRoute();
            route.setName(secResourceWebDTO.getMenuName());
            route.setPath(secResourceWebDTO.getPath());
            route.setRedirect(secResourceWebDTO.getRedirect());
            route.setIcon(secResourceWebDTO.getIcon());
            route.setComponent(secResourceWebDTO.getComponent());
            List<UmiRoute> childRoutes = buildRouteByPid(secResourceWebDTO.getId(), userId);
            if (CollectionUtils.isEmpty(childRoutes) == false) {
                route.setRoutes(childRoutes);
            }
            routes.add(route);
        }
        return routes;
    }

    @Override
    public Page<SecRoleDTO> listAuthorizedRolesByResourceWebId(SecRoleListByResourceWebParam param) {
        Page page = new Page(param.getCurrent(), param.getPageSize());
        Page<SecRole> secRolePage = secResourceWebRoleMapper.selectRelatedRolesByWebResource(page, param.getResourceWebId(), param.getStatus(), param.getName());
        Page<SecRoleDTO> result = new Page<>(secRolePage.getCurrent(), secRolePage.getSize(), secRolePage.getTotal());
        List<SecRoleDTO> secRoleDTOS = SecRoleConvert.INSTANCE.toDto(secRolePage.getRecords());
        result.setRecords(secRoleDTOS);
        return result;
    }

    @Override
    public Page<SecRoleDTO> listUnauthorizedRolesByResourceWebId(SecRoleListByResourceWebParam param) {
        Page page = new Page(param.getCurrent(), param.getPageSize());
        Page<SecRole> secRolePage = secResourceWebRoleMapper.selectUnrelatedRolesByWebResource(page, param.getResourceWebId(), param.getStatus(), param.getName());
        Page<SecRoleDTO> result = new Page<>(secRolePage.getCurrent(), secRolePage.getSize(), secRolePage.getTotal());
        List<SecRoleDTO> secRoleDTOS = SecRoleConvert.INSTANCE.toDto(secRolePage.getRecords());
        result.setRecords(secRoleDTOS);
        return result;
    }

    @Override
    public void authorize(SecRoleBatchAuthorizeForResourceWebParam param) {
        for (Long roleId : param.getRoleIds()) {
            SecResourceWebRole record = new SecResourceWebRole();
            record.setResourceWebId(param.getResourceWebId());
            record.setRoleId(roleId);
            secResourceWebRoleMapper.insert(record);
        }
    }

    @Override
    public void unauthorize(SecRoleBatchAuthorizeForResourceWebParam param) {
        for (Long roleId : param.getRoleIds()) {
            LambdaQueryWrapper<SecResourceWebRole> queryWrapper = Wrappers.lambdaQuery(SecResourceWebRole.class)
                    .eq(SecResourceWebRole::getResourceWebId, param.getResourceWebId())
                    .eq(SecResourceWebRole::getRoleId, roleId);
            secResourceWebRoleMapper.delete(queryWrapper);
        }
    }

    @Override
    public List<SecResourceWebWithAuthorizeDTO> listResourceWebsByRoleId(SecResourceWebListByRoleParam param) {
        List<SecResourceWebVO> secResourceWebVOS = secResourceWebRoleMapper.selectAllResourceWebWithAuthorizeStatus(param.getRoleId(), 0L);
        List<SecResourceWebWithAuthorizeDTO> result = SecResourceWebWithAuthorizeConvert.INSTANCE.toDto(secResourceWebVOS);
        result.forEach(dto -> recurse(param.getRoleId(), dto));
        return result;
    }

    private void recurse(Long roleId, SecResourceWebWithAuthorizeDTO resourceWebDTO) {
        List<SecResourceWebWithAuthorizeDTO> children = listResourceWebsByRoleIdAndPid(roleId, resourceWebDTO.getId());
        if (CollectionUtils.isEmpty(children) == false) {
            resourceWebDTO.setChildren(children);
            children.forEach(child -> recurse(roleId, child));
        }
    }

    private List<SecResourceWebWithAuthorizeDTO> listResourceWebsByRoleIdAndPid(Long roleId, Long pid) {
        List<SecResourceWebVO> secResourceWebVOS = secResourceWebRoleMapper.selectAllResourceWebWithAuthorizeStatus(roleId, pid);
        return SecResourceWebWithAuthorizeConvert.INSTANCE.toDto(secResourceWebVOS);
    }

    @Override
    public void authorize(SecResourceWebBatchAuthorizeForRoleParam param) {
        for (Long resourceWebId : param.getResourceWebIds()) {
            SecResourceWebRole record = new SecResourceWebRole();
            record.setResourceWebId(resourceWebId);
            record.setRoleId(param.getRoleId());
            secResourceWebRoleMapper.insert(record);
        }
    }

    @Override
    public void unauthorize(SecResourceWebBatchAuthorizeForRoleParam param) {
        for (Long resourceWebId : param.getResourceWebIds()) {
            LambdaQueryWrapper<SecResourceWebRole> queryWrapper = Wrappers.lambdaQuery(SecResourceWebRole.class)
                    .eq(SecResourceWebRole::getResourceWebId, resourceWebId)
                    .eq(SecResourceWebRole::getRoleId, param.getRoleId());
            secResourceWebRoleMapper.delete(queryWrapper);
        }
    }

    @Override
    public Page<SecUserDTO> listAuthorizedUsersByRoleId(SecUserListByRoleParam param) {
        Page page = new Page(param.getCurrent(), param.getPageSize());
        Page<SecUser> secUserPage = secUserRoleMapper.selectRelatedUsersByRole(page, param.getRoleId(), param.getStatus(), param.getUserName());
        Page<SecUserDTO> result = new Page<>(secUserPage.getCurrent(), secUserPage.getSize(), secUserPage.getTotal());
        List<SecUserDTO> secUserDTOS = SecUserConvert.INSTANCE.toDto(secUserPage.getRecords());
        result.setRecords(secUserDTOS);
        return result;
    }

    @Override
    public Page<SecUserDTO> listUnauthorizedUsersByRoleId(SecUserListByRoleParam param) {
        Page page = new Page(param.getCurrent(), param.getPageSize());
        Page<SecUser> secUserPage = secUserRoleMapper.selectUnrelatedUsersByRole(page, param.getRoleId(), param.getStatus(), param.getUserName());
        Page<SecUserDTO> result = new Page<>(secUserPage.getCurrent(), secUserPage.getSize(), secUserPage.getTotal());
        List<SecUserDTO> secUserDTOS = SecUserConvert.INSTANCE.toDto(secUserPage.getRecords());
        result.setRecords(secUserDTOS);
        return result;
    }

    @Override
    public void authorize(SecUserBatchAuthorizeForRoleParam param) {
        for (Long userId : param.getUserIds()) {
            SecUserRole record = new SecUserRole();
            record.setUserId(userId);
            record.setRoleId(param.getRoleId());
            secUserRoleMapper.insert(record);
        }
    }

    @Override
    public void unauthorize(SecUserBatchAuthorizeForRoleParam param) {
        for (Long userId : param.getUserIds()) {
            LambdaQueryWrapper<SecUserRole> queryWrapper = Wrappers.lambdaQuery(SecUserRole.class)
                    .eq(SecUserRole::getUserId, userId)
                    .eq(SecUserRole::getRoleId, param.getRoleId());
            secUserRoleMapper.delete(queryWrapper);
        }
    }

    @Override
    public List<SecRoleDTO> listAuthorizedRolesByUserId(SecRoleListByUserParam param) {
        List<SecRole> secRoleList = secUserRoleMapper.selectRelatedRolesByUser(param.getUserId(), param.getStatus(), param.getName());
        return SecRoleConvert.INSTANCE.toDto(secRoleList);
    }

    @Override
    public List<SecRoleDTO> listUnauthorizedRolesByUserId(SecRoleListByUserParam param) {
        List<SecRole> secRoleList = secUserRoleMapper.selectUnrelatedRolesByUser(param.getUserId(), param.getStatus(), param.getName());
        return SecRoleConvert.INSTANCE.toDto(secRoleList);
    }

    @Override
    public void authorize(SecRoleBatchAuthorizeForUserParam param) {
        for (Long roleId : param.getRoleIds()) {
            SecUserRole record = new SecUserRole();
            record.setUserId(param.getUserId());
            record.setRoleId(roleId);
            secUserRoleMapper.insert(record);
        }
    }

    @Override
    public void unauthorize(SecRoleBatchAuthorizeForUserParam param) {
        for (Long roleId : param.getRoleIds()) {
            LambdaQueryWrapper<SecUserRole> queryWrapper = Wrappers.lambdaQuery(SecUserRole.class)
                    .eq(SecUserRole::getUserId, param.getUserId())
                    .eq(SecUserRole::getRoleId, roleId);
            secUserRoleMapper.delete(queryWrapper);
        }
    }
}
