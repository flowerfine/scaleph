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
import cn.sliew.scaleph.engine.flink.service.FlinkJobInstanceService;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobInstanceDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobInstanceListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@Api(tags = "Flink管理-任务实例管理")
@RestController
@RequestMapping(path = "/api/flink/job-instance")
public class JobInstanceController {

    @Autowired
    private FlinkJobInstanceService flinkJobInstanceService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询任务实例列表", notes = "分页任务实例列表")
    public ResponseEntity<Page<FlinkJobInstanceDTO>> list(@Valid FlinkJobInstanceListParam param) {
        Page<FlinkJobInstanceDTO> page = flinkJobInstanceService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("getByCode")
    @ApiOperation(value = "查询任务实例", notes = "查询任务实例")
    public ResponseEntity<FlinkJobInstanceDTO> getByCode(@RequestParam("flinkJobCode") Long flinkJobCode) {
        FlinkJobInstanceDTO dto = flinkJobInstanceService.selectByCode(flinkJobCode);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
