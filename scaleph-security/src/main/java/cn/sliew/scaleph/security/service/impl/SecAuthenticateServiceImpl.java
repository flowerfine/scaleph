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

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.sliew.scaleph.cache.util.RedisUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.enums.ErrorShowTypeEnum;
import cn.sliew.scaleph.common.enums.ResponseCodeEnum;
import cn.sliew.scaleph.common.util.I18nUtil;
import cn.sliew.scaleph.security.authentication.UserDetailInfo;
import cn.sliew.scaleph.security.service.SecAuthenticateService;
import cn.sliew.scaleph.security.service.SecUserService;
import cn.sliew.scaleph.security.service.dto.SecCaptchaDTO;
import cn.sliew.scaleph.security.service.dto.SecRoleDTO;
import cn.sliew.scaleph.security.service.param.SecLoginParam;
import cn.sliew.scaleph.security.util.SecurityUtil;
import cn.sliew.scaleph.security.util.WebUtil;
import cn.sliew.scaleph.security.web.OnlineUserService;
import cn.sliew.scaleph.security.web.TokenProvider;
import cn.sliew.scaleph.system.model.ResponseVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SecAuthenticateServiceImpl implements SecAuthenticateService {


    @Override
    public SecCaptchaDTO getCaptcha() {
        return null;
    }

    @Override
    public boolean verityCaptcha(String uuid, String authCode) {
        return false;
    }

    @Override
    public ResponseVO login(HttpServletRequest request, SecLoginParam param) {
        return null;
    }

    @Override
    public void logout(String token) {

    }
}
