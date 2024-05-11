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
import cn.sliew.scaleph.application.doris.operator.DorisCluster;
import cn.sliew.scaleph.application.doris.service.DorisClusterEndpointService;
import cn.sliew.scaleph.application.doris.service.WsDorisOperatorInstanceService;
import cn.sliew.scaleph.application.doris.service.dto.DorisClusterFeEndpoint;
import cn.sliew.scaleph.application.doris.service.dto.WsDorisOperatorInstanceDTO;
import cn.sliew.scaleph.application.doris.service.param.WsDorisOperatorInstanceAddParam;
import cn.sliew.scaleph.application.doris.service.param.WsDorisOperatorInstanceListParam;
import cn.sliew.scaleph.application.doris.service.param.WsDorisOperatorInstanceUpdateParam;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Doris管理-Operator实例管理")
@RestController
@RequestMapping(path = "/api/doris/operator/instance")
public class WsDorisOperatorInstanceController {

    @Autowired
    private WsDorisOperatorInstanceService wsDorisInstanceService;
    @Autowired
    private DorisClusterEndpointService dorisClusterEndpointService;

    @Logging
    @GetMapping
    @Operation(summary = "查询实例列表", description = "分页查询实例列表")
    public ResponseEntity<Page<WsDorisOperatorInstanceDTO>> list(@Valid WsDorisOperatorInstanceListParam param) {
        Page<WsDorisOperatorInstanceDTO> page = wsDorisInstanceService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @Operation(summary = "查询实例信息", description = "查询实例信息")
    public ResponseEntity<ResponseVO<WsDorisOperatorInstanceDTO>> selectOne(@PathVariable("id") Long id) {
        WsDorisOperatorInstanceDTO dto = wsDorisInstanceService.selectOne(id);
        return new ResponseEntity(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @GetMapping("fromTemplate")
    @Operation(summary = "根据模板创建实例", description = "根据模板创建实例")
    public ResponseEntity<ResponseVO<WsDorisOperatorInstanceDTO>> fromTemplate(@RequestParam("templateId") Long templateId) {
        WsDorisOperatorInstanceDTO cluster = wsDorisInstanceService.fromTemplate(templateId);
        return new ResponseEntity(ResponseVO.success(cluster), HttpStatus.OK);
    }

    @Logging
    @PostMapping("asYaml")
    @Operation(summary = "转换实例信息", description = "转换实例信息")
    public ResponseEntity<ResponseVO<DorisCluster>> asYaml(@RequestBody WsDorisOperatorInstanceDTO dto) {
        DorisCluster cluster = wsDorisInstanceService.asYaml(dto);
        return new ResponseEntity(ResponseVO.success(cluster), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增实例", description = "新增实例")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody WsDorisOperatorInstanceAddParam param) {
        wsDorisInstanceService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改实例", description = "修改实例")
    public ResponseEntity<ResponseVO> update(@Valid @RequestBody WsDorisOperatorInstanceUpdateParam param) {
        wsDorisInstanceService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/{id}")
    @Operation(summary = "删除实例", description = "删除实例")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) {
        wsDorisInstanceService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除实例", description = "批量删除实例")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) {
        wsDorisInstanceService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PutMapping("deploy/{id}")
    @Operation(summary = "启动实例", description = "启动实例")
    public ResponseEntity<ResponseVO> deploy(@PathVariable("id") Long id) {
        wsDorisInstanceService.deploy(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("shutdown/{id}")
    @Operation(summary = "关闭实例", description = "关闭实例")
    public ResponseEntity<ResponseVO> shutdown(@PathVariable("id") Long id) {
        wsDorisInstanceService.shutdown(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("status/{id}")
    @Operation(summary = "获取实例状态", description = "获取实例状态")
    public ResponseEntity<ResponseVO<GenericKubernetesResource>> getStatus(@PathVariable("id") Long id) {
        Optional<GenericKubernetesResource> sessionCluster = wsDorisInstanceService.getStatusWithoutManagedFields(id);
        return new ResponseEntity<>(ResponseVO.success(sessionCluster.orElse(null)), HttpStatus.OK);
    }

    @Logging
    @GetMapping("endpoint/fe/{id}")
    @Operation(summary = "获取 FE endpoint", description = "获取 FE endpoint")
    public ResponseEntity<ResponseVO<DorisClusterFeEndpoint>> getFlinkUI(@PathVariable("id") Long id) {
        DorisClusterFeEndpoint feEndpoint = dorisClusterEndpointService.getFEEndpoint(id);
        return new ResponseEntity<>(ResponseVO.success(feEndpoint), HttpStatus.OK);
    }

}
