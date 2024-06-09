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
import cn.sliew.scaleph.application.flink.resource.definition.sessioncluster.FlinkSessionCluster;
import cn.sliew.scaleph.application.flink.service.FlinkJobManagerEndpointService;
import cn.sliew.scaleph.application.flink.service.WsFlinkKubernetesSessionClusterService;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesSessionClusterDTO;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesSessionClusterListParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesSessionClusterSelectListParam;
import cn.sliew.scaleph.system.model.ResponseVO;
import cn.sliew.scaleph.system.snowflake.exception.UidGenerateException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
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

@Tag(name = "Flink Kubernetes管理-Flink SessionCluster管理")
@RestController
@RequestMapping(path = "/api/flink/kubernetes/session-cluster")
public class WsFlinkKubernetesSessionClusterController {

    @Autowired
    private WsFlinkKubernetesSessionClusterService wsFlinkKubernetesSessionClusterService;
    @Autowired
    private FlinkJobManagerEndpointService flinkJobManagerEndpointService;

    @Logging
    @GetMapping
    @Operation(summary = "查询 SessionCluster 列表", description = "分页查询 SessionCluster 列表")
    public ResponseEntity<Page<WsFlinkKubernetesSessionClusterDTO>> list(@Valid WsFlinkKubernetesSessionClusterListParam param) {
        Page<WsFlinkKubernetesSessionClusterDTO> page = wsFlinkKubernetesSessionClusterService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("all")
    @Operation(summary = "查询 SessionCluster 列表", description = "查询 SessionCluster 列表")
    public ResponseEntity<ResponseVO<List<WsFlinkKubernetesSessionClusterDTO>>> listAll(@Valid WsFlinkKubernetesSessionClusterSelectListParam param) {
        List<WsFlinkKubernetesSessionClusterDTO> list = wsFlinkKubernetesSessionClusterService.listAll(param);
        return new ResponseEntity<>(ResponseVO.success(list), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @Operation(summary = "查询 SessionCluster", description = "查询 SessionCluster")
    public ResponseEntity<ResponseVO<WsFlinkKubernetesSessionClusterDTO>> selectOne(@PathVariable("id") Long id) {
        WsFlinkKubernetesSessionClusterDTO dto = wsFlinkKubernetesSessionClusterService.selectOne(id);
        return new ResponseEntity(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @PostMapping("asYAML")
    @Operation(summary = "转换 SessionCluster", description = "转换 SessionCluster")
    public ResponseEntity<ResponseVO<FlinkSessionCluster>> asYAML(@RequestBody WsFlinkKubernetesSessionClusterDTO dto) {
        FlinkSessionCluster sessionCluster = wsFlinkKubernetesSessionClusterService.asYaml(dto);
        return new ResponseEntity(ResponseVO.success(sessionCluster), HttpStatus.OK);
    }

    @Logging
    @GetMapping("fromTemplate")
    @Operation(summary = "转换 SessionCluster", description = "转换 SessionCluster")
    public ResponseEntity<ResponseVO<WsFlinkKubernetesSessionClusterDTO>> fromTemplate(@RequestParam("templateId") Long templateId) {
        WsFlinkKubernetesSessionClusterDTO sessionCluster = wsFlinkKubernetesSessionClusterService.fromTemplate(templateId);
        return new ResponseEntity(ResponseVO.success(sessionCluster), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增 SessionCluster", description = "新增 SessionCluster")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody WsFlinkKubernetesSessionClusterDTO param) throws UidGenerateException {
        wsFlinkKubernetesSessionClusterService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改 SessionCluster", description = "修改 SessionCluster")
    public ResponseEntity<ResponseVO> update(@Valid @RequestBody WsFlinkKubernetesSessionClusterDTO param) {
        wsFlinkKubernetesSessionClusterService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("{id}/sql-gateway")
    @Operation(summary = "支持 Sql Gateway", description = "支持 Sql Gateway")
    public ResponseEntity<ResponseVO> enableSqlGateway(@PathVariable("id") Long id) {
        wsFlinkKubernetesSessionClusterService.enableSqlGateway(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}/sql-gateway")
    @Operation(summary = "禁用 Sql Gateway", description = "禁用 Sql Gateway")
    public ResponseEntity<ResponseVO> disableSqlGateway(@PathVariable("id") Long id) {
        wsFlinkKubernetesSessionClusterService.disableSqlGateway(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("sql-gateway-session-cluster-id")
    @Operation(summary = "查询Sql Gateway Session Cluster ID", description = "查询Sql Gateway Session Cluster ID")
    public ResponseEntity<String> getSqlGatewaySessionClusterId(@RequestParam("projectId") Long projectId) {
        Optional<WsFlinkKubernetesSessionClusterDTO> cluster = wsFlinkKubernetesSessionClusterService.getSqlGatewaySessionCluster(projectId);
        return cluster.map(dto -> ResponseEntity.ok(dto.getSessionClusterId())).orElseThrow();
    }

    @Logging
    @DeleteMapping("/{id}")
    @Operation(summary = "删除 SessionCluster", description = "删除 SessionCluster")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) {
        wsFlinkKubernetesSessionClusterService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除 SessionCluster", description = "批量删除 SessionCluster")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) {
        wsFlinkKubernetesSessionClusterService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("{id}/flinkui")
    @Operation(summary = "获取 flink-ui 链接", description = "获取 flink-ui 链接")
    public ResponseEntity<ResponseVO<URI>> getFlinkUI(@PathVariable("id") Long id) throws Exception {
        URI endpoint = flinkJobManagerEndpointService.getSessionClusterJobManagerEndpoint(id);
        return new ResponseEntity<>(ResponseVO.success(endpoint), HttpStatus.OK);
    }

    @Logging
    @PostMapping("deploy/{id}")
    @Operation(summary = "启动 SessionCluster", description = "启动 SessionCluster")
    public ResponseEntity<ResponseVO> deploySessionCluster(@PathVariable("id") Long id) throws Exception {
        wsFlinkKubernetesSessionClusterService.deploy(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("shutdown/{id}")
    @Operation(summary = "关闭 SessionCluster", description = "关闭 SessionCluster")
    public ResponseEntity<ResponseVO> shutdownSessionCluster(@PathVariable("id") Long id) throws Exception {
        wsFlinkKubernetesSessionClusterService.shutdown(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("{id}/status")
    @Operation(summary = "获取 SessionCluster 状态", description = "获取 SessionCluster 状态")
    public ResponseEntity<ResponseVO<GenericKubernetesResource>> getSessionClusterStatus(@PathVariable("id") Long id) throws Exception {
        Optional<GenericKubernetesResource> sessionCluster = wsFlinkKubernetesSessionClusterService.getStatusWithoutManagedFields(id);
        return new ResponseEntity<>(ResponseVO.success(sessionCluster.orElse(null)), HttpStatus.OK);
    }
}
