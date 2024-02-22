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
import cn.sliew.scaleph.workspace.seatunnel.service.SeaTunnelJobService;
import cn.sliew.scaleph.workspace.seatunnel.service.WsDiJobService;
import cn.sliew.scaleph.workspace.seatunnel.service.dto.WsDiJobDTO;
import cn.sliew.scaleph.workspace.seatunnel.service.param.*;
import cn.sliew.scaleph.workspace.seatunnel.service.vo.DiJobAttrVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "数据开发-作业管理")
@RestController
@RequestMapping(path = {"/api/datadev/job", "/api/di/job"})
public class WsDiJobController {

    @Autowired
    private WsDiJobService wsDiJobService;
    @Autowired
    private SeaTunnelJobService seatunnelJobService;

    @Logging
    @GetMapping
    @Operation(summary = "查询 seatunnel 列表", description = "分页查询 seatunnel 列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_SELECT)")
    public ResponseEntity<Page<WsDiJobDTO>> listJob(@Valid WsDiJobListParam param) {
        Page<WsDiJobDTO> page = wsDiJobService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/all")
    @Operation(summary = "查询 seatunnel 列表", description = "查询 seatunnel 列表")
    public ResponseEntity<List<WsDiJobDTO>> listAll(@Valid WsDiJobSelectListParam param) {
        final List<WsDiJobDTO> result = wsDiJobService.listAll(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增 seatunnel", description = "新增 seatunnel，不涉及 DAG")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_ADD)")
    public ResponseEntity<ResponseVO> simpleAddJob(@Validated @RequestBody WsDiJobAddParam param) {
        WsDiJobDTO wsDiJobDTO = wsDiJobService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(wsDiJobDTO), HttpStatus.CREATED);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改 seatunnel", description = "只修改 seatunnel 属性，不涉及 DAG")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> simpleEditJob(@Validated @RequestBody WsDiJobUpdateParam param) {
        wsDiJobService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @Operation(summary = "删除 seatunnel", description = "删除 seatunnel")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_DELETE)")
    public ResponseEntity<ResponseVO> deleteJob(@PathVariable("id") Long id) {
        wsDiJobService.delete(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("batch")
    @Operation(summary = "批量删除 seatunnel", description = "批量删除 seatunnel")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_DELETE)")
    public ResponseEntity<ResponseVO> deleteJob(@RequestBody List<Long> ids) {
        wsDiJobService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("{id}/dag")
    @Operation(summary = "查询 seatunnel dag", description = "查询 seatunnel DAG")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_SELECT)")
    public ResponseEntity<WsDiJobDTO> getJobDetail(@PathVariable("id") Long id) {
        WsDiJobDTO job = wsDiJobService.queryJobGraph(id);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    @Logging
    @PostMapping("step")
    @Operation(summary = "保存步骤属性信息", description = "保存步骤属性信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobStepInfo(@Valid @RequestBody WsDiJobStepParam param) {
        Long editableJobId = wsDiJobService.saveJobStep(param);
        return new ResponseEntity<>(ResponseVO.success(editableJobId), HttpStatus.OK);
    }

    @Logging
    @PostMapping("{id}/dag")
    @Operation(summary = "保存 seatunnel dag", description = "保存 seatunnel dag")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobDetail(@PathVariable("id") Long id, @Validated @RequestBody WsDiJobGraphParam param) throws ScalephException {
        param.setJobId(id);
        Long editableJobId = wsDiJobService.saveJobGraph(param);
        return new ResponseEntity<>(ResponseVO.success(editableJobId), HttpStatus.CREATED);
    }

    @Logging
    @GetMapping("{id}/attr")
    @Operation(summary = "查询 seatunnel 属性", description = "查询 seatunnel 属性")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<DiJobAttrVO> listJobAttr(@PathVariable("id") Long id) {
        DiJobAttrVO vo = wsDiJobService.listJobAttrs(id);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @Logging
    @PostMapping("{id}/attr")
    @Operation(summary = "修改 seatunnel 属性", description = "修改 seatunnel 属性")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobAttr(@PathVariable("id") Long id, @Validated @RequestBody DiJobAttrVO jobAttrVO) throws ScalephException {
        jobAttrVO.setJobId(id);
        Long editableJobId = wsDiJobService.saveJobAttrs(jobAttrVO);
        return new ResponseEntity<>(ResponseVO.success(editableJobId), HttpStatus.OK);
    }

    @Logging
    @GetMapping("{id}/preview")
    @Operation(summary = "任务预览", description = "任务预览")
    public ResponseEntity<ResponseVO> previewJob(@PathVariable("id") Long id) throws Exception {
        String conf = seatunnelJobService.preview(id);
        return new ResponseEntity<>(ResponseVO.success(conf), HttpStatus.OK);
    }
}