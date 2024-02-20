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
import cn.sliew.scaleph.dag.xflow.dnd.DndDTO;
import cn.sliew.scaleph.workspace.flink.cdc.service.FlinkCDCDagService;
import cn.sliew.scaleph.workspace.flink.cdc.service.FlinkCDCJobService;
import cn.sliew.scaleph.workspace.flink.cdc.service.dto.WsFlinkArtifactCDCDTO;
import cn.sliew.scaleph.workspace.flink.cdc.service.param.WsFlinkArtifactCDCAddParam;
import cn.sliew.scaleph.workspace.flink.cdc.service.param.WsFlinkArtifactCDCListParam;
import cn.sliew.scaleph.workspace.flink.cdc.service.param.WsFlinkArtifactCDCSelectListParam;
import cn.sliew.scaleph.workspace.flink.cdc.service.param.WsFlinkArtifactCDCUpdateParam;
import cn.sliew.scaleph.plugin.framework.exception.PluginException;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Flink CDC")
@RestController
@RequestMapping(path = "/api/flink-cdc")
public class WsFlinkCDCController {

    @Autowired
    private FlinkCDCDagService flinkCDCDagService;
    @Autowired
    private FlinkCDCJobService flinkCDCJobService;

    @Logging
    @GetMapping("/dag/dnd")
    @Operation(summary = "查询DAG节点元信息", description = "后端统一返回节点信息")
    public ResponseEntity<ResponseVO<List<DndDTO>>> loadNodeMeta() throws PluginException {
        List<DndDTO> dnds = flinkCDCDagService.getDnds();
        return new ResponseEntity<>(ResponseVO.success(dnds), HttpStatus.OK);
    }

    @Logging
    @GetMapping
    @Operation(summary = "查询 fink cdc 列表", description = "分页查询 fink cdc 列表")
    public ResponseEntity<Page<WsFlinkArtifactCDCDTO>> listJob(@Valid WsFlinkArtifactCDCListParam param) {
        Page<WsFlinkArtifactCDCDTO> page = flinkCDCJobService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/all")
    @Operation(summary = "查询 fink cdc 列表", description = "查询 fink cdc 列表")
    public ResponseEntity<List<WsFlinkArtifactCDCDTO>> listAll(@Valid WsFlinkArtifactCDCSelectListParam param) {
        List<WsFlinkArtifactCDCDTO> result = flinkCDCJobService.listAll(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增 fink cdc", description = "新增 fink cdc，不涉及 DAG")
    public ResponseEntity<ResponseVO<WsFlinkArtifactCDCDTO>> simpleAddJob(@Validated @RequestBody WsFlinkArtifactCDCAddParam param) {
        WsFlinkArtifactCDCDTO wsFlinkArtifactCDCDTO = flinkCDCJobService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(wsFlinkArtifactCDCDTO), HttpStatus.CREATED);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改 fink cdc", description = "只修改 fink cdc 属性，不涉及 DAG")
    public ResponseEntity<ResponseVO> simpleEditJob(@Validated @RequestBody WsFlinkArtifactCDCUpdateParam param) {
        flinkCDCJobService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @Operation(summary = "删除 fink cdc", description = "删除 fink cdc")
    public ResponseEntity<ResponseVO> deleteJob(@PathVariable("id") Long id) throws ScalephException {
        flinkCDCJobService.delete(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("batch")
    @Operation(summary = "批量删除 fink cdc", description = "批量删除 fink cdc")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) throws ScalephException {
        flinkCDCJobService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("all")
    @Operation(summary = "批量删除 fink cdc", description = "批量删除 fink cdc")
    public ResponseEntity<ResponseVO> deleteAll(@RequestParam("flinkArtifactId") Long flinkArtifactId) throws ScalephException {
        flinkCDCJobService.deleteAll(flinkArtifactId);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }
}