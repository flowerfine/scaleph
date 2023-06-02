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
import cn.sliew.scaleph.meta.service.MetaSystemService;
import cn.sliew.scaleph.meta.service.dto.MetaSystemDTO;
import cn.sliew.scaleph.meta.service.param.MetaSystemParam;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author gleiyu
 */
@Api(tags = "数据标准-业务系统")
@RestController
@RequestMapping(path = "/api/stdata/system")
public class SystemController {

    @Autowired
    private MetaSystemService metaSystemService;

    @Logging
    @GetMapping
    @ApiOperation(value = "分页查询业务系统", notes = "分页查询业务系统信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_SYSTEM_SELECT)")
    public ResponseEntity<Page<MetaSystemDTO>> listMetaSystem(@Valid MetaSystemParam param) {
        Page<MetaSystemDTO> page = metaSystemService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "新增业务系统", notes = "新增业务系统")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_SYSTEM_ADD)")
    public ResponseEntity<ResponseVO> addMetaSystem(@Validated @RequestBody MetaSystemDTO metaSystemDTO) {
        metaSystemService.insert(metaSystemDTO);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.CREATED);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "修改业务系统", notes = "修改业务系统")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_SYSTEM_EDIT)")
    public ResponseEntity<ResponseVO> editMetaSystem(@Validated @RequestBody MetaSystemDTO metaSystemDTO) {
        metaSystemService.update(metaSystemDTO);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "删除业务系统", notes = "删除业务系统")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_SYSTEM_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaSystem(@PathVariable(value = "id") Long id) {
        metaSystemService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @ApiOperation(value = "批量删除业务系统", notes = "批量删除业务系统")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_SYSTEM_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaSystem(@RequestBody List<Long> ids) {
        metaSystemService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }
}
