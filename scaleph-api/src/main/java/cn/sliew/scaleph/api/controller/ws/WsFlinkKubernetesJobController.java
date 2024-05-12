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
import cn.sliew.scaleph.application.flink.service.FlinkJobManagerEndpointService;
import cn.sliew.scaleph.application.flink.service.WsFlinkKubernetesJobInstanceService;
import cn.sliew.scaleph.application.flink.service.WsFlinkKubernetesJobService;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceSavepointDTO;
import cn.sliew.scaleph.application.flink.service.param.*;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<ResponseVO<String>> asYaml(@PathVariable("id") Long id) {
        String yaml = wsFlinkKubernetesJobInstanceService.mockYaml(id);
        return new ResponseEntity(ResponseVO.success(yaml), HttpStatus.OK);
    }

    @Logging
    @PutMapping("/")
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
    public ResponseEntity<ResponseVO<URI>> getFlinkUI(@PathVariable("jobInstanceId") Long jobInstanceId) {
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
    @PostMapping("restart/{id}")
    @Operation(summary = "重启 Job", description = "重启 Job")
    public ResponseEntity<ResponseVO> restart(@PathVariable("id") Long id) throws Exception {
        wsFlinkKubernetesJobInstanceService.restart(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("triggerSavepoint/{id}")
    @Operation(summary = "为 Job 创建 savepoint", description = "为 Job 创建 savepoint")
    public ResponseEntity<ResponseVO> triggerSavepoint(@PathVariable("id") Long id) throws Exception {
        wsFlinkKubernetesJobInstanceService.triggerSavepoint(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("suspend/{id}")
    @Operation(summary = "暂停 Job", description = "暂停 Job")
    public ResponseEntity<ResponseVO> suspend(@PathVariable("id") Long id) throws Exception {
        wsFlinkKubernetesJobInstanceService.suspend(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("resume/{id}")
    @Operation(summary = "恢复 Job", description = "恢复 Job")
    public ResponseEntity<ResponseVO> resume(@PathVariable("id") Long id) throws Exception {
        wsFlinkKubernetesJobInstanceService.resume(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("instances")
    @Operation(summary = "获取任务实例列表", description = "获取任务实例列表")
    public ResponseEntity<Page<WsFlinkKubernetesJobInstanceDTO>> listInstances(@Valid WsFlinkKubernetesJobInstanceListParam param) {
        Page<WsFlinkKubernetesJobInstanceDTO> result = wsFlinkKubernetesJobInstanceService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("instances/current")
    @Operation(summary = "获取任务当前实例", description = "获取任务当前实例")
    public ResponseEntity<ResponseVO<WsFlinkKubernetesJobInstanceDTO>> currentInstance(@RequestParam("wsFlinkKubernetesJobId") Long wsFlinkKubernetesJobId) {
        Optional<WsFlinkKubernetesJobInstanceDTO> optional = wsFlinkKubernetesJobInstanceService.selectCurrent(wsFlinkKubernetesJobId);
        if (optional.isPresent()) {
            return new ResponseEntity<>(ResponseVO.success(optional.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseVO.error("not found"), HttpStatus.NOT_FOUND);
    }

    @Logging
    @GetMapping("/instances/asYaml/{id}")
    @Operation(summary = "查询 YAML 格式 Job 实例", description = "查询 YAML 格式 Job 实例")
    public ResponseEntity<ResponseVO<Object>> instanceAsYaml(@PathVariable("id") Long id) throws Exception {
        Object dto = wsFlinkKubernetesJobInstanceService.asYaml(id);
        return new ResponseEntity(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/instances/status/asYaml/{id}")
    @Operation(summary = "查询 YAML 格式 Job 状态", description = "查询 YAML 格式 Job 状态")
    public ResponseEntity<ResponseVO<String>> instanceStatusAsYaml(@PathVariable("id") Long id) {
        Optional<GenericKubernetesResource> optional = wsFlinkKubernetesJobInstanceService.getStatusWithoutManagedFields(id);
        if (optional.isPresent()) {
            return new ResponseEntity(ResponseVO.success(Serialization.asYaml(optional.get())), HttpStatus.OK);
        }
        return new ResponseEntity(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/instances/savepoint")
    @Operation(summary = "查询 Job 实例 savepoint", description = "查询 Job 实例 savepoint")
    public ResponseEntity<Page<WsFlinkKubernetesJobInstanceSavepointDTO>> getSavepoint(@Valid WsFlinkKubernetesJobInstanceSavepointListParam param) {
        Page<WsFlinkKubernetesJobInstanceSavepointDTO> result = wsFlinkKubernetesJobInstanceService.selectSavepoint(param);
        return new ResponseEntity(result, HttpStatus.OK);
    }

}
