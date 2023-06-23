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
import cn.sliew.scaleph.engine.flink.service.WsFlinkClusterInstanceService;
import cn.sliew.scaleph.engine.flink.service.WsFlinkJobLogService;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkJobLogDTO;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkJobLogListParam;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobService;
import cn.sliew.scaleph.project.service.WsProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "工作台-数据看板")
@RestController
@RequestMapping(path = "/api/studio/databoard")
public class DataBoardController {

    @Autowired
    private WsProjectService wsProjectService;
    @Autowired
    private WsFlinkClusterInstanceService wsFlinkClusterInstanceService;
    @Autowired
    private WsDiJobService wsDiJobService;
    @Autowired
    private WsFlinkJobLogService wsFlinkJobLogService;

    @Logging
    @GetMapping(path = "/project")
    @Operation(summary = "查询项目数量", description = "查询项目数量")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STUDIO_DATA_BOARD_SHOW)")
    public ResponseEntity<Long> countProject() {
        Long result = this.wsProjectService.totalCnt();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/cluster")
    @Operation(summary = "查询集群数量", description = "查询集群数量")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STUDIO_DATA_BOARD_SHOW)")
    public ResponseEntity<Long> countCluster() {
        Long result = this.wsFlinkClusterInstanceService.totalCnt();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/job")
    @Operation(summary = "查询作业数量", description = "查询作业数量")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STUDIO_DATA_BOARD_SHOW)")
    public ResponseEntity<Long> countJob(@RequestParam(value = "jobType") String jobType) {
        Long result = this.wsDiJobService.totalCnt(jobType);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/topBatch100")
    @Operation(summary = "查询近7日周期任务运行时长TOP100", description = "查询近7日周期任务运行时长TOP100")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STUDIO_DATA_BOARD_SHOW)")
    public ResponseEntity<List<WsFlinkJobLogDTO>> batchTop100In7d() {
        //todo 查询近7日周期任务运行时长TOP100
        Date currentDate = DateUtil.beginOfDay(new Date());
        List<WsFlinkJobLogDTO> list =
                this.wsFlinkJobLogService.list(new WsFlinkJobLogListParam(0L)).getRecords();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/realtimeJob")
    @Operation(summary = "统计实时任务运行状态分布", description = "统计实时任务运行状态分布")
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
