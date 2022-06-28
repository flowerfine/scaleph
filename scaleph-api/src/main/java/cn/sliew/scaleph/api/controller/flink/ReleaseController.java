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

package cn.sliew.scaleph.api.controller.flink;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.api.vo.ResponseVO;
import cn.sliew.scaleph.common.exception.CustomException;
import cn.sliew.scaleph.engine.flink.FlinkRelease;
import cn.sliew.scaleph.engine.flink.service.FlinkReleaseService;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkReleaseDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkReleaseListParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkReleaseLoadParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkReleaseUploadParam;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "Flink管理-release管理")
@RestController
@RequestMapping(path = "/api/flink/release")
public class ReleaseController {

    @Autowired
    private FlinkReleaseService flinkReleaseService;

    @Logging
    @GetMapping("versions")
    @ApiOperation(value = "查询 release 版本", notes = "查询 release 版本")
    public ResponseEntity<List<String>> versions() {
        final List<String> versions = Arrays.stream(FlinkRelease.values()).map(FlinkRelease::getVersion).collect(Collectors.toList());
        return new ResponseEntity<>(versions, HttpStatus.OK);
    }

    @Logging
    @GetMapping
    @ApiOperation(value = "查询 release 列表", notes = "查询 release 列表")
    public ResponseEntity<Page<FlinkReleaseDTO>> list(@Valid FlinkReleaseListParam param) throws IOException {
        final Page<FlinkReleaseDTO> flinkReleaseDTOS = flinkReleaseService.list(param);
        return new ResponseEntity<>(flinkReleaseDTOS, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @ApiOperation(value = "查询 release 详情", notes = "查询 release 详情")
    public ResponseEntity<FlinkReleaseDTO> get(@PathVariable("id") Long id) throws IOException {
        final FlinkReleaseDTO result = flinkReleaseService.selectOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PostMapping("load")
    @ApiOperation(value = "加载 release", notes = "加载 release")
    public ResponseEntity<ResponseVO> load(@Valid @RequestBody FlinkReleaseLoadParam param) throws IOException {
        flinkReleaseService.load(param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    /**
     * 支持文件上传和表单一起提交，如果是多个文件时，可以使用 {@code @RequestParam("files") MultipartFile[] files}
     */
    @Logging
    @PostMapping("upload")
    @ApiOperation(value = "上传 release", notes = "上传 release")
    public ResponseEntity<ResponseVO> upload(@Valid FlinkReleaseUploadParam param, @RequestPart("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new CustomException("缺少文件");
        }
        flinkReleaseService.upload(param, file);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("download/{id}")
    @ApiOperation("下载 release")
    public ResponseEntity<ResponseVO> download(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            final String name = flinkReleaseService.download(id, outputStream);
            response.setCharacterEncoding("utf-8");// 设置字符编码
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8")); // 设置响应头
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除 release", notes = "删除 release")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) throws IOException {
        flinkReleaseService.delete(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @ApiOperation(value = "批量删除 release", notes = "批量删除 release")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) throws IOException {
        flinkReleaseService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
