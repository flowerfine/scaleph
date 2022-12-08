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
import cn.sliew.scaleph.meta.service.MetaDataMapService;
import cn.sliew.scaleph.meta.service.MetaDataSetService;
import cn.sliew.scaleph.meta.service.MetaDataSetTypeService;
import cn.sliew.scaleph.meta.service.dto.MetaDataMapDTO;
import cn.sliew.scaleph.meta.service.dto.MetaDataSetDTO;
import cn.sliew.scaleph.meta.service.dto.MetaDataSetTypeDTO;
import cn.sliew.scaleph.meta.service.param.MetaDataMapParam;
import cn.sliew.scaleph.meta.service.param.MetaDataSetParam;
import cn.sliew.scaleph.meta.service.param.MetaDataSetTypeParam;
import cn.sliew.scaleph.system.vo.ResponseVO;
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

@Api(tags = "数据标准-参考数据")
@RestController
@RequestMapping(path = "/api/stdata/ref")
public class RefDataController {

    @Autowired
    private MetaDataSetService metaDataSetService;
    @Autowired
    private MetaDataSetTypeService metaDataSetTypeService;
    @Autowired
    private MetaDataMapService metaDataMapService;

    @Logging
    @GetMapping(path = "/data")
    @ApiOperation(value = "分页查询参考数据", notes = "分页查询参考数据信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_SELECT)")
    public ResponseEntity<Page<MetaDataSetDTO>> listMetaDataSet(@Valid MetaDataSetParam param) {
        Page<MetaDataSetDTO> page = metaDataSetService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/data/type/{type}")
    @ApiOperation(value = "按类型查询参考数据", notes = "按类型查询参考数据")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_SELECT)")
    public ResponseEntity<ResponseVO<List<MetaDataSetDTO>>> listMetaDataByType(@PathVariable("type") Long typeId) {
        List<MetaDataSetDTO> list = metaDataSetService.listByType(typeId);
        return new ResponseEntity<>(ResponseVO.sucess(list), HttpStatus.OK);
    }

    @Logging
    @PutMapping(path = "/data")
    @ApiOperation(value = "新增参考数据", notes = "新增参考数据")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_ADD)")
    public ResponseEntity<ResponseVO> addMetaDataSet(@Validated @RequestBody MetaDataSetDTO metaDataSetDTO) {
        metaDataSetService.insert(metaDataSetDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    @Logging
    @PostMapping(path = "/data")
    @ApiOperation(value = "修改参考数据", notes = "修改参考数据")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_EDIT)")
    public ResponseEntity<ResponseVO> editMetaDataSet(@Validated @RequestBody MetaDataSetDTO metaDataSetDTO) {
        metaDataSetService.update(metaDataSetDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/data/{id}")
    @ApiOperation(value = "删除参考数据", notes = "删除参考数据")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaDataSet(@PathVariable("id") Long id) {
        metaDataSetService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/data/batch")
    @ApiOperation(value = "批量删除参考数据", notes = "批量删除参考数据")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaDataSet(@RequestBody List<Long> ids) {
        metaDataSetService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/type")
    @ApiOperation(value = "分页查询参考数据类型", notes = "分页查询参考数据类型信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_TYPE_SELECT)")
    public ResponseEntity<Page<MetaDataSetTypeDTO>> listMetaDataSetType(MetaDataSetTypeParam param) {
        Page<MetaDataSetTypeDTO> page = metaDataSetTypeService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @PutMapping(path = "/type")
    @ApiOperation(value = "新增参考数据类型", notes = "新增参考数据类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_TYPE_ADD)")
    public ResponseEntity<ResponseVO> addMetaDataSetType(@Validated @RequestBody MetaDataSetTypeDTO metaDataSetTypeDTO) {
        metaDataSetTypeService.insert(metaDataSetTypeDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    @Logging
    @PostMapping(path = "/type")
    @ApiOperation(value = "修改参考数据类型", notes = "修改参考数据类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_TYPE_EDIT)")
    public ResponseEntity<ResponseVO> editMetaDataSetType(@Validated @RequestBody MetaDataSetTypeDTO metaDataSetTypeDTO) {
        metaDataSetTypeService.update(metaDataSetTypeDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/type/{id}")
    @ApiOperation(value = "删除参考数据类型", notes = "删除参考数据类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_TYPE_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaDataSetType(@PathVariable(value = "id") Long id) {
        metaDataSetTypeService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/type/batch")
    @ApiOperation(value = "批量删除参考数据类型", notes = "批量删除参考数据类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_TYPE_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaDataSetType(@RequestBody List<Long> ids) {
        metaDataSetTypeService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/map")
    @ApiOperation(value = "分页查询参考数据映射", notes = "分页查询参考数据映射信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_MAP_SELECT)")
    public ResponseEntity<Page<MetaDataMapDTO>> listMetaDataMap(MetaDataMapParam param) {
        Page<MetaDataMapDTO> page = metaDataMapService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @PutMapping(path = "/map")
    @ApiOperation(value = "新增参考数据映射", notes = "新增参考数据映射")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_MAP_ADD)")
    public ResponseEntity<ResponseVO> addMetaDataMap(@Validated @RequestBody MetaDataMapDTO metaDataSetTypeDTO) {
        metaDataMapService.insert(metaDataSetTypeDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    @Logging
    @PostMapping(path = "/map")
    @ApiOperation(value = "修改参考数据映射", notes = "修改参考数据映射")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_MAP_EDIT)")
    public ResponseEntity<ResponseVO> editMetaDataMap(@Validated @RequestBody MetaDataMapDTO metaDataSetTypeDTO) {
        metaDataMapService.update(metaDataSetTypeDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/map/{id}")
    @ApiOperation(value = "删除参考数据映射", notes = "删除参考数据映射")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_MAP_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaDataMap(@PathVariable(value = "id") Long id) {
        metaDataMapService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/map/batch")
    @ApiOperation(value = "批量删除参考数据映射", notes = "批量删除参考数据映射")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_REF_DATA_MAP_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaDataMap(@RequestBody List<Long> ids) {
        metaDataMapService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
