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

package cn.sliew.scaleph.api.controller;

import cn.sliew.scaleph.api.vo.ResponseVO;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.engine.flink.FlinkRelease;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConfigService;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelJobService;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "测试模块")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private SeatunnelStorageService seatunnelStorageService;
    @Autowired
    private SeatunnelJobService seatunnelJobService;
    @Autowired
    private SeatunnelConfigService seatunnelConfigService;

    @PostMapping(path = "/downloadFlinkRelease")
    @ApiOperation("下载 flink release")
    public ResponseEntity<ResponseVO> downloadFlinkRelease() {
        seatunnelStorageService.downloadFlinkRelease(FlinkRelease.V_1_13_6);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @GetMapping(path = "/buildSeatunnelConfig")
    @ApiOperation("生成 seatunnel config")
    public ResponseEntity<ResponseVO> buildSeatunnelConfig(@RequestParam("jobId") Long jobId) {
        final DiJobDTO diJobDTO = seatunnelJobService.queryJobInfo(jobId);
        final String config = seatunnelConfigService.buildConfig(diJobDTO);
        System.out.println(config);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
