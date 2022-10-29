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
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.core.di.service.DiJobResourceFileService;
import cn.sliew.scaleph.core.di.service.DiJobService;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.core.di.service.dto.DiResourceFileDTO;
import cn.sliew.scaleph.core.di.service.param.*;
import cn.sliew.scaleph.core.di.service.vo.DiJobAttrVO;
import cn.sliew.scaleph.core.di.service.vo.DiJobRunVO;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelJobService;
import cn.sliew.scaleph.engine.seatunnel.service.dto.DagPanelDTO;
import cn.sliew.scaleph.plugin.framework.exception.PluginException;
import cn.sliew.scaleph.system.service.vo.DictVO;
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
import java.util.ArrayList;
import java.util.List;

@Api(tags = "数据开发-作业管理")
@RestController
@RequestMapping(path = {"/api/datadev/job", "/api/di/job"})
public class JobController {

    @Autowired
    private DiJobService diJobService;
    @Autowired
    private DiJobResourceFileService diJobResourceFileService;
    @Autowired
    private SeatunnelJobService seatunnelJobService;

    @Logging
    @GetMapping
    @ApiOperation(value = "分页查询作业列表", notes = "分页查询作业列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_SELECT)")
    public ResponseEntity<Page<DiJobDTO>> listJob(@Valid DiJobParam param) {
        Page<DiJobDTO> page = diJobService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "新增作业记录", notes = "新增一条作业记录，相关流程定义不涉及")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_ADD)")
    public ResponseEntity<ResponseVO> simpleAddJob(@Validated @RequestBody DiJobAddParam param) {
        DiJobDTO diJobDTO = diJobService.insert(param);
        return new ResponseEntity<>(ResponseVO.sucess(diJobDTO), HttpStatus.CREATED);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "修改作业记录", notes = "只修改作业记录属性，相关流程定义不改变")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> simpleEditJob(@Validated @RequestBody DiJobUpdateParam param) {
        diJobService.update(param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除作业", notes = "删除作业")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_DELETE)")
    public ResponseEntity<ResponseVO> deleteJob(@PathVariable("id") Long id) {
        diJobService.delete(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/batch")
    @ApiOperation(value = "批量删除作业", notes = "批量删除作业")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_DELETE)")
    public ResponseEntity<ResponseVO> deleteJob(@RequestBody List<Long> ids) {
        diJobService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }


    @Logging
    @GetMapping(path = "/detail")
    @ApiOperation(value = "查询作业详情", notes = "查询作业详情，包含作业流程定义信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_SELECT)")
    public ResponseEntity<DiJobDTO> getJobDetail(@RequestParam(value = "id") Long id) {
        DiJobDTO job = diJobService.queryJobGraph(id);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/step")
    @ApiOperation(value = "保存步骤属性信息", notes = "保存步骤属性信息，未触发作业版本号变更")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobStepInfo(@Valid @RequestBody DiJobStepParam param) throws ScalephException {
        Long editableJobId = diJobService.saveJobStep(param);
        return new ResponseEntity<>(ResponseVO.sucess(editableJobId), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/detail")
    @ApiOperation(value = "保存作业详情", notes = "保存作业相关流程定义，如果已经有对应版本号的数据，则提醒用户编辑最新版本。")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobDetail(@Validated @RequestBody DiJobGraphParam param) throws ScalephException {
        Long editableJobId = diJobService.saveJobGraph(param);
        return new ResponseEntity<>(ResponseVO.sucess(editableJobId), HttpStatus.CREATED);
    }

    @Logging
    @GetMapping(path = "/attr/{jobId}")
    @ApiOperation(value = "查询作业属性", notes = "查询作业属性列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<DiJobAttrVO> listJobAttr(@PathVariable(value = "jobId") Long jobId) {
        DiJobAttrVO vo = diJobService.listJobAttrs(jobId);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/attr")
    @ApiOperation(value = "修改作业属性", notes = "修改作业属性信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobAttr(@RequestBody DiJobAttrVO jobAttrVO) throws ScalephException {
        Long editableJobId = diJobService.saveJobAttrs(jobAttrVO);
        return new ResponseEntity<>(ResponseVO.sucess(editableJobId), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/publish/{jobId}")
    @ApiOperation(value = "发布任务", notes = "发布任务")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> publishJob(@PathVariable(value = "jobId") Long jobId) throws ScalephException {
        diJobService.publish(jobId);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/preview/{jobId}")
    @ApiOperation(value = "任务预览", notes = "任务预览")
    public ResponseEntity<ResponseVO> previewJob(@PathVariable(value = "jobId") Long jobId) throws Exception {
        String conf = seatunnelJobService.preview(jobId);
        return new ResponseEntity<>(ResponseVO.sucess(conf), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/run")
    @ApiOperation(value = "运行任务", notes = "运行任务，提交至集群")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> runJob(@RequestBody DiJobRunVO jobRunParam) throws Exception {
        seatunnelJobService.run(jobRunParam);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/stop")
    @ApiOperation(value = "停止任务", notes = "停止任务,自动创建savepoint,作业可能会正常运行完后停止。任务的日志状态通过定时任务同步")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> stopJob(@RequestParam(value = "jobId") Long jobId) throws Exception {
        seatunnelJobService.stop(jobId);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/resource/{jobId}")
    @ApiOperation(value = "查询作业资源", notes = "查询作业资源列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<List<DictVO>> listJobResourceFile(@PathVariable("jobId") Long jobId) {
        List<DictVO> list = new ArrayList<>();
        List<DiResourceFileDTO> resourceList = diJobResourceFileService.listJobResources(jobId);
        for (DiResourceFileDTO dto : resourceList) {
            DictVO dict = new DictVO(String.valueOf(dto.getId()), dto.getFileName());
            list.add(dict);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/node/meta")
    @ApiOperation(value = "查询DAG节点元信息", notes = "后端统一返回节点信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_SELECT)")
    public ResponseEntity<List<DagPanelDTO>> loadNodeMeta() throws PluginException {
        List<DagPanelDTO> list = seatunnelJobService.loadDndPanelInfo();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}