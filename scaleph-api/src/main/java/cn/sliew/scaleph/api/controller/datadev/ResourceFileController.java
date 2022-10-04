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

package cn.sliew.scaleph.api.controller.datadev;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.system.vo.ResponseVO;
import cn.sliew.scaleph.core.di.service.DiResourceFileService;
import cn.sliew.scaleph.core.di.service.dto.DiResourceFileDTO;
import cn.sliew.scaleph.core.di.service.param.DiResourceFileParam;
import cn.sliew.scaleph.storage.service.FileSystemService;
import cn.sliew.scaleph.system.service.vo.DictVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "数据开发-资源管理")
@RestController
@RequestMapping(path = {"/api/datadev/resource", "/api/di/resource"})
public class ResourceFileController {

    @Autowired
    private DiResourceFileService diResourceFileService;

    @Autowired
    private FileSystemService fileSystemService;

    @Logging
    @GetMapping
    @ApiOperation(value = "分页查询资源列表", notes = "分页查询资源列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_SELECT)")
    public ResponseEntity<Page<DiResourceFileDTO>> listResource(DiResourceFileParam param) {
        Page<DiResourceFileDTO> page = this.diResourceFileService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/project")
    @ApiOperation(value = "查询项目下资源", notes = "查询项目下资源列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_SELECT)")
    public ResponseEntity<List<DictVO>> listByProject(@NotNull Long projectId) {
        List<DictVO> list = this.diResourceFileService.listByProjectId(projectId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @Logging
    @PostMapping
    @ApiOperation(value = "新增资源", notes = "新增资源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_ADD)")
    public ResponseEntity<ResponseVO> addResource(@NotNull @RequestBody DiResourceFileDTO fileDTO)
            throws IOException {
        DiResourceFileDTO dto = new DiResourceFileDTO();
        dto.setProjectId(fileDTO.getProjectId());
        dto.setFileName(fileDTO.getFileName());
        dto.setFilePath(String.valueOf(fileDTO.getProjectId()));
        dto.resolveFileType(fileDTO.getFileName());


        Long fileSize = fileSystemService.getFileSize(dto.getFilePath() + "/" + dto.getFileName());
//        Long fileSize = this.storageService.getFileSize(dto.getFilePath(), dto.getFileName());
        dto.setFileSize(fileSize);
        this.diResourceFileService.insert(dto);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    @Logging
    @PostMapping(path = "/upload")
    @ApiOperation(value = "上传资源文件", notes = "上传资源文件")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_ADD)")
    public ResponseEntity<ResponseVO> uploadResource(@NotNull String projectId,
                                                     @RequestParam("file") MultipartFile file) throws IOException {
        try (final InputStream inputStream = file.getInputStream()) {
            fileSystemService.upload(inputStream, projectId + "/" + file.getOriginalFilename());
            return new ResponseEntity<>(ResponseVO.sucess(file.getOriginalFilename()),
                    HttpStatus.CREATED);
        }
    }

    @Logging
    @DeleteMapping(path = "/upload")
    @ApiOperation(value = "删除资源文件", notes = "删除资源文件")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_ADD)")
    public ResponseEntity<ResponseVO> deleteResource(@NotNull String projectId,
                                                     @NotNull String fileName) throws IOException {
        fileSystemService.delete(projectId + "/" + fileName);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    @Logging
    @GetMapping(path = "/download")
    @ApiOperation(value = "下载资源文件", notes = "下载资源文件")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_DOWNLOAD)")
    public void downloadResource(@NotNull String projectId,
                                 @NotNull String fileName,
                                 HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        try (final InputStream inputStream = fileSystemService.get(projectId + "/" + fileName);
             final ServletOutputStream outputStream = response.getOutputStream()) {
            FileCopyUtils.copy(inputStream, outputStream);
        }
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "删除资源", notes = "删除资源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_DELETE)")
    public ResponseEntity<ResponseVO> deleteResource(@PathVariable(value = "id") Long id) throws IOException {
        List<DiResourceFileDTO> list = diResourceFileService.listByIds(Collections.singletonList(id));
        diResourceFileService.deleteById(id);

        if (CollectionUtils.isEmpty(list) == false) {
            DiResourceFileDTO resource = list.get(0);
            fileSystemService.delete(resource.getProjectId() + "/" + resource.getFileName());
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/batch")
    @ApiOperation(value = "批量删除资源", notes = "批量删除资源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_DELETE)")
    public ResponseEntity<ResponseVO> deleteResource(@RequestBody Map<Integer, String> map) throws IOException {
        List<DiResourceFileDTO> list = diResourceFileService.listByIds(map.values());
        diResourceFileService.deleteBatch(map);
        for (DiResourceFileDTO resource : list) {
            fileSystemService.delete(resource.getProjectId() + "/" + resource.getFileName());
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
