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
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.system.model.ResponseVO;
import cn.sliew.scaleph.workspace.flink.cdc.service.WsArtifactFlinkCDCService;
import cn.sliew.scaleph.workspace.flink.cdc.service.dto.WsArtifactFlinkCDCDTO;
import cn.sliew.scaleph.workspace.flink.cdc.service.param.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Artifact管理-Flink-CDC")
@RestController
@RequestMapping(path = "/api/artifact/flink/cdc")
public class WsArtifactFlinkCDCController {

    @Autowired
    private WsArtifactFlinkCDCService wsArtifactFlinkCDCService;

    @Logging
    @GetMapping
    @Operation(summary = "查询 fink cdc 列表", description = "分页查询 fink cdc 列表")
    public ResponseEntity<Page<WsArtifactFlinkCDCDTO>> list(@Valid WsArtifactFlinkCDCListParam param) {
        Page<WsArtifactFlinkCDCDTO> page = wsArtifactFlinkCDCService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/history")
    @Operation(summary = "根据 artifact 分页查询 fink cdc 列表", description = "根据 artifact 分页查询 fink cdc 列表")
    public ResponseEntity<Page<WsArtifactFlinkCDCDTO>> listByArtifact(@Valid WsArtifactFlinkCDCArtifactParam param) {
        Page<WsArtifactFlinkCDCDTO> result = wsArtifactFlinkCDCService.listByArtifact(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/all")
    @Operation(summary = "查询 fink cdc 列表", description = "查询 fink cdc 列表")
    public ResponseEntity<ResponseVO<List<WsArtifactFlinkCDCDTO>>> listAll(@Valid WsArtifactFlinkCDCSelectListParam param) {
        List<WsArtifactFlinkCDCDTO> result = wsArtifactFlinkCDCService.listAll(param);
        return new ResponseEntity<>(ResponseVO.success(result), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @Operation(summary = "查询 flink cdc 详情", description = "查询 flink cdc 详情")
    public ResponseEntity<WsArtifactFlinkCDCDTO> selectOne(@PathVariable("id") Long id) {
        WsArtifactFlinkCDCDTO result = wsArtifactFlinkCDCService.selectOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增 fink cdc", description = "新增 fink cdc，不涉及 DAG")
    public ResponseEntity<ResponseVO<WsArtifactFlinkCDCDTO>> insert(@Validated @RequestBody WsArtifactFlinkCDCAddParam param) {
        WsArtifactFlinkCDCDTO wsFlinkArtifactCDCDTO = wsArtifactFlinkCDCService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(wsFlinkArtifactCDCDTO), HttpStatus.CREATED);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改 fink cdc", description = "只修改 fink cdc 属性，不涉及 DAG")
    public ResponseEntity<ResponseVO> update(@Validated @RequestBody WsArtifactFlinkCDCUpdateParam param) {
        wsArtifactFlinkCDCService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @Operation(summary = "删除 fink cdc", description = "删除 fink cdc")
    public ResponseEntity<ResponseVO> deleteJob(@PathVariable("id") Long id) throws ScalephException {
        wsArtifactFlinkCDCService.delete(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("batch")
    @Operation(summary = "批量删除 fink cdc", description = "批量删除 fink cdc")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) throws ScalephException {
        wsArtifactFlinkCDCService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("artifact/{artifactId}")
    @Operation(summary = "删除 artifact", description = "删除 artifact")
    public ResponseEntity<ResponseVO> deleteArtifact(@PathVariable("artifactId") Long artifactId) throws ScalephException {
        wsArtifactFlinkCDCService.deleteArtifact(artifactId);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("preview")
    @Operation(summary = "预览 flink cdc 配置", description = "预览 flink cdc 配置")
    public ResponseEntity<ResponseVO<JsonNode>> previewJob(@RequestBody WsArtifactFlinkCDCDTO dto) throws Exception {
        JsonNode conf = wsArtifactFlinkCDCService.buildConfig(dto);
        return new ResponseEntity<>(ResponseVO.success(conf), HttpStatus.OK);
    }
}