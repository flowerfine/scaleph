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

package cn.sliew.scaleph.api.controller.dataservice;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.dataservice.service.DataserviceParameterMapService;
import cn.sliew.scaleph.dataservice.service.dto.DataserviceParameterMapDTO;
import cn.sliew.scaleph.dataservice.service.dto.DataserviceParameterMappingDTO;
import cn.sliew.scaleph.dataservice.service.param.DataserviceParameterMapAddParam;
import cn.sliew.scaleph.dataservice.service.param.DataserviceParameterMapListParam;
import cn.sliew.scaleph.dataservice.service.param.DataserviceParameterMapUpdateParam;
import cn.sliew.scaleph.dataservice.service.param.DataserviceParameterMappingReplaceParam;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Tag(name = "数据服务-ParameterMap管理")
@RestController
@RequestMapping(path = "/api/dataservice/parameter-map")
public class DataserviceParameterMapController {

    @Autowired
    private DataserviceParameterMapService dataserviceParameterMapService;

    @Logging
    @GetMapping
    @Operation(summary = "查询 parameterMap 列表", description = "查询 parameterMap 列表")
    public ResponseEntity<ResponseVO<Page<DataserviceParameterMapDTO>>> get(@Valid DataserviceParameterMapListParam param) throws ParseException {
        Page<DataserviceParameterMapDTO> result = dataserviceParameterMapService.list(param);
        return new ResponseEntity<>(ResponseVO.success(result), HttpStatus.OK);
    }

    @Logging
    @GetMapping("{parameterMapId}/mappings")
    @Operation(summary = "查询 parameterMapping 列表", description = "查询 parameterMapping 列表")
    public ResponseEntity<ResponseVO<List<DataserviceParameterMappingDTO>>> listMappings(@PathVariable("parameterMapId") Long parameterMapId) throws ParseException {
        List<DataserviceParameterMappingDTO> result = dataserviceParameterMapService.listMappings(parameterMapId);
        return new ResponseEntity<>(ResponseVO.success(result), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增 parameterMap", description = "新增 parameterMap")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody DataserviceParameterMapAddParam param) throws ParseException {
        dataserviceParameterMapService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改 parameterMap", description = "修改 parameterMap")
    public ResponseEntity<ResponseVO> update(@Valid @RequestBody DataserviceParameterMapUpdateParam param) throws ParseException {
        dataserviceParameterMapService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("mappings/replace")
    @Operation(summary = "替换 parameterMapping", description = "替换 parameterMapping")
    public ResponseEntity<ResponseVO> replaceMappings(@Valid @RequestBody DataserviceParameterMappingReplaceParam param) throws ParseException {
        dataserviceParameterMapService.replaceMappings(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @Operation(summary = "删除 parameterMap", description = "删除 parameterMap")
    public ResponseEntity<ResponseVO> deleteById(@PathVariable("id") Long id) {
        dataserviceParameterMapService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @Operation(summary = "批量删除 parameterMap", description = "批量删除 parameterMap")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) {
        dataserviceParameterMapService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
