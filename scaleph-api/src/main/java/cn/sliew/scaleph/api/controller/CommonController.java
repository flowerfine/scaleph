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
import cn.sliew.scaleph.security.util.SecurityUtil;
import cn.sliew.scaleph.system.vo.ResponseVO;
import cn.sliew.scaleph.cache.util.RedisUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.storage.service.FileSystemService;
import cn.sliew.scaleph.system.snowflake.UidGenerator;
import cn.sliew.scaleph.system.snowflake.exception.UidGenerateException;
import cn.sliew.scaleph.system.util.I18nUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
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
    private FileSystemService fileSystemService;
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
    @PostMapping(path = "/file/upload")
    @ApiOperation(value = "上传文件", notes = "上传文件到公共目录")
    public ResponseEntity<ResponseVO> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (StringUtils.isEmpty(SecurityUtil.getCurrentUserName())) {
            return new ResponseEntity<>(
                    ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED),
                            I18nUtil.get("response.error.unauthorized")), HttpStatus.OK);
        }

        try (final InputStream inputStream = file.getInputStream()) {
            fileSystemService.upload(inputStream, file.getName());
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/file/download")
    @ApiOperation(value = "下载文件", notes = "从公共目录下载文件")
    public ResponseEntity<ResponseVO> download(@NotNull String fileName,
                                               HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(SecurityUtil.getCurrentUserName())) {
            return new ResponseEntity<>(
                    ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED),
                            I18nUtil.get("response.error.unauthorized")), HttpStatus.OK);
        }

        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        try (final InputStream inputStream = fileSystemService.get(fileName);
             final ServletOutputStream outputStream = response.getOutputStream()) {
            FileCopyUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/file/delete")
    @ApiOperation(value = "删除文件", notes = "从公共目录删除文件")
    public ResponseEntity<ResponseVO> deleteFile(@NotNull String fileName) throws IOException {
        if (StringUtils.isEmpty(SecurityUtil.getCurrentUserName())) {
            return new ResponseEntity<>(
                    ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED),
                            I18nUtil.get("response.error.unauthorized")), HttpStatus.OK);
        }
        fileSystemService.delete(fileName);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/uid/snowflake")
    @ApiOperation(value = "获取 snowflake uid", notes = "获取 snowflake uid")
    public ResponseEntity<Long> getSnowflakeUid() throws UidGenerateException {
        return new ResponseEntity<>(defaultUidGenerator.getUID(), HttpStatus.OK);
    }

}
