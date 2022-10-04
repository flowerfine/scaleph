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
import cn.sliew.scaleph.engine.flink.service.FlinkClusterInstanceService;
import cn.sliew.scaleph.engine.flink.service.FlinkService;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkClusterInstanceDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkClusterInstanceListParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkSessionClusterAddParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Api(tags = "Flink管理-集群实例管理")
@RestController
@RequestMapping(path = "/api/flink/cluster-instance")
public class ClusterInstanceController {

    @Autowired
    private FlinkClusterInstanceService flinkClusterInstanceService;
    @Autowired
    private FlinkService flinkService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询集群实例列表", notes = "分页查询集群实例列表")
    public ResponseEntity<Page<FlinkClusterInstanceDTO>> list(@Valid FlinkClusterInstanceListParam param) {
        Page<FlinkClusterInstanceDTO> page = flinkClusterInstanceService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "创建 session 集群", notes = "创建 session 集群")
    public ResponseEntity<ResponseVO> createSessionCluster(@Valid @RequestBody FlinkSessionClusterAddParam param) throws Exception {
        flinkService.createSessionCluster(param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @ApiOperation(value = "关闭集群", notes = "关闭集群")
    public ResponseEntity<ResponseVO> shutdownCluster(@PathVariable("id") Long id) throws Exception {
        flinkService.shutdown(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @ApiOperation(value = "批量关闭集群", notes = "批量关闭集群")
    public ResponseEntity<ResponseVO> shutdownClusterBatch(@RequestBody List<Long> ids) throws Exception {
        flinkService.shutdownBatch(ids);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

}
