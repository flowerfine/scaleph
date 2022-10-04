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
import cn.sliew.scaleph.system.vo.ResponseVO;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.resource.service.JarService;
import cn.sliew.scaleph.resource.service.dto.JarDTO;
import cn.sliew.scaleph.resource.service.param.JarListParam;
import cn.sliew.scaleph.resource.service.param.JarUploadParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@Api(tags = "资源管理-jar")
@RestController
@RequestMapping(path = "/api/resource/jar")
public class JarController {

    @Autowired
    private JarService jarService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询 jar 列表", notes = "查询 jar 列表")
    public ResponseEntity<Page<JarDTO>> list(@Valid JarListParam param) throws IOException {
        final Page<JarDTO> jarDTOS = jarService.list(param);
        return new ResponseEntity<>(jarDTOS, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @ApiOperation(value = "查询 jar 详情", notes = "查询 jar 详情")
    public ResponseEntity<JarDTO> get(@PathVariable("id") Long id) throws IOException {
        final JarDTO result = jarService.selectOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 支持文件上传和表单一起提交，如果是多个文件时，可以使用 {@code @RequestParam("files") MultipartFile[] files}
     */
    @Logging
    @PostMapping("upload")
    @ApiOperation(value = "上传 jar", notes = "上传 jar")
    public ResponseEntity<ResponseVO> upload(@Valid JarUploadParam param, @RequestPart("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new ScalephException("缺少文件");
        }
        jarService.upload(param, file);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("download/{id}")
    @ApiOperation("下载 jar")
    public ResponseEntity<ResponseVO> download(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            final String name = jarService.download(id, outputStream);
            response.setCharacterEncoding("utf-8");// 设置字符编码
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8")); // 设置响应头
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除 jar", notes = "删除 jar")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) throws IOException {
        jarService.delete(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @ApiOperation(value = "批量删除 jar", notes = "批量删除 jar")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) throws IOException {
        jarService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
