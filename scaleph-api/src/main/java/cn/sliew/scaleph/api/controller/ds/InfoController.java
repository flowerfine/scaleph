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

package cn.sliew.scaleph.api.controller.ds;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.common.dict.job.DataSourceType;
import cn.sliew.scaleph.ds.modal.AbstractDataSource;
import cn.sliew.scaleph.ds.service.DsInfoService;
import cn.sliew.scaleph.ds.service.dto.DsInfoDTO;
import cn.sliew.scaleph.ds.service.param.DsInfoListParam;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "数据源管理-数据源")
@RestController
@RequestMapping(path = "/api/ds/info")
public class InfoController {

    @Autowired
    private DsInfoService dsInfoService;

    @Logging
    @GetMapping
    @Operation(summary = "查询数据源列表", description = "查询数据源列表")
    public ResponseEntity<Page<DsInfoDTO>> list(@Valid DsInfoListParam param) {
        final Page<DsInfoDTO> result = dsInfoService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("{type}")
    @Operation(summary = "查询指定数据源列表", description = "查询指定数据源列表")
    public ResponseEntity<ResponseVO<List<DsInfoDTO>>> listByType(@PathVariable("type") DataSourceType type) {
        final List<DsInfoDTO> result = dsInfoService.listByType(type);
        return new ResponseEntity<>(ResponseVO.success(result), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @Operation(summary = "获取数据源详情", description = "获取数据源详情")
    public ResponseEntity<ResponseVO<DsInfoDTO>> get(@PathVariable("id") Long id) {
        final DsInfoDTO result = dsInfoService.selectOne(id, false);
        return new ResponseEntity<>(ResponseVO.success(result), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增数据源", description = "新增数据源")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody AbstractDataSource dataSource) {
        dsInfoService.insert(dataSource);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("{id}")
    @Operation(summary = "修改数据源", description = "修改数据源")
    public ResponseEntity<ResponseVO> update(@PathVariable("id") Long id, @Valid @RequestBody AbstractDataSource dataSource) {
        dsInfoService.update(id, dataSource);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @Operation(summary = "删除数据源", description = "删除数据源")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) {
        dsInfoService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("batch")
    @Operation(summary = "批量删除数据源", description = "批量删除数据源")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) {
        dsInfoService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
