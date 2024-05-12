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
import cn.sliew.scaleph.dataservice.service.DataserviceResultMapService;
import cn.sliew.scaleph.dataservice.service.dto.DataserviceResultMapDTO;
import cn.sliew.scaleph.dataservice.service.dto.DataserviceResultMappingDTO;
import cn.sliew.scaleph.dataservice.service.param.DataserviceResultMapAddParam;
import cn.sliew.scaleph.dataservice.service.param.DataserviceResultMapListParam;
import cn.sliew.scaleph.dataservice.service.param.DataserviceResultMapUpdateParam;
import cn.sliew.scaleph.dataservice.service.param.DataserviceResultMappingReplaceParam;
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

@Tag(name = "数据服务-ResultMap管理")
@RestController
@RequestMapping(path = "/api/dataservice/result-map")
public class DataserviceResultMapController {

    @Autowired
    private DataserviceResultMapService dataserviceResultMapService;

    @Logging
    @GetMapping
    @Operation(summary = "查询 resultMap 列表", description = "查询 resultMap 列表")
    public ResponseEntity<ResponseVO<Page<DataserviceResultMapDTO>>> get(@Valid DataserviceResultMapListParam param) throws ParseException {
        Page<DataserviceResultMapDTO> result = dataserviceResultMapService.list(param);
        return new ResponseEntity<>(ResponseVO.success(result), HttpStatus.OK);
    }

    @Logging
    @GetMapping("{resultMapId}/mappings")
    @Operation(summary = "查询 resultMapping 列表", description = "查询 resultMapping 列表")
    public ResponseEntity<ResponseVO<List<DataserviceResultMappingDTO>>> listMappings(@PathVariable("resultMapId") Long resultMapId) {
        List<DataserviceResultMappingDTO> result = dataserviceResultMapService.listMappings(resultMapId);
        return new ResponseEntity<>(ResponseVO.success(result), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增 resultMap", description = "新增 resultMap")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody DataserviceResultMapAddParam param) throws ParseException {
        dataserviceResultMapService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改 resultMap", description = "修改 resultMap")
    public ResponseEntity<ResponseVO> update(@Valid @RequestBody DataserviceResultMapUpdateParam param) throws ParseException {
        dataserviceResultMapService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("mappings/replace")
    @Operation(summary = "替换 resultMapping", description = "替换 resultMapping")
    public ResponseEntity<ResponseVO> replaceMappings(@Valid @RequestBody DataserviceResultMappingReplaceParam param) {
        dataserviceResultMapService.replaceMappings(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @Operation(summary = "删除 resultMap", description = "删除 resultMap")
    public ResponseEntity<ResponseVO> deleteById(@PathVariable("id") Long id) {
        dataserviceResultMapService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @Operation(summary = "批量删除 resultMap", description = "批量删除 resultMap")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) {
        dataserviceResultMapService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
