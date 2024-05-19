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
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelEngineType;
import cn.sliew.scaleph.dag.xflow.dnd.DndDTO;
import cn.sliew.scaleph.plugin.framework.exception.PluginException;
import cn.sliew.scaleph.system.model.ResponseVO;
import cn.sliew.scaleph.workspace.seatunnel.service.SeaTunnelDagService;
import cn.sliew.scaleph.workspace.seatunnel.service.WsArtifactSeaTunnelService;
import cn.sliew.scaleph.workspace.seatunnel.service.dto.WsArtifactSeaTunnelDTO;
import cn.sliew.scaleph.workspace.seatunnel.service.param.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Artifact管理-SeaTunnel")
@RestController
@RequestMapping(path = "/api/artifact/seatunnel")
public class WsArtifactSeaTunnelController {

    @Autowired
    private WsArtifactSeaTunnelService wsArtifactSeaTunnelService;
    @Autowired
    private SeaTunnelDagService seaTunnelDagService;

    @Logging
    @GetMapping
    @Operation(summary = "查询 seatunnel 列表", description = "查询 seatunnel 列表")
    public ResponseEntity<Page<WsArtifactSeaTunnelDTO>> list(@Valid WsArtifactSeaTunnelListParam param) {
        Page<WsArtifactSeaTunnelDTO> result = wsArtifactSeaTunnelService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/history")
    @Operation(summary = "根据 artifact 分页查询 seatunnel 列表", description = "根据 artifact 分页查询 seatunnel 列表")
    public ResponseEntity<Page<WsArtifactSeaTunnelDTO>> listByArtifact(@Valid WsArtifactSeaTunnelArtifactParam param) {
        Page<WsArtifactSeaTunnelDTO> result = wsArtifactSeaTunnelService.listByArtifact(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/all")
    @Operation(summary = "查询 seatunnel 列表", description = "查询 seatunnel 列表")
    public ResponseEntity<ResponseVO<List<WsArtifactSeaTunnelDTO>>> listAll(@Valid WsArtifactSeaTunnelSelectListParam param) {
        List<WsArtifactSeaTunnelDTO> result = wsArtifactSeaTunnelService.listAll(param);
        return new ResponseEntity<>(ResponseVO.success(result), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @Operation(summary = "查询 seatunnel 详情", description = "查询 seatunnel 详情")
    public ResponseEntity<WsArtifactSeaTunnelDTO> selectOne(@PathVariable("id") Long id) {
        WsArtifactSeaTunnelDTO result = wsArtifactSeaTunnelService.selectOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增 seatunnel", description = "新增 seatunnel")
    public ResponseEntity<ResponseVO<WsArtifactSeaTunnelDTO>> insert(@RequestBody @Valid WsArtifactSeaTunnelAddParam param) {
        WsArtifactSeaTunnelDTO dto = wsArtifactSeaTunnelService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改 seatunnel", description = "修改 seatunnel")
    public ResponseEntity<ResponseVO> update(@RequestBody @Valid WsArtifactSeaTunnelUpdateParam param) {
        wsArtifactSeaTunnelService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("graph")
    @Operation(summary = "修改 seatunnel graph", description = "修改 seatunnel graph")
    public ResponseEntity<ResponseVO> updateGraph(@RequestBody @Valid WsArtifactSeaTunnelGraphParam param) {
        wsArtifactSeaTunnelService.updateGraph(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/{id}")
    @Operation(summary = "删除 seatunnel", description = "删除 seatunnel")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) {
        wsArtifactSeaTunnelService.delete(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/artifact/{artifactId}")
    @Operation(summary = "删除 artifact", description = "删除 artifactl")
    public ResponseEntity<ResponseVO> deleteArtifact(@PathVariable("artifactId") Long artifactId) {
        wsArtifactSeaTunnelService.deleteArtifact(artifactId);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/dag/dnd/{type}")
    @Operation(summary = "查询DAG节点元信息", description = "后端统一返回节点信息")
    public ResponseEntity<ResponseVO<List<DndDTO>>> loadNodeMeta(@PathVariable("type") SeaTunnelEngineType type) throws PluginException {
        List<DndDTO> dnds = seaTunnelDagService.getDnds(type);
        return new ResponseEntity<>(ResponseVO.success(dnds), HttpStatus.OK);
    }

    @Logging
    @GetMapping("{id}/preview")
    @Operation(summary = "预览 seatunnel 配置", description = "预览 seatunnel 配置")
    public ResponseEntity<ResponseVO<String>> previewJob(@PathVariable("id") Long id) throws Exception {
        String conf = wsArtifactSeaTunnelService.buildConfig(id, Optional.empty(), Optional.empty());
        return new ResponseEntity<>(ResponseVO.success(conf), HttpStatus.OK);
    }
}