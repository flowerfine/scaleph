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

package cn.sliew.scaleph.api.controller.studio;

import cn.hutool.core.date.DateUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.api.vo.TransferVO;
import cn.sliew.scaleph.core.di.service.DiJobService;
import cn.sliew.scaleph.core.di.service.DiProjectService;
import cn.sliew.scaleph.engine.flink.service.FlinkClusterInstanceService;
import cn.sliew.scaleph.engine.flink.service.FlinkJobLogService;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobLogDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobLogListParam;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "工作台-数据看板")
@RestController
@RequestMapping(path = "/api/studio/databoard")
public class DataBoardController {

    @Autowired
    private DiProjectService diProjectService;
    @Autowired
    private FlinkClusterInstanceService flinkClusterInstanceService;
    @Autowired
    private DiJobService diJobService;
    @Autowired
    private FlinkJobLogService flinkJobLogService;

    @Logging
    @GetMapping(path = "/project")
    @ApiOperation(value = "查询项目数量", notes = "查询项目数量")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STUDIO_DATA_BOARD_SHOW)")
    public ResponseEntity<Long> countProject() {
        Long result = this.diProjectService.totalCnt();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/cluster")
    @ApiOperation(value = "查询集群数量", notes = "查询集群数量")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STUDIO_DATA_BOARD_SHOW)")
    public ResponseEntity<Long> countCluster() {
        Long result = this.flinkClusterInstanceService.totalCnt();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/job")
    @ApiOperation(value = "查询作业数量", notes = "查询作业数量")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STUDIO_DATA_BOARD_SHOW)")
    public ResponseEntity<Long> countJob(@RequestParam(value = "jobType") String jobType) {
        Long result = this.diJobService.totalCnt(jobType);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/topBatch100")
    @ApiOperation(value = "查询近7日周期任务运行时长TOP100", notes = "查询近7日周期任务运行时长TOP100")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STUDIO_DATA_BOARD_SHOW)")
    public ResponseEntity<List<FlinkJobLogDTO>> batchTop100In7d() {
        //todo 查询近7日周期任务运行时长TOP100
        Date currentDate = DateUtil.beginOfDay(new Date());
        List<FlinkJobLogDTO> list =
                this.flinkJobLogService.list(new FlinkJobLogListParam(0L)).getRecords();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/realtimeJob")
    @ApiOperation(value = "统计实时任务运行状态分布", notes = "统计实时任务运行状态分布")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STUDIO_DATA_BOARD_SHOW)")
    public ResponseEntity<List<TransferVO>> realtimeJobRuntimeStatus() {
        //todo
        List<TransferVO> list = new ArrayList<>();
//        Map<String, String> map = this.diJobLogService.groupRealtimeJobRuntimeStatus();
//        map.forEach((key, value) -> {
//            list.add(new TransferVO(value, key));
//        });
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
