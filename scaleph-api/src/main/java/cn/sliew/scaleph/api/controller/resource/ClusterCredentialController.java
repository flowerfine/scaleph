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
import cn.sliew.scaleph.resource.service.ClusterCredentialService;
import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import cn.sliew.scaleph.resource.service.param.ClusterCredentialListParam;
import cn.sliew.scaleph.resource.service.vo.FileStatusVO;
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
@Api(tags = "资源管理-集群凭证")
@RestController
@RequestMapping(path = "/api/resource/cluster-credential")
public class ClusterCredentialController {

    @Autowired
    private ClusterCredentialService clusterCredentialService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询部署配置列表", notes = "查询部署配置列表")
    public ResponseEntity<Page<ClusterCredentialDTO>> list(@Valid ClusterCredentialListParam param) {
        final Page<ClusterCredentialDTO> result = clusterCredentialService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping({"{id}"})
    @ApiOperation(value = "查询部署配置", notes = "查询部署配置")
    public ResponseEntity<ClusterCredentialDTO> selectOne(@PathVariable("id") Long id) {
        final ClusterCredentialDTO result = clusterCredentialService.selectOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "新增部署配置", notes = "新增部署配置")
    public ResponseEntity<ResponseVO> addDeployConfig(@Valid @RequestBody ClusterCredentialDTO param) {
        clusterCredentialService.insert(param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "修改部署配置", notes = "修改部署配置")
    public ResponseEntity<ResponseVO> updateDeployConfig(@Valid @RequestBody ClusterCredentialDTO param) {
        clusterCredentialService.update(param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除部署配置", notes = "删除部署配置")
    public ResponseEntity<ResponseVO> deleteDeployConfig(@PathVariable("id") Long id) {
        clusterCredentialService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @ApiOperation(value = "批量删除部署配置", notes = "批量删除部署配置")
    public ResponseEntity<ResponseVO> deleteDeployConfig(@RequestBody List<Long> ids) {
        clusterCredentialService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("{id}/file")
    @ApiOperation(value = "查询部署配置文件列表", notes = "查询部署配置文件列表")
    public ResponseEntity<List<FileStatusVO>> listDeployConfigFile(@PathVariable("id") Long id) throws IOException {
        final List<FileStatusVO> fileStatuses = clusterCredentialService.listCredentialFile(id);
        return new ResponseEntity<>(fileStatuses, HttpStatus.OK);
    }

    /**
     * 支持文件上传和表单一起提交，如果是多个文件时，可以使用 {@code @RequestParam("files") MultipartFile[] files}
     */
    @Logging
    @PostMapping("{id}/file")
    @ApiOperation(value = "上传部署配置文件", notes = "上传部署配置文件，支持上传多个文件")
    public ResponseEntity<ResponseVO> uploadDeployConfigFile(@PathVariable("id") Long id, @RequestPart("files") MultipartFile[] files) throws IOException {
        clusterCredentialService.uploadCredentialFile(id, files);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("{id}/file/{fileName}")
    @ApiOperation(value = "下载部署配置文件", notes = "下载部署配置文件")
    public ResponseEntity<ResponseVO> downloadDeployConfigFile(
            @PathVariable("id") Long id,
            @PathVariable("fileName") String fileName,
            HttpServletResponse response) throws IOException {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            clusterCredentialService.downloadCredentialFile(id, fileName, outputStream);
            response.setCharacterEncoding("utf-8");// 设置字符编码
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8")); // 设置响应头
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}/file/{fileName}")
    @ApiOperation(value = "删除部署配置文件", notes = "删除部署配置文件")
    public ResponseEntity<ResponseVO> deleteDeployConfigFile(
            @PathVariable("id") Long id,
            @PathVariable("fileName") String fileName) throws IOException {
        clusterCredentialService.deleteCredentialFile(id, fileName);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}/file")
    @ApiOperation(value = "批量删除部署配置文件", notes = "删除部署配置文件")
    public ResponseEntity<ResponseVO> deleteDeployConfigFiles(
            @PathVariable("id") Long id,
            @RequestBody List<String> fileNames) throws IOException {
        clusterCredentialService.deleteCredentialFiles(id, fileNames);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
