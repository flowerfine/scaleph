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
import cn.sliew.scaleph.engine.flink.kubernetes.operator.entity.DeploymentTemplate;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesDeploymentTemplateService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesDeploymentTemplateDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesDeploymentTemplateListParam;
import cn.sliew.scaleph.system.snowflake.exception.UidGenerateException;
import cn.sliew.scaleph.system.vo.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Flink Kubernetes管理-模板管理")
@RestController
@RequestMapping(path = "/api/flink/kubernetes/deployment-template")
public class WsFlinkKubernetesDeploymentTemplateController {

    @Autowired
    private WsFlinkKubernetesDeploymentTemplateService wsFlinkKubernetesDeploymentTemplateService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询 Deployment 模板列表", notes = "分页查询 Deployment 模板列表")
    public ResponseEntity<Page<WsFlinkKubernetesDeploymentTemplateDTO>> list(@Valid WsFlinkKubernetesDeploymentTemplateListParam param) {
        Page<WsFlinkKubernetesDeploymentTemplateDTO> page = wsFlinkKubernetesDeploymentTemplateService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @ApiOperation(value = "查询 Deployment 模板信息", notes = "查询 Deployment 模板信息")
    public ResponseEntity<ResponseVO<WsFlinkKubernetesDeploymentTemplateDTO>> selectOne(@PathVariable("id") Long id) {
        WsFlinkKubernetesDeploymentTemplateDTO dto = wsFlinkKubernetesDeploymentTemplateService.selectOne(id);
        return new ResponseEntity(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @PostMapping("asTemplate")
    @ApiOperation(value = "转换 Deployment 模板信息", notes = "转换 Deployment 模板信息")
    public ResponseEntity<ResponseVO<DeploymentTemplate>> asTemplate(@RequestBody WsFlinkKubernetesDeploymentTemplateDTO dto) {
        DeploymentTemplate template = wsFlinkKubernetesDeploymentTemplateService.asTemplate(dto);
        return new ResponseEntity(ResponseVO.success(template), HttpStatus.OK);
    }

    @Logging
    @PostMapping("asTemplateWithDefault")
    @ApiOperation(value = "转换 Deployment 模板和默认信息", notes = "转换 Deployment 模板和默认信息")
    public ResponseEntity<ResponseVO<DeploymentTemplate>> asTemplateWithDefault(@RequestBody WsFlinkKubernetesDeploymentTemplateDTO dto) {
        DeploymentTemplate template = wsFlinkKubernetesDeploymentTemplateService.asTemplateWithDefault(dto);
        return new ResponseEntity(ResponseVO.success(template), HttpStatus.OK);
    }

    @Logging
    @PatchMapping("default")
    @ApiOperation(value = "merge 默认 Deployment 模板信息", notes = "merge 默认 Deployment 模板信息")
    public ResponseEntity<ResponseVO<WsFlinkKubernetesDeploymentTemplateDTO>> mergeDefault(@RequestBody WsFlinkKubernetesDeploymentTemplateDTO template) {
        WsFlinkKubernetesDeploymentTemplateDTO dto = wsFlinkKubernetesDeploymentTemplateService.mergeDefault(template);
        return new ResponseEntity<ResponseVO<WsFlinkKubernetesDeploymentTemplateDTO>>(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "新增 Deployment 模板", notes = "新增 Deployment 模板")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody WsFlinkKubernetesDeploymentTemplateDTO param) throws UidGenerateException {
        wsFlinkKubernetesDeploymentTemplateService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "修改 Deployment 模板", notes = "修改 Deployment 模板")
    public ResponseEntity<ResponseVO> update(@Valid @RequestBody WsFlinkKubernetesDeploymentTemplateDTO param) {
        wsFlinkKubernetesDeploymentTemplateService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除 Deployment 模板", notes = "删除 Deployment 模板")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) {
        wsFlinkKubernetesDeploymentTemplateService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除 Deployment 模板", notes = "批量删除 Deployment 模板")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) {
        wsFlinkKubernetesDeploymentTemplateService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
