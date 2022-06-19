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
import cn.sliew.scaleph.engine.flink.service.FlinkArtifactService;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkArtifactDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkArtifactListParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkArtifactUploadParam;
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

@Slf4j
@Api(tags = "Flink管理-artifact管理")
@RestController
@RequestMapping(path = "/api/flink/artifact")
public class ArtifactController {

    @Autowired
    private FlinkArtifactService flinkArtifactService;

    @Logging
    @PostMapping
    @ApiOperation(value = "上传 artifact", notes = "上传artifact")
    public ResponseEntity<ResponseVO> upload(@Valid FlinkArtifactUploadParam param, @RequestPart("file") MultipartFile file) throws IOException {
        flinkArtifactService.upload(param, file);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("{id}")
    @ApiOperation(value = "下载 artifact", notes = "下载 artifact")
    public ResponseEntity<ResponseVO> download(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            final String fileName = flinkArtifactService.download(id, outputStream);
            response.setCharacterEncoding("utf-8");// 设置字符编码
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8")); // 设置响应头
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除 artifact", notes = "删除 artifact")
    public ResponseEntity<ResponseVO> deleteById(@PathVariable("id") Long id) throws IOException {
        flinkArtifactService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping
    @ApiOperation(value = "查询 artifact 列表", notes = "查询 artifact 列表")
    public ResponseEntity<Page<FlinkArtifactDTO>> list(@Valid FlinkArtifactListParam param) {
        final Page<FlinkArtifactDTO> result = flinkArtifactService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
