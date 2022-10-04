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

import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.security.util.SecurityUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gleiyu
 */
@Service(value = "svs")
public class SecurityValidateService {

    /**
     * 检查用户角色权限，有权限则放行。
     * 如果是SUPER ADMIN角色则直接放行
     *
     * @param privileges 权限标识字符串
     * @return true/false
     */
    public boolean validate(String... privileges) {
        UserDetails userDetails = SecurityUtil.getCurrentUser();
        if (userDetails == null) {
            return false;
        }

        List<String> privilegeList = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
        return privilegeList.contains(Constants.ROLE_SYS_ADMIN) ||
                Arrays.stream(privileges).anyMatch(privilegeList::contains);
    }
}
