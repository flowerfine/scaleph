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
import cn.sliew.scaleph.engine.flink.service.WsFlinkJobLogService;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkJobLogDTO;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkJobLogListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Flink管理-任务实例管理")
@RestController
@RequestMapping(path = "/api/flink/job-log")
public class WsFlinkJobLogController {

    @Autowired
    private WsFlinkJobLogService wsFlinkJobLogService;

    @Logging
    @GetMapping
    @Operation(summary = "查询任务运行日志列表", description = "分页任务运行日志列表")
    public ResponseEntity<Page<WsFlinkJobLogDTO>> list(@Valid WsFlinkJobLogListParam param) {
        Page<WsFlinkJobLogDTO> page = wsFlinkJobLogService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
