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

package cn.sliew.scaleph.api.controller.admin;

import cn.sliew.scaleph.api.annotation.AnonymousAccess;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.common.dict.DictInstance;
import cn.sliew.scaleph.common.dict.DictType;
import cn.sliew.scaleph.system.cache.DictTypeCache;
import cn.sliew.scaleph.system.service.SysDictService;
import cn.sliew.scaleph.system.service.SysDictTypeService;
import cn.sliew.scaleph.system.service.dto.SysDictDTO;
import cn.sliew.scaleph.system.service.dto.SysDictTypeDTO;
import cn.sliew.scaleph.system.service.param.SysDictParam;
import cn.sliew.scaleph.system.service.param.SysDictTypeParam;
import cn.sliew.scaleph.system.service.vo.DictVO;
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

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据字典表 前端控制器
 * </p>
 *
 * @author liyu
 */
@RestController
@RequestMapping("/api/admin/dict")
@Api(tags = "系统管理-数据字典管理")
public class SysDictController {

    @Autowired
    private SysDictTypeService sysDictTypeService;
    @Autowired
    private SysDictService sysDictService;

    @Logging
    @GetMapping(path = "/data")
    @ApiOperation(value = "查询数据字典", notes = "分页查询数据字典")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_DATA_SELECT)")
    public ResponseEntity<Page<SysDictDTO>> listDict(SysDictParam sysDictParam) {
        Page<SysDictDTO> pageDTO = sysDictService.listByPage(sysDictParam);
        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }

    /**
     * todo multiple version endpoint path such as v1/data, v2/data
     *
     * @see org.springframework.web.servlet.mvc.condition.RequestCondition
     */
    @AnonymousAccess
    @GetMapping(path = "/data/{dictTypeCode}")
    @ApiOperation(value = "查询数据字典", notes = "根据字典类型code查询数据字典")
    public ResponseEntity<List<DictVO>> listDictByType(
            @NotNull @PathVariable(value = "dictTypeCode") String dictTypeCode) {
        List<SysDictDTO> list = sysDictService.selectByType(dictTypeCode);
        List<DictVO> result = list.stream()
                .map(d -> new DictVO(d.getDictCode(), d.getDictValue()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @AnonymousAccess
    @GetMapping(path = "/data/v2/{dictTypeCode}")
    @ApiOperation(value = "查询数据字典", notes = "根据字典类型code查询数据字典")
    public ResponseEntity<List<DictInstance>> listDictByType(
            @PathVariable(value = "dictTypeCode") DictType dictType) {
        List<DictInstance> list = sysDictService.selectByType2(dictType);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/data")
    @ApiOperation(value = "新增数据字典", notes = "新增数据字典")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_DATA_ADD)")
    public ResponseEntity<ResponseVO> addDict(@Validated @RequestBody SysDictDTO sysDictDTO) {
        sysDictService.insert(sysDictDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    @Logging
    @PutMapping(path = "/data")
    @ApiOperation(value = "修改数据字典", notes = "修改数据字典")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_DATA_EDIT)")
    public ResponseEntity<ResponseVO> editDict(@Validated @RequestBody SysDictDTO sysDictDTO) {
        sysDictService.update(sysDictDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/data/{id}")
    @ApiOperation(value = "删除数据字典", notes = "根据id删除数据字典")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_DATA_DELETE)")
    public ResponseEntity<ResponseVO> deleteDict(@PathVariable(value = "id") String id) {
        sysDictService.deleteById(Long.valueOf(id));
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/data/batch")
    @ApiOperation(value = "批量删除数据字典", notes = "根据id列表批量数据字典")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_DATA_DELETE)")
    public ResponseEntity<ResponseVO> deleteBatchDict(@RequestBody Map<Integer, String> map) {
        sysDictService.deleteBatch(map);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/type")
    @ApiOperation(value = "查询数据字典类型", notes = "分页查询数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_TYPE_SELECT)")
    public ResponseEntity<Page<SysDictTypeDTO>> listDictType(SysDictTypeParam sysDictTypeParam) {
        Page<SysDictTypeDTO> pageDTO = sysDictTypeService.listByPage(sysDictTypeParam);
        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }

    @AnonymousAccess
    @GetMapping(path = "/type/all")
    @ApiOperation(value = "查询数据字典类型", notes = "从缓存中查询所有数据字典类型")
    public ResponseEntity<List<DictVO>> listDictTypeAll() {
        List<SysDictTypeDTO> list = DictTypeCache.listAll();
        List<DictVO> result = new ArrayList<>();
        for (SysDictTypeDTO dto : list) {
            result.add(new DictVO(dto.getDictTypeCode(), dto.getDictTypeName()));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/type")
    @ApiOperation(value = "新增数据字典类型", notes = "新增数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_TYPE_ADD)")
    public ResponseEntity<ResponseVO> addDictType(@Validated @RequestBody
                                                  SysDictTypeDTO sysDictTypeDTO) {
        sysDictTypeService.insert(sysDictTypeDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    @Logging
    @PutMapping(path = "/type")
    @ApiOperation(value = "修改数据字典类型", notes = "修改数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_TYPE_EDIT)")
    public ResponseEntity<ResponseVO> editDictType(
            @Validated @RequestBody SysDictTypeDTO sysDictTypeDTO) {
        sysDictTypeService.update(sysDictTypeDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/type/{id}")
    @ApiOperation(value = "删除数据字典类型", notes = "根据id删除数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_TYPE_DELETE)")
    public ResponseEntity<ResponseVO> deleteDictType(@PathVariable(value = "id") String id) {
        sysDictTypeService.deleteById(Long.valueOf(id));
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/type/batch")
    @ApiOperation(value = "批量删除数据字典类型", notes = "根据id列表批量数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_TYPE_DELETE)")
    public ResponseEntity<ResponseVO> deleteBatchDictType(@RequestBody Map<Integer, String> map) {
        sysDictTypeService.deleteBatch(map);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}

