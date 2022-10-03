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
import cn.sliew.scaleph.api.vo.ResponseVO;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.resource.service.KerberosService;
import cn.sliew.scaleph.resource.service.dto.KerberosDTO;
import cn.sliew.scaleph.resource.service.param.KerberosListParam;
import cn.sliew.scaleph.resource.service.param.KerberosUploadParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

@Api(tags = "资源管理-jar")
@RestController
@RequestMapping(path = "/api/resource/kerberos")
public class KerberosController {

    @Autowired
    private KerberosService kerberosService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询 kerberos 列表", notes = "查询 kerberos 列表")
    public ResponseEntity<Page<KerberosDTO>> list(@Valid KerberosListParam param) throws IOException {
        final Page<KerberosDTO> kerberosDTOS = kerberosService.list(param);
        return new ResponseEntity<>(kerberosDTOS, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @ApiOperation(value = "查询 kerberos 详情", notes = "查询 kerberos 详情")
    public ResponseEntity<KerberosDTO> get(@PathVariable("id") Long id) throws IOException {
        final KerberosDTO result = kerberosService.selectOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 支持文件上传和表单一起提交，如果是多个文件时，可以使用 {@code @RequestParam("files") MultipartFile[] files}
     */
    @Logging
    @PostMapping("upload")
    @ApiOperation(value = "上传 kerberos", notes = "上传 kerberos")
    public ResponseEntity<ResponseVO> upload(@Valid KerberosUploadParam param, @RequestPart("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new ScalephException("缺少文件");
        }
        kerberosService.upload(param, file);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("download/{id}")
    @ApiOperation("下载 kerberos")
    public ResponseEntity<ResponseVO> download(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            final String name = kerberosService.download(id, outputStream);
            response.setCharacterEncoding("utf-8");// 设置字符编码
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8")); // 设置响应头
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除 kerberos", notes = "删除 kerberos")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) throws IOException {
        kerberosService.delete(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @ApiOperation(value = "批量删除 kerberos", notes = "批量删除 kerberos")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) throws IOException {
        kerberosService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

}
