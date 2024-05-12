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
import cn.sliew.scaleph.application.flink.resource.definition.deployment.FlinkDeployment;
import cn.sliew.scaleph.application.flink.resource.definition.sessioncluster.FlinkSessionCluster;
import cn.sliew.scaleph.application.flink.service.WsFlinkKubernetesDeploymentService;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesDeploymentDTO;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesDeploymentListParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesDeploymentSelectListParam;
import cn.sliew.scaleph.system.model.ResponseVO;
import cn.sliew.scaleph.system.snowflake.exception.UidGenerateException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Flink Kubernetes管理-FlinkDeployment管理")
@RestController
@RequestMapping(path = "/api/flink/kubernetes/deployment")
public class WsFlinkKubernetesDeploymentController {

    @Autowired
    private WsFlinkKubernetesDeploymentService wsFlinkKubernetesDeploymentService;

    @Logging
    @GetMapping
    @Operation(summary = "查询 Deployment 列表", description = "分页查询 Deployment 列表")
    public ResponseEntity<Page<WsFlinkKubernetesDeploymentDTO>> list(@Valid WsFlinkKubernetesDeploymentListParam param) {
        Page<WsFlinkKubernetesDeploymentDTO> page = wsFlinkKubernetesDeploymentService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("all")
    @Operation(summary = "查询 Deployment 列表", description = "查询 Deployment 列表")
    public ResponseEntity<ResponseVO<List<WsFlinkKubernetesDeploymentDTO>>> listAll(@Valid WsFlinkKubernetesDeploymentSelectListParam param) {
        List<WsFlinkKubernetesDeploymentDTO> list = wsFlinkKubernetesDeploymentService.listAll(param);
        return new ResponseEntity<>(ResponseVO.success(list), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @Operation(summary = "查询 Deployment", description = "查询 Deployment")
    public ResponseEntity<ResponseVO<WsFlinkKubernetesDeploymentDTO>> selectOne(@PathVariable("id") Long id) {
        WsFlinkKubernetesDeploymentDTO dto = wsFlinkKubernetesDeploymentService.selectOne(id);
        return new ResponseEntity(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/asYaml/{id}")
    @Operation(summary = "查询 YAML 格式 Deployment", description = "查询 YAML 格式 Deployment")
    public ResponseEntity<ResponseVO<FlinkDeployment>> asYaml(@PathVariable("id") Long id) {
        FlinkDeployment dto = wsFlinkKubernetesDeploymentService.asYaml(id);
        return new ResponseEntity(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @PostMapping("asYAML")
    @Operation(summary = "转换 Deployment", description = "转换 Deployment")
    public ResponseEntity<ResponseVO<FlinkSessionCluster>> asYAML(@RequestBody WsFlinkKubernetesDeploymentDTO dto) {
        FlinkDeployment deployment = wsFlinkKubernetesDeploymentService.asYAML(dto);
        return new ResponseEntity(ResponseVO.success(deployment), HttpStatus.OK);
    }

    @Logging
    @GetMapping("fromTemplate")
    @Operation(summary = "转换 Deployment", description = "转换 Deployment")
    public ResponseEntity<ResponseVO<WsFlinkKubernetesDeploymentDTO>> fromTemplate(@RequestParam("templateId") Long templateId) {
        WsFlinkKubernetesDeploymentDTO deployment = wsFlinkKubernetesDeploymentService.fromTemplate(templateId);
        return new ResponseEntity(ResponseVO.success(deployment), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增 Deployment", description = "新增 Deployment")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody WsFlinkKubernetesDeploymentDTO param) throws UidGenerateException {
        wsFlinkKubernetesDeploymentService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改 Deployment", description = "修改 Deployment")
    public ResponseEntity<ResponseVO> update(@Valid @RequestBody WsFlinkKubernetesDeploymentDTO param) {
        wsFlinkKubernetesDeploymentService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/{id}")
    @Operation(summary = "删除 Deployment", description = "删除 Deployment")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) {
        wsFlinkKubernetesDeploymentService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除 Deployment", description = "批量删除 Deployment")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) {
        wsFlinkKubernetesDeploymentService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
