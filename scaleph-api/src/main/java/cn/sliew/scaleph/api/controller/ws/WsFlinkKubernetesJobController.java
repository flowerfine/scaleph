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
import cn.sliew.scaleph.engine.flink.kubernetes.service.FlinkJobManagerEndpointService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesJobInstanceService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesJobService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.*;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Tag(name = "Flink Kubernetes管理-Job管理")
@RestController
@RequestMapping(path = "/api/flink/kubernetes/job")
public class WsFlinkKubernetesJobController {

    @Autowired
    private WsFlinkKubernetesJobService wsFlinkKubernetesJobService;
    @Autowired
    private WsFlinkKubernetesJobInstanceService wsFlinkKubernetesJobInstanceService;
    @Autowired
    private FlinkJobManagerEndpointService flinkJobManagerEndpointService;

    @Logging
    @GetMapping
    @Operation(summary = "查询 Job 列表", description = "分页查询 Job 列表")
    public ResponseEntity<Page<WsFlinkKubernetesJobDTO>> list(@Valid WsFlinkKubernetesJobListParam param) {
        Page<WsFlinkKubernetesJobDTO> page = wsFlinkKubernetesJobService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @Operation(summary = "查询 Job", description = "查询 Job")
    public ResponseEntity<ResponseVO<WsFlinkKubernetesJobDTO>> selectOne(@PathVariable("id") Long id) {
        WsFlinkKubernetesJobDTO dto = wsFlinkKubernetesJobService.selectOne(id);
        return new ResponseEntity(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/asYaml/{id}")
    @Operation(summary = "查询 YAML 格式 Job", description = "查询 YAML 格式 Job")
    public ResponseEntity<ResponseVO<Object>> asYaml(@PathVariable("id") Long id) throws Exception {
        Object dto = wsFlinkKubernetesJobService.asYaml(id);
        return new ResponseEntity(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "创建 Job", description = "创建 Job")
    public ResponseEntity<ResponseVO> insert(@RequestBody @Valid WsFlinkKubernetesJobAddParam param) {
        wsFlinkKubernetesJobService.insert(param);
        return new ResponseEntity(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @Operation(summary = "更新 Job", description = "更新 Job")
    public ResponseEntity<ResponseVO> update(@RequestBody @Valid WsFlinkKubernetesJobUpdateParam param) {
        wsFlinkKubernetesJobService.update(param);
        return new ResponseEntity(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @Operation(summary = "删除 Job", description = "删除 Job")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) {
        wsFlinkKubernetesJobService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("batch")
    @Operation(summary = "批量删除 Job", description = "批量删除 Job")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) {
        wsFlinkKubernetesJobService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("{jobInstanceId}/flinkui")
    @Operation(summary = "获取 flink-ui 链接", description = "获取 flink-ui 链接")
    public ResponseEntity<ResponseVO<URI>> getFlinkUI(@PathVariable("jobInstanceId") Long jobInstanceId) throws Exception {
        URI endpoint = flinkJobManagerEndpointService.getJobManagerEndpoint(jobInstanceId);
        return new ResponseEntity<>(ResponseVO.success(endpoint), HttpStatus.OK);
    }

    @Logging
    @PostMapping("deploy")
    @Operation(summary = "启动 Job", description = "启动 Job")
    public ResponseEntity<ResponseVO> deploy(@Valid @RequestBody WsFlinkKubernetesJobInstanceDeployParam param) throws Exception {
        wsFlinkKubernetesJobInstanceService.deploy(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("shutdown")
    @Operation(summary = "关闭 Job", description = "关闭 Job")
    public ResponseEntity<ResponseVO> shutdown(@Valid @RequestBody WsFlinkKubernetesJobInstanceShutdownParam param) throws Exception {
        wsFlinkKubernetesJobInstanceService.shutdown(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("instances")
    @Operation(summary = "获取任务实例列表", description = "获取任务实例列表")
    public ResponseEntity<ResponseVO<Page<WsFlinkKubernetesJobInstanceDTO>>> listInstances(@Valid @RequestBody WsFlinkKubernetesJobInstanceListParam param) throws Exception {
        Page<WsFlinkKubernetesJobInstanceDTO> result = wsFlinkKubernetesJobInstanceService.list(param);
        return new ResponseEntity<>(ResponseVO.success(result), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/instances/asYaml/{id}")
    @Operation(summary = "查询 YAML 格式 Job 实例", description = "查询 YAML 格式 Job 实例")
    public ResponseEntity<ResponseVO<Object>> instanceAsYaml(@PathVariable("id") Long id) throws Exception {
        Object dto = wsFlinkKubernetesJobInstanceService.asYaml(id);
        return new ResponseEntity(ResponseVO.success(dto), HttpStatus.OK);
    }

}
