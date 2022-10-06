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

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.api.annotation.AnonymousAccess;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.security.util.SecurityUtil;
import cn.sliew.scaleph.system.vo.ResponseVO;
import cn.sliew.scaleph.common.constant.DictConstants;
import cn.sliew.scaleph.common.enums.BoolEnum;
import cn.sliew.scaleph.log.service.LogMessageService;
import cn.sliew.scaleph.log.service.dto.LogMessageDTO;
import cn.sliew.scaleph.log.service.param.LogMessageParam;
import cn.sliew.scaleph.system.service.vo.DictVO;
import cn.sliew.scaleph.system.util.I18nUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 站内信表 前端控制器
 * </p>
 *
 * @author liyu
 */
@RestController
@RequestMapping("/api/msg")
@Api(tags = "系统管理-消息管理")
public class LogMessageController {

    @Autowired
    private LogMessageService logMessageService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询用户的消息信息", notes = "用户登录后查询自己的消息列表")
    public ResponseEntity<Page<LogMessageDTO>> listMessage(LogMessageParam param) {
        String userName = SecurityUtil.getCurrentUserName();
        if (!StrUtil.isEmpty(userName)) {
            param.setReceiver(userName);
            Page<LogMessageDTO> page = this.logMessageService.listByPage(param);
            return new ResponseEntity<>(page, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "更新消息读取状态", notes = "更新指定消息为已读状态")
    public ResponseEntity<ResponseVO> readMessage(@RequestBody LogMessageDTO message) {
        String userName = SecurityUtil.getCurrentUserName();
        if (!Strings.isNullOrEmpty(userName)) {
            message.setIsRead(DictVO.toVO(DictConstants.YES_NO, BoolEnum.YES.getValue()));
            this.logMessageService.update(message);
            return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED),
                            I18nUtil.get("response.error.unauthorized")), HttpStatus.OK);
        }
    }

    @Logging
    @GetMapping(path = "/readAll")
    @ApiOperation(value = "全部标记已读", notes = "更新用户全部未读消息为已读")
    public ResponseEntity<ResponseVO> readAllMessage() {
        String userName = SecurityUtil.getCurrentUserName();
        if (!Strings.isNullOrEmpty(userName)) {
            this.logMessageService.readAll(userName);
            return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED),
                            I18nUtil.get("response.error.unauthorized")), HttpStatus.OK);
        }
    }

    @Logging
    @GetMapping(path = "/count")
    @AnonymousAccess
    @ApiOperation(value = "查询用户的未读消息数量", notes = "用户登录后查询自己的未读消息数量")
    public ResponseEntity<Long> countUnReadMessage() {
        String userName = SecurityUtil.getCurrentUserName();
        if (!Strings.isNullOrEmpty(userName)) {
            Long result = this.logMessageService.countUnReadMsg(userName);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(0L, HttpStatus.OK);
        }
    }

}

