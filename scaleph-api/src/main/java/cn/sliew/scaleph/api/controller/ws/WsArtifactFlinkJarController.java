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

package cn.sliew.scaleph.api.controller.ws;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.system.model.ResponseVO;
import cn.sliew.scaleph.workspace.flink.service.WsArtifactFlinkJarService;
import cn.sliew.scaleph.workspace.flink.service.dto.WsArtifactFlinkJarDTO;
import cn.sliew.scaleph.workspace.flink.service.param.*;
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

@Tag(name = "Artifact管理-Flink-Jar")
@RestController
@RequestMapping(path = "/api/artifact/flink/jar")
public class WsArtifactFlinkJarController {

    @Autowired
    private WsArtifactFlinkJarService wsArtifactFlinkJarService;

    @Logging
    @GetMapping
    @Operation(summary = "查询 flink jar 列表", description = "查询 flink jar 列表")
    public ResponseEntity<Page<WsArtifactFlinkJarDTO>> list(@Valid WsArtifactFlinkJarListParam param) {
        Page<WsArtifactFlinkJarDTO> result = wsArtifactFlinkJarService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/history")
    @Operation(summary = "根据 artifact 分页查询 flink jar 列表", description = "根据 artifact 分页查询 flink jar 列表")
    public ResponseEntity<Page<WsArtifactFlinkJarDTO>> listByArtifact(@Valid WsArtifactFlinkJarArtifactParam param) {
        Page<WsArtifactFlinkJarDTO> result = wsArtifactFlinkJarService.listByArtifact(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/all")
    @Operation(summary = "查询 flink jar 列表", description = "查询 flink jar 列表")
    public ResponseEntity<ResponseVO<List<WsArtifactFlinkJarDTO>>> listAll(@Valid WsArtifactFlinkJarSelectListParam param) {
        List<WsArtifactFlinkJarDTO> result = wsArtifactFlinkJarService.listAll(param);
        return new ResponseEntity<>(ResponseVO.success(result), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @Operation(summary = "查询 flink jar 详情", description = "查询 flink jar 详情")
    public ResponseEntity<WsArtifactFlinkJarDTO> selectOne(@PathVariable("id") Long id) {
        WsArtifactFlinkJarDTO result = wsArtifactFlinkJarService.selectOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "上传 flink jar", description = "上传 flink jar")
    public ResponseEntity<ResponseVO> upload(@Valid WsArtifactFlinkJarUploadParam param, @RequestPart("file") MultipartFile file) throws IOException {
        wsArtifactFlinkJarService.upload(param, file);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("jar")
    @Operation(summary = "修改 flink jar", description = "修改 flink jar")
    public ResponseEntity<ResponseVO> updateJar(@Valid WsArtifactFlinkJarUpdateParam param, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        wsArtifactFlinkJarService.update(param, file);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/download/{id}")
    @Operation(summary = "下载 artifact jar", description = "下载 artifact jar")
    public ResponseEntity<ResponseVO> download(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            String fileName = wsArtifactFlinkJarService.download(id, outputStream);
            response.setCharacterEncoding("utf-8");// 设置字符编码
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8")); // 设置响应头
        }
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/{id}")
    @Operation(summary = "删除 flink jar", description = "删除 flink jar")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) throws IOException {
        wsArtifactFlinkJarService.deleteOne(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/artifact/{artifactId}")
    @Operation(summary = "删除 artifact 下所有 flink jar", description = "删除 artifact 下所有 flink jar")
    public ResponseEntity<ResponseVO> deleteArtifact(@PathVariable("artifactId") Long artifactId) throws IOException {
        wsArtifactFlinkJarService.deleteArtifact(artifactId);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }
}
