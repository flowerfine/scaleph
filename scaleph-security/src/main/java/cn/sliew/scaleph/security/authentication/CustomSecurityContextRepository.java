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

package cn.sliew.scaleph.security.authentication;

import cn.sliew.scaleph.security.web.OnlineUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import java.util.Optional;

/**
 * https://docs.spring.io/spring-security/reference/servlet/authentication/persistence.html
 * @see OnlineUserService
 */
public class CustomSecurityContextRepository implements SecurityContextRepository {

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        return getContext(requestResponseHolder.getRequest()).orElse(null);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = context.getAuthentication();
        // 存储到 session 中
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return getContext(request).isPresent();
    }

    private Optional<SecurityContext> getContext(HttpServletRequest request) {
        // 读取 token，从 redis 中获取授权信息
        return Optional.empty();
    }
}
