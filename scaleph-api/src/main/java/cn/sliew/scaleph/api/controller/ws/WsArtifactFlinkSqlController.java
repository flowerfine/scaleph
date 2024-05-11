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
import cn.sliew.scaleph.workspace.flink.sql.service.WsArtifactFlinkSqlService;
import cn.sliew.scaleph.workspace.flink.sql.service.dto.WsArtifactFlinkSqlDTO;
import cn.sliew.scaleph.workspace.flink.sql.service.param.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Artifact管理-Flink-Sql")
@RestController
@RequestMapping(path = "/api/artifact/flink/sql")
public class WsArtifactFlinkSqlController {

    @Autowired
    private WsArtifactFlinkSqlService wsArtifactFlinkSqlService;

    @Logging
    @GetMapping
    @Operation(summary = "查询 flink sql 列表", description = "查询 flink sql 列表")
    public ResponseEntity<Page<WsArtifactFlinkSqlDTO>> list(@Valid WsArtifactFlinkSqlListParam param) {
        Page<WsArtifactFlinkSqlDTO> result = wsArtifactFlinkSqlService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/history")
    @Operation(summary = "根据 artifact 分页查询 flink sql 列表", description = "根据 artifact 分页查询 flink sql 列表")
    public ResponseEntity<Page<WsArtifactFlinkSqlDTO>> listByArtifact(@Valid WsArtifactFlinkSqlArtifactParam param) {
        Page<WsArtifactFlinkSqlDTO> result = wsArtifactFlinkSqlService.listByArtifact(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/all")
    @Operation(summary = "查询 flink sql 列表", description = "查询 flink sql 列表")
    public ResponseEntity<ResponseVO<List<WsArtifactFlinkSqlDTO>>> listAll(@Valid WsArtifactFlinkSqlSelectListParam param) {
        List<WsArtifactFlinkSqlDTO> result = wsArtifactFlinkSqlService.listAll(param);
        return new ResponseEntity<>(ResponseVO.success(result), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @Operation(summary = "查询 flink sql 详情", description = "查询 flink sql 详情")
    public ResponseEntity<WsArtifactFlinkSqlDTO> selectOne(@PathVariable("id") Long id) {
        WsArtifactFlinkSqlDTO result = wsArtifactFlinkSqlService.selectOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增 flink sql", description = "新增 flink sql")
    public ResponseEntity<ResponseVO> insert(@RequestBody @Valid WsArtifactFlinkSqlInsertParam param) {
        wsArtifactFlinkSqlService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改 flink sql", description = "修改 flink sql")
    public ResponseEntity<ResponseVO> update(@RequestBody @Valid WsArtifactFlinkSqlUpdateParam param) {
        wsArtifactFlinkSqlService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("script")
    @Operation(summary = "修改 flink sql script", description = "修改 flink sql script")
    public ResponseEntity<ResponseVO> updateScript(@RequestBody @Valid WsArtifactFlinkSqlScriptUpdateParam param) {
        wsArtifactFlinkSqlService.updateScript(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/{id}")
    @Operation(summary = "删除 flink sql", description = "删除 flink sql")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) {
        wsArtifactFlinkSqlService.deleteOne(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/artifact/{artifactId}")
    @Operation(summary = "删除 artifact", description = "删除 artifactl")
    public ResponseEntity<ResponseVO> deleteArtifact(@PathVariable("artifactId") Long artifactId) {
        wsArtifactFlinkSqlService.deleteArtifact(artifactId);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }
}
