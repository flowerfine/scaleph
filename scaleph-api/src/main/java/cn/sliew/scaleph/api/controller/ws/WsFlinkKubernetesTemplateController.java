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
import cn.sliew.scaleph.engine.flink.kubernetes.resource.template.FlinkTemplate;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesTemplateService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesTemplateDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesTemplateListParam;
import cn.sliew.scaleph.system.snowflake.exception.UidGenerateException;
import cn.sliew.scaleph.system.model.ResponseVO;
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
@RequestMapping(path = "/api/flink/kubernetes/template")
public class WsFlinkKubernetesTemplateController {

    @Autowired
    private WsFlinkKubernetesTemplateService wsFlinkKubernetesTemplateService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询模板列表", notes = "分页查询模板列表")
    public ResponseEntity<Page<WsFlinkKubernetesTemplateDTO>> list(@Valid WsFlinkKubernetesTemplateListParam param) {
        Page<WsFlinkKubernetesTemplateDTO> page = wsFlinkKubernetesTemplateService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @ApiOperation(value = "查询模板信息", notes = "查询模板信息")
    public ResponseEntity<ResponseVO<WsFlinkKubernetesTemplateDTO>> selectOne(@PathVariable("id") Long id) {
        WsFlinkKubernetesTemplateDTO dto = wsFlinkKubernetesTemplateService.selectOne(id);
        return new ResponseEntity(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @PostMapping("asTemplate")
    @ApiOperation(value = "转换模板信息", notes = "转换模板信息")
    public ResponseEntity<ResponseVO<FlinkTemplate>> asTemplate(@RequestBody WsFlinkKubernetesTemplateDTO dto) {
        FlinkTemplate template = wsFlinkKubernetesTemplateService.asTemplate(dto);
        return new ResponseEntity(ResponseVO.success(template), HttpStatus.OK);
    }

    @Logging
    @PostMapping("asTemplateWithDefault")
    @ApiOperation(value = "转换模板和默认信息", notes = "转换模板和默认信息")
    public ResponseEntity<ResponseVO<FlinkTemplate>> asTemplateWithDefault(@RequestBody WsFlinkKubernetesTemplateDTO dto) {
        FlinkTemplate template = wsFlinkKubernetesTemplateService.asTemplateWithDefault(dto);
        return new ResponseEntity(ResponseVO.success(template), HttpStatus.OK);
    }

    @Logging
    @PatchMapping("default")
    @ApiOperation(value = "merge 默认模板信息", notes = "merge 默认模板信息")
    public ResponseEntity<ResponseVO<WsFlinkKubernetesTemplateDTO>> mergeDefault(@RequestBody WsFlinkKubernetesTemplateDTO template) {
        WsFlinkKubernetesTemplateDTO dto = wsFlinkKubernetesTemplateService.mergeDefault(template);
        return new ResponseEntity<ResponseVO<WsFlinkKubernetesTemplateDTO>>(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "新增模板", notes = "新增模板")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody WsFlinkKubernetesTemplateDTO param) throws UidGenerateException {
        wsFlinkKubernetesTemplateService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "修改模板", notes = "修改模板")
    public ResponseEntity<ResponseVO> update(@Valid @RequestBody WsFlinkKubernetesTemplateDTO param) {
        wsFlinkKubernetesTemplateService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除模板", notes = "删除模板")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) {
        wsFlinkKubernetesTemplateService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除模板", notes = "批量删除模板")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) {
        wsFlinkKubernetesTemplateService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
