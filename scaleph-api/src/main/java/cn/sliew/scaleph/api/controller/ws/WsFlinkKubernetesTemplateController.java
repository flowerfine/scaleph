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
import cn.sliew.scaleph.application.flink.service.WsFlinkKubernetesTemplateService;
import cn.sliew.scaleph.application.flink.service.dto.FlinkImageOption;
import cn.sliew.scaleph.application.flink.service.dto.FlinkVersionOption;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesTemplateDTO;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesTemplateAddParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesTemplateListParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesTemplateUpdateParam;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.application.flink.resource.definition.template.FlinkTemplate;
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

@Tag(name = "Flink Kubernetes管理-模板管理")
@RestController
@RequestMapping(path = "/api/flink/kubernetes/template")
public class WsFlinkKubernetesTemplateController {

    @Autowired
    private WsFlinkKubernetesTemplateService wsFlinkKubernetesTemplateService;

    @Logging
    @GetMapping
    @Operation(summary = "查询模板列表", description = "分页查询模板列表")
    public ResponseEntity<Page<WsFlinkKubernetesTemplateDTO>> list(@Valid WsFlinkKubernetesTemplateListParam param) {
        Page<WsFlinkKubernetesTemplateDTO> page = wsFlinkKubernetesTemplateService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @Operation(summary = "查询模板信息", description = "查询模板信息")
    public ResponseEntity<ResponseVO<WsFlinkKubernetesTemplateDTO>> selectOne(@PathVariable("id") Long id) {
        WsFlinkKubernetesTemplateDTO dto = wsFlinkKubernetesTemplateService.selectOne(id);
        return new ResponseEntity(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/flinkVersionMappings")
    @Operation(summary = "查询 Flink 版本映射", description = "查询 Flink 版本映射")
    public ResponseEntity<ResponseVO<List<FlinkVersionOption>>> getFlinkVersionOptions() {
        List<FlinkVersionOption> options = wsFlinkKubernetesTemplateService.getFlinkVersionOptions();
        return new ResponseEntity(ResponseVO.success(options), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/flinkImageMappings")
    @Operation(summary = "查询 Flink 镜像映射", description = "查询 Flink 镜像映射")
    public ResponseEntity<ResponseVO<List<FlinkImageOption>>> getFlinkVersionOptions(@RequestParam("flinkVersion") FlinkVersion flinkVersion) {
        List<FlinkImageOption> options = wsFlinkKubernetesTemplateService.getFlinkImageOptions(flinkVersion);
        return new ResponseEntity(ResponseVO.success(options), HttpStatus.OK);
    }

    @Logging
    @PostMapping("asYaml")
    @Operation(summary = "转换模板信息", description = "转换模板信息")
    public ResponseEntity<ResponseVO<FlinkTemplate>> asYaml(@RequestBody WsFlinkKubernetesTemplateDTO dto) {
        FlinkTemplate template = wsFlinkKubernetesTemplateService.asYaml(dto);
        return new ResponseEntity(ResponseVO.success(template), HttpStatus.OK);
    }

    @Logging
    @PostMapping("asYamlWithDefault")
    @Operation(summary = "转换模板和默认信息", description = "转换模板和默认信息")
    public ResponseEntity<ResponseVO<FlinkTemplate>> asYamlWithDefault(@RequestBody WsFlinkKubernetesTemplateDTO dto) {
        FlinkTemplate template = wsFlinkKubernetesTemplateService.asYamlWithDefault(dto);
        return new ResponseEntity(ResponseVO.success(template), HttpStatus.OK);
    }

    @Logging
    @PatchMapping("default")
    @Operation(summary = "merge 默认模板信息", description = "merge 默认模板信息")
    public ResponseEntity<ResponseVO<WsFlinkKubernetesTemplateDTO>> mergeDefault(@RequestBody WsFlinkKubernetesTemplateDTO template) {
        WsFlinkKubernetesTemplateDTO dto = wsFlinkKubernetesTemplateService.mergeDefault(template);
        return new ResponseEntity<ResponseVO<WsFlinkKubernetesTemplateDTO>>(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增模板", description = "新增模板")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody WsFlinkKubernetesTemplateAddParam param) throws UidGenerateException {
        wsFlinkKubernetesTemplateService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改模板", description = "修改模板")
    public ResponseEntity<ResponseVO> update(@Valid @RequestBody WsFlinkKubernetesTemplateUpdateParam param) {
        wsFlinkKubernetesTemplateService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("template")
    @Operation(summary = "修改模板内容", description = "修改模板内容")
    public ResponseEntity<ResponseVO> updateTemplate(@Valid @RequestBody WsFlinkKubernetesTemplateDTO param) {
        wsFlinkKubernetesTemplateService.updateTemplate(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/{id}")
    @Operation(summary = "删除模板", description = "删除模板")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) {
        wsFlinkKubernetesTemplateService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除模板", description = "批量删除模板")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) {
        wsFlinkKubernetesTemplateService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
