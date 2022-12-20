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
import cn.sliew.scaleph.engine.flink.service.WsFlinkCheckpointService;
import cn.sliew.scaleph.engine.flink.service.WsFlinkJobInstanceService;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkCheckpointDTO;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkJobInstanceDTO;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkCheckpointListParam;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkJobInstanceListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "Flink管理-任务实例管理")
@RestController
@RequestMapping(path = "/api/flink/job-instance")
public class WsFlinkJobInstanceController {

    @Autowired
    private WsFlinkJobInstanceService wsFlinkJobInstanceService;
    @Autowired
    private WsFlinkCheckpointService wsFlinkCheckpointService;

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

}
