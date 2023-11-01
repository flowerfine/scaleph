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
import cn.sliew.scaleph.security.web.OnlineUserService;
import cn.sliew.scaleph.security.web.TokenProvider;
import cn.sliew.scaleph.system.model.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SecAuthenticateServiceImpl implements SecAuthenticateService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private SecUserService secUserService;

    @Autowired
    private OnlineUserService onlineUserService;

    @Override
    public SecCaptchaDTO getCaptcha() {
        LineCaptcha lineCaptcha =
                CaptchaUtil.createLineCaptcha(150, 32, 5, RandomUtil.randomInt(6, 10));
        Font font = new Font("Stencil", Font.BOLD + Font.ITALIC, 20);
        lineCaptcha.setFont(font);
        lineCaptcha.setBackground(new Color(246, 250, 254));
        lineCaptcha.createCode();
        String uuid = Constants.AUTH_CODE_KEY + UUID.randomUUID();
        // 过期时间 10min
        redisUtil.set(uuid, lineCaptcha.getCode(), 10 * 60);
        SecCaptchaDTO dto = new SecCaptchaDTO();
        dto.setUuid(uuid);
        dto.setImg(lineCaptcha.getImageBase64Data());
        return dto;
    }

    @Override
    public boolean verityCaptcha(String uuid, String authCode) {
        String redisAuthCode = (String) redisUtil.get(uuid);
        redisUtil.delKeys(uuid);
        return StringUtils.hasText(authCode) && redisAuthCode.equalsIgnoreCase(authCode);
    }

    @Override
    public ResponseVO login(HttpServletRequest request, SecLoginParam param) {
        if (verityCaptcha(param.getUuid(), param.getAuthCode()) == false) {
            return ResponseVO.error(
                    ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.authCode"),
                    ErrorShowTypeEnum.ERROR_MESSAGE);
        }

        try {
            authenticate(param);
            final UserDetailInfo userInfo = (UserDetailInfo) SecurityUtil.getCurrentUser();
            userInfo.setLoginTime(new Date());
            userInfo.setLoginIpAddress(ServletUtil.getClientIP(request));
            userInfo.setRemember(param.getRemember());
            //查询用户权限信息，同时存储到redis onlineuser中
            List<SecRoleDTO> roles = secUserService.getAllPrivilegeByUserName(userInfo.getUsername());
            userInfo.getUser().setRoles(roles);
            //存储信息到redis中

            //生成token 使用uuid作为token
            String token = tokenProvider.createToken();
            onlineUserService.insert(userInfo, token);
            //验证成功返回token
            return ResponseVO.success(token);
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            return ResponseVO.error(
                    ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.login.password"),
                    ErrorShowTypeEnum.ERROR_MESSAGE);
        }
    }

    private void authenticate(SecLoginParam param) {
        //检查用户名密码
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(param.getUserName(), param.getPassword());
        //spring security框架调用userDetailsService获取用户信息并验证，验证通过后返回一个Authentication对象，存储到线程的SecurityContext中
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void logout(String token) {
        onlineUserService.logoutByToken(token);
    }
}
