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

import cn.sliew.scaleph.api.annotation.AnonymousAccess;
import cn.sliew.scaleph.engine.flink.service.FlinkKubernetesService;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobListParam;
import cn.sliew.scaleph.system.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "Flink管理-operator")
@RestController
@RequestMapping(path = "/api/flink/operator")
public class FlinkController {

    @Autowired
    private FlinkKubernetesService flinkKubernetesService;

    @AnonymousAccess
    @PostMapping("session")
    @ApiOperation(value = "创建 session 集群", notes = "创建 session 集群")
    public ResponseEntity<ResponseVO> createSession(@Valid FlinkJobListParam param) throws Exception {
        flinkKubernetesService.createSession();
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
