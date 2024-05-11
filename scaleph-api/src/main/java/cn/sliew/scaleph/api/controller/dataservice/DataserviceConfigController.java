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
import cn.sliew.scaleph.dataservice.service.DataserviceConfigService;
import cn.sliew.scaleph.dataservice.service.dto.DataserviceConfigDTO;
import cn.sliew.scaleph.dataservice.service.param.DataserviceConfigListParam;
import cn.sliew.scaleph.dataservice.service.param.DataserviceConfigSaveParam;
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

@Tag(name = "数据服务-配置管理")
@RestController
@RequestMapping(path = "/api/dataservice/config")
public class DataserviceConfigController {

    @Autowired
    private DataserviceConfigService dataserviceConfigService;

    @Logging
    @GetMapping
    @Operation(summary = "查询 config 列表", description = "查询 config 列表")
    public ResponseEntity<Page<DataserviceConfigDTO>> get(@Valid DataserviceConfigListParam param) throws ParseException {
        Page<DataserviceConfigDTO> result = dataserviceConfigService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增 config", description = "新增 config")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody DataserviceConfigSaveParam param) throws ParseException {
        dataserviceConfigService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("{id}")
    @Operation(summary = "修改 config", description = "修改 config")
    public ResponseEntity<ResponseVO> update(@PathVariable("id") Long id, @Valid @RequestBody DataserviceConfigSaveParam param) throws ParseException {
        param.setId(id);
        dataserviceConfigService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @Operation(summary = "删除 config", description = "删除 config")
    public ResponseEntity<ResponseVO> deleteById(@PathVariable("id") Long id) {
        dataserviceConfigService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @Operation(summary = "批量删除 config", description = "批量删除 config")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) {
        dataserviceConfigService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
