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

import cn.sliew.scaleph.common.util.I18nUtil;
import cn.sliew.scaleph.security.util.WebUtil;
import cn.sliew.scaleph.system.model.ResponseVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 认证后的用户访问无权限资源时返回403提示
 *
 * @author gleiyu
 * @see ExceptionTranslationFilter
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        ResponseVO info = ResponseVO.error(
                String.valueOf(HttpServletResponse.SC_FORBIDDEN),
                I18nUtil.get("response.error.no.privilege"));
        WebUtil.renderJson(response, info);
    }
}
