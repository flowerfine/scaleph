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
import cn.sliew.scaleph.cache.util.RedisUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.security.service.SecAuthenticateService;
import cn.sliew.scaleph.security.service.dto.SecCaptchaDTO;
import cn.sliew.scaleph.security.service.param.SecCaptchaVerifyParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.UUID;

@Service
public class SecAuthenticateServiceImpl implements SecAuthenticateService {

    @Autowired
    private RedisUtil redisUtil;

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
    public String login(HttpServletRequest request, HttpServletResponse response, String username, String password, Boolean rememberMe) {

//        try {
//            //检查用户名密码
//            UsernamePasswordAuthenticationToken authenticationToken =
//                    new UsernamePasswordAuthenticationToken(loginUser.getUserName(),
//                            loginUser.getPassword());
//            //spring security框架调用userDetailsService获取用户信息并验证，验证通过后返回一个Authentication对象，存储到线程的SecurityContext中
//            Authentication authentication =
//                    authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            //生成token 使用uuid作为token
//            String token = tokenProvider.createToken();
//            final UserDetailInfo userInfo = (UserDetailInfo) authentication.getPrincipal();
//            userInfo.setLoginTime(new Date());
//            userInfo.setLoginIpAddress(ServletUtil.getClientIP(request));
//            userInfo.setRemember(loginUser.getRemember());
//            //查询用户权限信息，同时存储到redis onlineuser中
//            List<SecRoleDTO> roles = secUserService.getAllPrivilegeByUserName(userInfo.getUsername());
//            userInfo.getUser().setRoles(roles);
//            //存储信息到redis中
//            onlineUserService.insert(userInfo, token);
//            //启用 session
//            HttpSession session = request.getSession(true);
//            session.setAttribute(session.getId(), token);
//            //验证成功返回token
//            return new ResponseEntity<>(ResponseVO.success(token), HttpStatus.OK);
//        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
//            return new ResponseEntity<>(
//                    ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
//                            I18nUtil.get("response.error.login.password"),
//                            ErrorShowTypeEnum.ERROR_MESSAGE), HttpStatus.OK);
//        }
        return null;


    }

    @Override
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }
}
