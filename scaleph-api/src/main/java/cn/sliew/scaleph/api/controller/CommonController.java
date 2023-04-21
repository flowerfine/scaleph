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

package cn.sliew.scaleph.api.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.RandomUtil;
import cn.sliew.scaleph.api.annotation.AnonymousAccess;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.cache.util.RedisUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.system.snowflake.UidGenerator;
import cn.sliew.scaleph.system.snowflake.exception.UidGenerateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author gleiyu
 */
@Controller
@RequestMapping("/api")
@Api(tags = "公共模块")
public class CommonController {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UidGenerator defaultUidGenerator;

    /**
     * 生成验证码
     *
     * @param req  request
     * @param resp response
     */
    @AnonymousAccess
    @ApiOperation(value = "查询验证码", notes = "查询验证码信息")
    @GetMapping(path = {"/authCode"})
    public ResponseEntity<Object> authCode(HttpServletRequest req, HttpServletResponse resp) {
        LineCaptcha lineCaptcha =
                CaptchaUtil.createLineCaptcha(150, 32, 5, RandomUtil.randomInt(6, 10));
        Font font = new Font("Stencil", Font.BOLD + Font.ITALIC, 20);
        lineCaptcha.setFont(font);
        lineCaptcha.setBackground(new Color(246, 250, 254));
        lineCaptcha.createCode();
        String uuid = Constants.AUTH_CODE_KEY + UUID.randomUUID().toString();
        redisUtil.set(uuid, lineCaptcha.getCode(), 10 * 60);
        Map<String, Object> map = new HashMap<>(2);
        map.put("uuid", uuid);
        map.put("img", lineCaptcha.getImageBase64Data());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/uid/snowflake")
    @ApiOperation(value = "获取 snowflake uid", notes = "获取 snowflake uid")
    public ResponseEntity<Long> getSnowflakeUid() throws UidGenerateException {
        return new ResponseEntity<>(defaultUidGenerator.getUID(), HttpStatus.OK);
    }

}
