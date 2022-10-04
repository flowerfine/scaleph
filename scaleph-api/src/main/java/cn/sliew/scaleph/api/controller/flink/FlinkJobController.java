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

package cn.sliew.scaleph.api.controller.flink;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.system.vo.ResponseVO;
import cn.sliew.scaleph.engine.flink.service.FlinkJobService;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobDTO;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobForJarDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobListByCodeParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobListByTypeParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Api(tags = "Flink管理-任务管理")
@RestController
@RequestMapping(path = "/api/flink/job")
public class FlinkJobController {

    @Autowired
    private FlinkJobService flinkJobService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询任务列表", notes = "分页查询任务列表")
    public ResponseEntity<Page<FlinkJobDTO>> list(@Valid FlinkJobListParam param) {
        Page<FlinkJobDTO> page = flinkJobService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("version")
    @ApiOperation(value = "查询任务版本列表", notes = "分页查询任务版本列表")
    public ResponseEntity<Page<FlinkJobDTO>> list(@Valid FlinkJobListByCodeParam param) {
        Page<FlinkJobDTO> page = flinkJobService.listByCode(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "新增任务", notes = "新增任务")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody FlinkJobDTO param) {
        flinkJobService.insert(param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("{code}")
    @ApiOperation(value = "修改任务", notes = "修改任务")
    public ResponseEntity<ResponseVO> update(@PathVariable("code") Long code, @Valid @RequestBody FlinkJobDTO param) {
        param.setCode(code);
        flinkJobService.update(param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("jar")
    @ApiOperation(value = "查询 Jar 任务列表", notes = "分页查询 Jar 任务列表")
    public ResponseEntity<Page<FlinkJobForJarDTO>> listJobsForJar(@Valid FlinkJobListByTypeParam param) {
        Page<FlinkJobForJarDTO> page = flinkJobService.listJobsForJar(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

}
