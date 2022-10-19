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

package cn.sliew.scaleph.api.controller.scheduler;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.core.scheduler.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(tags = "调度管理-调度服务")
@RestController
@RequestMapping(path = {"/api/scheduler"})
public class SchedulerController {

    @Autowired
    private ScheduleService scheduleService;

    @Logging
    @GetMapping(path = "/cron/next")
    @ApiOperation(value = "查询最近5次运行时间", notes = "查询最近5次运行时间")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<List<Date>> listNext5FireTime(@RequestParam("crontabStr") String crontabStr) throws ParseException {
        List<Date> dates = scheduleService.listNext5FireTime(crontabStr);
        return new ResponseEntity<>(dates, HttpStatus.OK);
    }
}
