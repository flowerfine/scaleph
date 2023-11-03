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

import cn.sliew.scaleph.api.annotation.AnonymousAccess;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.security.service.SecAuthenticateService;
import cn.sliew.scaleph.security.service.dto.SecCaptchaDTO;
import cn.sliew.scaleph.system.snowflake.UidGenerator;
import cn.sliew.scaleph.system.snowflake.exception.UidGenerateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gleiyu
 */
@Controller
@RequestMapping("/api")
@Tag(name = "公共模块")
public class CommonController {

    @Autowired
    private UidGenerator defaultUidGenerator;
    @Autowired
    private SecAuthenticateService secAuthenticateService;

    /**
     * 生成验证码
     */
    @AnonymousAccess
    @Operation(summary = "查询验证码", description = "查询验证码信息")
    @GetMapping(path = {"/authCode"})
    public ResponseEntity<Object> authCode() {
        SecCaptchaDTO captcha = secAuthenticateService.getCaptcha();
        return new ResponseEntity<>(captcha, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/uid/snowflake")
    @Operation(summary = "获取 snowflake uid", description = "获取 snowflake uid")
    public ResponseEntity<Long> getSnowflakeUid() throws UidGenerateException {
        return new ResponseEntity<>(defaultUidGenerator.getUID(), HttpStatus.OK);
    }

}
