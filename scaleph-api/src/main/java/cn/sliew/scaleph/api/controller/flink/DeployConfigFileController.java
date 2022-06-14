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
import cn.sliew.scaleph.engine.flink.service.FlinkDeployConfigFileService;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkDeployConfigFileDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkDeployConfigFileListParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkDeployConfigFileUploadParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Api(tags = "Flink管理-部署配置文件管理")
@RestController
@RequestMapping(path = "/api/flink/deploy-config-file")
public class DeployConfigFileController {

    @Autowired
    private FlinkDeployConfigFileService flinkDeployConfigFileService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询部署配置文件列表", notes = "查询部署配置文件列表")
    public ResponseEntity<Page<FlinkDeployConfigFileDTO>> list(@Valid FlinkDeployConfigFileListParam param) throws IOException {
        final Page<FlinkDeployConfigFileDTO> result = flinkDeployConfigFileService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 支持文件上传和表单一起提交，如果是多个文件时，可以使用 {@code @RequestParam("files") MultipartFile[] files}
     */
    @Logging
    @PostMapping("upload")
    @ApiOperation(value = "上传部署配置文件", notes = "上传部署配置文件，支持上传多个文件")
    public ResponseEntity<ResponseVO> upload(@Valid FlinkDeployConfigFileUploadParam param, @RequestPart("files") MultipartFile[] files) throws Exception {
        flinkDeployConfigFileService.upload(param, files);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
