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

package cn.sliew.scaleph.api.controller.stdata;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.meta.service.MetaDataElementService;
import cn.sliew.scaleph.meta.service.dto.MetaDataElementDTO;
import cn.sliew.scaleph.meta.service.param.MetaDataElementParam;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "数据标准-数据元")
@RestController
@RequestMapping(path = "/api/stdata/element")
public class DataElementController {

    @Autowired
    private MetaDataElementService metaDataElementService;

    @Logging
    @GetMapping
    @Operation(summary = "分页查询数据元", description = "分页查询数据元信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_DATA_ELEMENT_SELECT)")
    public ResponseEntity<Page<MetaDataElementDTO>> listMetaDataElement(@Valid MetaDataElementParam param) {
        Page<MetaDataElementDTO> page = metaDataElementService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增数据元", description = "新增数据元")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_DATA_ELEMENT_ADD)")
    public ResponseEntity<ResponseVO> addMetaDataElement(@Validated @RequestBody MetaDataElementDTO metaDataElementDTO) {
        metaDataElementService.insert(metaDataElementDTO);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.CREATED);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改数据元", description = "修改数据元")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_DATA_ELEMENT_EDIT)")
    public ResponseEntity<ResponseVO> editMetaDataElement(@Validated @RequestBody MetaDataElementDTO metaDataElementDTO) {
        metaDataElementService.update(metaDataElementDTO);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "删除数据元", description = "删除数据元")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_DATA_ELEMENT_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaDataElement(@PathVariable(value = "id") String id) {
        metaDataElementService.deleteById(Long.valueOf(id));
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @Operation(summary = "批量删除数据元", description = "批量删除数据元")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_DATA_ELEMENT_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaDataElement(@RequestBody List<Long> ids) {
        metaDataElementService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }
}
