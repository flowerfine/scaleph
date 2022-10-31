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
import cn.sliew.scaleph.engine.flink.service.FlinkYarnService;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobForSeaTunnelSubmitParam;
import cn.sliew.scaleph.system.vo.ResponseVO;
import cn.sliew.scaleph.engine.flink.service.FlinkService;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobForJarSubmitParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "Flink管理-Jar 任务实例")
@RestController
@RequestMapping(path = "/api/flink/job-instance/jar")
public class JobInstanceJarController {

    @Autowired
    private FlinkService flinkService;
    @Autowired
    private FlinkYarnService flinkYarnService;

    @Logging
    @PutMapping
    @ApiOperation(value = "提交 jar 任务", notes = "提交 jar 任务")
    public ResponseEntity<ResponseVO> submitJar(@Valid @RequestBody FlinkJobForJarSubmitParam param) throws Exception {
        flinkService.submitJar(param.getFlinkJobId());
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PutMapping("seatunnel")
    @ApiOperation(value = "提交 seatunnel 任务", notes = "提交 seatunnel 任务")
    public ResponseEntity<ResponseVO> submitSeaTunnel(@Valid @RequestBody FlinkJobForSeaTunnelSubmitParam param) throws Exception {
        flinkService.submitSeaTunnel(param.getFlinkJobId());
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
