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

import cn.sliew.scaleph.system.util.I18nUtil;
import cn.sliew.scaleph.system.vo.ResponseVO;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 未登录用户访问无权限资源时候返回未鉴权信息，前端跳转登录页面
 *
 * @author gleiyu
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse resp,
                         AuthenticationException authException) throws IOException {
        try (PrintWriter out = resp.getWriter()) {
            resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ResponseVO info = ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED),
                    I18nUtil.get("response.error.unauthorized"));
            out.write(info.toString());
            out.flush();
        }
    }
}
