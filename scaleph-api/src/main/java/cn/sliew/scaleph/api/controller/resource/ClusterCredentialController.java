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
import cn.sliew.scaleph.resource.service.ClusterCredentialService;
import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import cn.sliew.scaleph.resource.service.param.ClusterCredentialListParam;
import cn.sliew.scaleph.resource.service.param.ClusterCredentialUploadParam;
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

@Tag(name = "资源管理-集群凭证")
@RestController
@RequestMapping(path = "/api/resource/cluster-credential")
public class ClusterCredentialController {

    @Autowired
    private ClusterCredentialService clusterCredentialService;

    @Logging
    @GetMapping
    @Operation(summary = "查询部署配置列表", description = "查询部署配置列表")
    public ResponseEntity<Page<ClusterCredentialDTO>> list(@Valid ClusterCredentialListParam param) {
        Page<ClusterCredentialDTO> result = clusterCredentialService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("{id}")
    @Operation(summary = "查询部署配置", description = "查询部署配置")
    public ResponseEntity<ClusterCredentialDTO> selectOne(@PathVariable("id") Long id) {
        ClusterCredentialDTO result = clusterCredentialService.selectOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PostMapping("upload")
    @Operation(summary = "上传部署配置", description = "上传部署配置")
    public ResponseEntity<ResponseVO> upload(@Valid ClusterCredentialUploadParam param, @RequestPart("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new ScalephException("缺少文件");
        }
        clusterCredentialService.upload(param, file);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("download/{id}")
    @Operation(summary = "下载部署配置")
    public ResponseEntity<ResponseVO> download(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            String name = clusterCredentialService.download(id, outputStream);
            response.setCharacterEncoding("utf-8");// 设置字符编码
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8")); // 设置响应头
        }
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @Operation(summary = "删除部署配置", description = "删除部署配置")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) throws IOException {
        clusterCredentialService.delete(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @Operation(summary = "批量删除部署配置", description = "批量删除部署配置")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) throws IOException {
        clusterCredentialService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }
}
