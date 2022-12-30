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
import cn.sliew.scaleph.engine.flink.service.*;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkCheckpointDTO;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkJobDTO;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkJobInstanceDTO;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkCheckpointListParam;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkJobInstanceListParam;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkJobSubmitParam;
import cn.sliew.scaleph.system.snowflake.UidGenerator;
import cn.sliew.scaleph.system.vo.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Flink管理-任务实例管理")
@RestController
@RequestMapping(path = "/api/flink/job-instance")
public class WsFlinkJobInstanceController {

    @Autowired
    private WsFlinkService wsFlinkService;
    @Autowired
    private WsFlinkYarnService wsFlinkYarnService;
    @Autowired
    private WsFlinkJobInstanceService wsFlinkJobInstanceService;
    @Autowired
    private WsFlinkCheckpointService wsFlinkCheckpointService;
    @Autowired
    private UidGenerator defaultUidGenerator;
    @Autowired
    private WsFlinkJobService wsFlinkJobService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询任务实例列表", notes = "分页任务实例列表")
    public ResponseEntity<Page<WsFlinkJobInstanceDTO>> list(@Valid WsFlinkJobInstanceListParam param) {
        Page<WsFlinkJobInstanceDTO> page = wsFlinkJobInstanceService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("getByCode")
    @ApiOperation(value = "查询任务实例", notes = "查询任务实例")
    public ResponseEntity<WsFlinkJobInstanceDTO> getByCode(@RequestParam("flinkJobCode") Long flinkJobCode) {
        WsFlinkJobInstanceDTO dto = wsFlinkJobInstanceService.selectByCode(flinkJobCode);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Logging
    @GetMapping("checkpoints")
    @ApiOperation(value = "查询任务 checkpoints", notes = "查询任务 checkpoints")
    public ResponseEntity<Page<WsFlinkCheckpointDTO>> checkpoints(@Valid WsFlinkCheckpointListParam param) {
        Page<WsFlinkCheckpointDTO> result = wsFlinkCheckpointService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping("submit")
    @ApiOperation(value = "提交任务", notes = "提交任务")
    public ResponseEntity<ResponseVO> submitJar(@Valid @RequestBody WsFlinkJobSubmitParam param) throws Exception {
        WsFlinkJobDTO job = wsFlinkJobService.selectOne(param.getFlinkJobId());
        wsFlinkJobInstanceService.archiveLog(job.getCode());
        job.setName(job.getName() + "_" + defaultUidGenerator.getUID());
        wsFlinkService.submit(job);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("stop/{id}")
    @ApiOperation(value = "终止任务", notes = "终止任务")
    public ResponseEntity<ResponseVO> stop(@PathVariable("id") Long id) throws Exception {
        wsFlinkService.stop(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("cancel/{id}")
    @ApiOperation(value = "取消任务", notes = "取消任务")
    public ResponseEntity<ResponseVO> cancel(@PathVariable("id") Long id) throws Exception {
        wsFlinkService.cancel(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("savepoint/{id}")
    @ApiOperation(value = "创建savepoint", notes = "创建savepoint")
    public ResponseEntity<ResponseVO> savepoint(@PathVariable("id") Long id) throws Exception {
        wsFlinkService.triggerSavepoint(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
