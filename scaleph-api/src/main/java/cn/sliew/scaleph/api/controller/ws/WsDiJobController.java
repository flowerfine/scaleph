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
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelJobService;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobService;
import cn.sliew.scaleph.engine.seatunnel.service.dto.DagPanelDTO;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobDTO;
import cn.sliew.scaleph.engine.seatunnel.service.param.*;
import cn.sliew.scaleph.engine.seatunnel.service.vo.DiJobAttrVO;
import cn.sliew.scaleph.plugin.framework.exception.PluginException;
import cn.sliew.scaleph.system.vo.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "数据开发-作业管理")
@RestController
@RequestMapping(path = {"/api/datadev/job", "/api/di/job"})
public class WsDiJobController {

    @Autowired
    private WsDiJobService wsDiJobService;
    @Autowired
    private SeatunnelJobService seatunnelJobService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询 seatunnel 列表", notes = "分页查询 seatunnel 列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_SELECT)")
    public ResponseEntity<Page<WsDiJobDTO>> listJob(@Valid WsDiJobListParam param) {
        Page<WsDiJobDTO> page = wsDiJobService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/all")
    @ApiOperation(value = "查询 seatunnel 列表", notes = "查询 seatunnel 列表")
    public ResponseEntity<List<WsDiJobDTO>> listAll(@Valid WsDiJobSelectListParam param) {
        final List<WsDiJobDTO> result = wsDiJobService.listAll(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "新增 seatunnel", notes = "新增 seatunnel，不涉及 DAG")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_ADD)")
    public ResponseEntity<ResponseVO> simpleAddJob(@Validated @RequestBody WsDiJobAddParam param) {
        WsDiJobDTO wsDiJobDTO = wsDiJobService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(wsDiJobDTO), HttpStatus.CREATED);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "修改 seatunnel", notes = "只修改 seatunnel 属性，不涉及 DAG")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> simpleEditJob(@Validated @RequestBody WsDiJobUpdateParam param) {
        wsDiJobService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除 seatunnel", notes = "删除 seatunnel")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_DELETE)")
    public ResponseEntity<ResponseVO> deleteJob(@PathVariable("id") Long id) {
        wsDiJobService.delete(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "batch")
    @ApiOperation(value = "批量删除 seatunnel", notes = "批量删除 seatunnel")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_DELETE)")
    public ResponseEntity<ResponseVO> deleteJob(@RequestBody List<Long> ids) {
        wsDiJobService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "{id}/dag")
    @ApiOperation(value = "查询 seatunnel dag", notes = "查询 seatunnel DAG")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_SELECT)")
    public ResponseEntity<WsDiJobDTO> getJobDetail(@PathVariable("id") Long id) {
        WsDiJobDTO job = wsDiJobService.queryJobGraph(id);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "step")
    @ApiOperation(value = "保存步骤属性信息", notes = "保存步骤属性信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobStepInfo(@Valid @RequestBody WsDiJobStepParam param) {
        Long editableJobId = wsDiJobService.saveJobStep(param);
        return new ResponseEntity<>(ResponseVO.success(editableJobId), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "{id}/dag")
    @ApiOperation(value = "保存 seatunnel dag", notes = "保存 seatunnel dag")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobDetail(@PathVariable("id") Long id, @Validated @RequestBody WsDiJobGraphParam param) throws ScalephException {
        param.setJobId(id);
        Long editableJobId = wsDiJobService.saveJobGraph(param);
        return new ResponseEntity<>(ResponseVO.success(editableJobId), HttpStatus.CREATED);
    }

    @Logging
    @GetMapping(path = "{id}/attr")
    @ApiOperation(value = "查询 seatunnel 属性", notes = "查询 seatunnel 属性")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<DiJobAttrVO> listJobAttr(@PathVariable("id") Long id) {
        DiJobAttrVO vo = wsDiJobService.listJobAttrs(id);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "{id}/attr")
    @ApiOperation(value = "修改 seatunnel 属性", notes = "修改 seatunnel 属性")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobAttr(@PathVariable("id") Long id, @Validated @RequestBody DiJobAttrVO jobAttrVO) throws ScalephException {
        jobAttrVO.setJobId(id);
        Long editableJobId = wsDiJobService.saveJobAttrs(jobAttrVO);
        return new ResponseEntity<>(ResponseVO.success(editableJobId), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "{id}/preview")
    @ApiOperation(value = "任务预览", notes = "任务预览")
    public ResponseEntity<ResponseVO> previewJob(@PathVariable("id") Long id) throws Exception {
        String conf = seatunnelJobService.preview(id);
        return new ResponseEntity<>(ResponseVO.success(conf), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/node/meta/{type}")
    @ApiOperation(value = "查询DAG节点元信息", notes = "后端统一返回节点信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_SELECT)")
    public ResponseEntity<List<DagPanelDTO>> loadNodeMeta(@PathVariable("type") SeaTunnelEngineType type) throws PluginException {
        List<DagPanelDTO> list = seatunnelJobService.loadDndPanelInfo(type);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}