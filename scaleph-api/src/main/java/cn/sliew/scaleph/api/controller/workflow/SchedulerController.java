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

package cn.sliew.scaleph.api.controller.workflow;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.system.vo.ResponseVO;
import cn.sliew.scaleph.workflow.scheduler.SchedulerService;
import cn.sliew.scaleph.workflow.service.WorkflowScheduleService;
import cn.sliew.scaleph.workflow.service.dto.WorkflowScheduleDTO;
import cn.sliew.scaleph.workflow.service.param.WorkflowScheduleAddParam;
import cn.sliew.scaleph.workflow.service.param.WorkflowScheduleListParam;
import cn.sliew.scaleph.workflow.service.param.WorkflowScheduleUpdateParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Api(tags = "workflow管理-调度管理")
@RestController
@RequestMapping(path = {"/api/scheduler"})
public class SchedulerController {

    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private WorkflowScheduleService workflowScheduleService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询 workflow 调度列表", notes = "查询 workflow 调度列表")
    public ResponseEntity<ResponseVO<List<WorkflowScheduleDTO>>> get(@Valid WorkflowScheduleListParam param) throws ParseException {
        List<WorkflowScheduleDTO> result = workflowScheduleService.list(param);
        return new ResponseEntity<>(ResponseVO.sucess(result), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "新增 workflow 调度", notes = "新增 workflow 调度")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody WorkflowScheduleAddParam param) throws ParseException {
        workflowScheduleService.insert(param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("{id}")
    @ApiOperation(value = "修改 workflow 调度", notes = "修改 workflow 调度")
    public ResponseEntity<ResponseVO> update(@PathVariable("id") Long id, @Valid @RequestBody WorkflowScheduleUpdateParam param) throws ParseException {
        workflowScheduleService.update(id, param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除 workflow 调度", notes = "删除 workflow 调度")
    public ResponseEntity<ResponseVO> deleteById(@PathVariable("id") Long id) {
        workflowScheduleService.delete(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @ApiOperation(value = "批量删除 workflow 调度", notes = "批量删除 workflow 调度")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) {
        workflowScheduleService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("{id}/enable")
    @ApiOperation(value = "启用 workflow 调度", notes = "启用 workflow 调度")
    public ResponseEntity<ResponseVO> enable(@PathVariable("id") Long id) throws ParseException {
        workflowScheduleService.enable(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("{id}/disable")
    @ApiOperation(value = "启用 workflow 调度", notes = "启用 workflow 调度")
    public ResponseEntity<ResponseVO> disable(@PathVariable("id") Long id) throws ParseException {
        workflowScheduleService.disable(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/cron/next")
    @ApiOperation(value = "查询最近5次运行时间", notes = "查询最近5次运行时间")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<List<Date>> listNext5FireTime(@RequestParam("crontabStr") String crontabStr) throws ParseException {
        List<Date> dates = schedulerService.listNext5FireTime(crontabStr);
        return new ResponseEntity<>(dates, HttpStatus.OK);
    }

}
