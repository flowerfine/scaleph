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
import cn.sliew.scaleph.application.doris.service.WsDorisOperatorTemplateService;
import cn.sliew.scaleph.application.doris.service.dto.WsDorisOperatorTemplateDTO;
import cn.sliew.scaleph.application.doris.service.param.WsDorisOperatorTemplateAddParam;
import cn.sliew.scaleph.application.doris.service.param.WsDorisOperatorTemplateListParam;
import cn.sliew.scaleph.application.doris.service.param.WsDorisOperatorTemplateUpdateParam;
import cn.sliew.scaleph.application.doris.service.resource.template.DorisTemplate;
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

@Tag(name = "Doris管理-Operator模板管理")
@RestController
@RequestMapping(path = "/api/doris/operator/template")
public class WsDorisOperatorTemplateController {

    @Autowired
    private WsDorisOperatorTemplateService wsDorisTemplateService;

    @Logging
    @GetMapping
    @Operation(summary = "查询模板列表", description = "分页查询模板列表")
    public ResponseEntity<Page<WsDorisOperatorTemplateDTO>> list(@Valid WsDorisOperatorTemplateListParam param) {
        Page<WsDorisOperatorTemplateDTO> page = wsDorisTemplateService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @Operation(summary = "查询模板信息", description = "查询模板信息")
    public ResponseEntity<ResponseVO<WsDorisOperatorTemplateDTO>> selectOne(@PathVariable("id") Long id) {
        WsDorisOperatorTemplateDTO dto = wsDorisTemplateService.selectOne(id);
        return new ResponseEntity(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @PostMapping("asYaml")
    @Operation(summary = "转换模板信息", description = "转换模板信息")
    public ResponseEntity<ResponseVO<DorisTemplate>> asYaml(@RequestBody WsDorisOperatorTemplateDTO dto) {
        DorisTemplate template = wsDorisTemplateService.asYaml(dto);
        return new ResponseEntity(ResponseVO.success(template), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增模板", description = "新增模板")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody WsDorisOperatorTemplateAddParam param) throws UidGenerateException {
        wsDorisTemplateService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改模板", description = "修改模板")
    public ResponseEntity<ResponseVO> update(@Valid @RequestBody WsDorisOperatorTemplateUpdateParam param) {
        wsDorisTemplateService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/{id}")
    @Operation(summary = "删除模板", description = "删除模板")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) {
        wsDorisTemplateService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除模板", description = "批量删除模板")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) {
        wsDorisTemplateService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
