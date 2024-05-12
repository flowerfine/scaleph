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
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.resource.service.SeaTunnelReleaseService;
import cn.sliew.scaleph.resource.service.dto.SeaTunnelReleaseDTO;
import cn.sliew.scaleph.resource.service.param.SeaTunnelConnectorUploadParam;
import cn.sliew.scaleph.resource.service.param.SeaTunnelReleaseListParam;
import cn.sliew.scaleph.resource.service.param.SeaTunnelReleaseUploadParam;
import cn.sliew.scaleph.resource.service.vo.FileStatusVO;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Tag(name = "资源管理-seatunnel-release")
@RestController
@RequestMapping(path = "/api/resource/seatunnel-release")
public class SeaTunnelReleaseController {

    @Autowired
    private SeaTunnelReleaseService seaTunnelReleaseService;

    @Logging
    @GetMapping
    @Operation(summary = "查询 release 列表", description = "查询 release 列表")
    public ResponseEntity<Page<SeaTunnelReleaseDTO>> list(@Valid SeaTunnelReleaseListParam param) throws IOException {
        final Page<SeaTunnelReleaseDTO> releaseSeaTunnelDTOS = seaTunnelReleaseService.list(param);
        return new ResponseEntity<>(releaseSeaTunnelDTOS, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @Operation(summary = "查询 release 详情", description = "查询 release 详情")
    public ResponseEntity<SeaTunnelReleaseDTO> get(@PathVariable("id") Long id) throws IOException {
        final SeaTunnelReleaseDTO result = seaTunnelReleaseService.selectOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}/connectors")
    @Operation(summary = "查询 release connectors", description = "查询 release connectors")
    public ResponseEntity<List<FileStatusVO>> listConnectors(@PathVariable("id") Long id) throws IOException {
        final List<FileStatusVO> result = seaTunnelReleaseService.listConnectors(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 支持文件上传和表单一起提交，如果是多个文件时，可以使用 {@code @RequestParam("files") MultipartFile[] files}
     */
    @Logging
    @PostMapping("upload")
    @Operation(summary = "上传 release", description = "上传 release")
    public ResponseEntity<ResponseVO<SeaTunnelReleaseDTO>> upload(@Valid SeaTunnelReleaseUploadParam param, @RequestPart("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new ScalephException("缺少文件");
        }
        SeaTunnelReleaseDTO result = seaTunnelReleaseService.upload(param, file);
        return new ResponseEntity<>(ResponseVO.success(result), HttpStatus.OK);
    }

    @Logging
    @PostMapping("uploadConnector")
    @Operation(summary = "上传 connector", description = "上传 connector")
    public ResponseEntity<ResponseVO> uploadConnector(@Valid SeaTunnelConnectorUploadParam param, @RequestPart("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new ScalephException("缺少文件");
        }
        seaTunnelReleaseService.uploadConnector(param, file);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("fetch")
    @Operation(summary = "自动获取 connector", description = "自动获取 connector")
    public ResponseEntity<ResponseVO> fetchConnectors(@RequestParam("id") Long id) throws Exception {
        seaTunnelReleaseService.fetchConnectors(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("download/{id}")
    @Operation(summary = "下载 release")
    public ResponseEntity<ResponseVO> download(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            final String name = seaTunnelReleaseService.download(id, outputStream);
            response.setCharacterEncoding("utf-8");// 设置字符编码
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8")); // 设置响应头
        }
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("download/{id}/connectors/{connector}")
    @Operation(summary = "下载 release connector")
    public ResponseEntity<ResponseVO> downloadConnector(@PathVariable("id") Long id, @PathVariable("connector") String connector, HttpServletResponse response) throws IOException {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            String name = seaTunnelReleaseService.downloadConnector(id, connector, outputStream);
            response.setCharacterEncoding("utf-8");// 设置字符编码
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8")); // 设置响应头
        }
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @Operation(summary = "删除 release", description = "删除 release")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) throws IOException {
        seaTunnelReleaseService.delete(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @Operation(summary = "批量删除 release", description = "批量删除 release")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) throws IOException {
        seaTunnelReleaseService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }
}
