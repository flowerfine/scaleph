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
    @Override
    public List<UmiRoute> getWebRoute() {
        return null;
    }

    @Override
    public Page<SecRoleDTO> listAuthorizedRolesByResourceWebId(SecRoleListByResourceWebParam param) {
        return null;
    }

    @Override
    public Page<SecRoleDTO> listUnauthorizedRolesByResourceWebId(SecRoleListByResourceWebParam param) {
        return null;
    }

    @Override
    public void authorize(SecRoleBatchAuthorizeForResourceWebParam param) {

    }

    @Override
    public void unauthorize(SecRoleBatchAuthorizeForResourceWebParam param) {

    }

    @Override
    public List<SecResourceWebWithAuthorizeDTO> listResourceWebsByRoleId(SecResourceWebListByRoleParam param) {
        return null;
    }

    @Override
    public void authorize(SecResourceWebBatchAuthorizeForRoleParam param) {

    }

    @Override
    public void unauthorize(SecResourceWebBatchAuthorizeForRoleParam param) {

    }

    @Override
    public Page<SecUserDTO> listAuthorizedUsersByRoleId(SecUserListByRoleParam param) {
        return null;
    }

    @Override
    public Page<SecUserDTO> listUnauthorizedUsersByRoleId(SecUserListByRoleParam param) {
        return null;
    }

    @Override
    public void authorize(SecUserBatchAuthorizeForRoleParam param) {

    }

    @Override
    public void unauthorize(SecUserBatchAuthorizeForRoleParam param) {

    }

    @Override
    public List<SecRoleDTO> listAuthorizedRolesByUserId(SecRoleListByUserParam param) {
        return null;
    }

    @Override
    public List<SecRoleDTO> listUnauthorizedRolesByUserId(SecRoleListByUserParam param) {
        return null;
    }

    @Override
    public void authorize(SecRoleBatchAuthorizeForUserParam param) {

    }

    @Override
    public void unauthorize(SecRoleBatchAuthorizeForUserParam param) {

    }
}
