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

package cn.sliew.scaleph.api.controller.resource;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.common.util.I18nUtil;
import cn.sliew.scaleph.security.util.SecurityUtil;
import cn.sliew.scaleph.storage.service.FileSystemService;
import cn.sliew.scaleph.system.model.ResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

@Tag(name = "资源管理-文件管理")
@RestController
@RequestMapping(path = "/api/resource/file")
public class FileController {

    @Autowired
    private FileSystemService fileSystemService;

    @Logging
    @GetMapping(path = "download")
    @Operation(summary = "下载", description = "从公共目录下载文件")
    public ResponseEntity<ResponseVO> download(@RequestParam("path") String path, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(SecurityUtil.getCurrentUserName())) {
            return new ResponseEntity<>(
                    ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED),
                            I18nUtil.get("response.error.unauthorized")), HttpStatus.OK);
        }

        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(path, "UTF-8"));
        try (InputStream inputStream = fileSystemService.get(path);
             ServletOutputStream outputStream = response.getOutputStream()) {
            FileCopyUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }
}
