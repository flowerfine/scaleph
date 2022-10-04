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

package cn.sliew.scaleph.api.controller.admin;

import cn.hutool.core.date.DateUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.log.service.LogLoginService;
import cn.sliew.scaleph.log.service.dto.LogLoginDTO;
import cn.sliew.scaleph.log.service.param.LogLoginParam;
import cn.sliew.scaleph.security.util.SecurityUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>
 * 用户操作日志 前端控制器
 * </p>
 *
 * @author liyu
 */
@RestController
@RequestMapping("/api/admin/log")
@Api(tags = "系统管理-日志管理")
public class LogController {

    @Autowired
    private LogLoginService logLoginService;

    @Logging
    @GetMapping(path = "login")
    @ApiOperation(value = "查询用户近30天的登录日志", notes = "查询用户近30天的登录日志")
    public ResponseEntity<Page<LogLoginDTO>> listLoginLogNearlyOneMonth(LogLoginParam param) {
        String userName = SecurityUtil.getCurrentUserName();
        if (StringUtils.hasText(userName)) {
            param.setUserName(userName);
            param.setLoginTime(DateUtil.offsetDay(DateUtil.beginOfDay(new Date()), -30));
            Page<LogLoginDTO> result = this.logLoginService.listByPage(param);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}

