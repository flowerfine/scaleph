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

package cn.sliew.scaleph.security.util;

import cn.sliew.scaleph.security.authentication.UserDetailInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

/**
 * @author gleiyu
 */
public enum SecurityUtil {
    ;

    /**
     * 从线程中获取登录的用户信息
     *
     * @return UserDetails
     */
    public static UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication) && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 获取当前登录的用户名称
     *
     * @return username
     */
    public static String getCurrentUserName() {
        UserDetails currentUser = getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUsername();
        }
        return null;
    }

    /**
     * 获取当前登录的用户名称
     *
     * @return username
     * @see cn.sliew.scaleph.security.web.TokenFilter
     */
    public static Optional<Long> getCurrentUserId() {
        UserDetailInfo userDetailInfo = (UserDetailInfo) SecurityUtil.getCurrentUser();
        if (userDetailInfo != null) {
            return Optional.of(userDetailInfo.getUser().getId());
        }
        return Optional.empty();
    }
}
