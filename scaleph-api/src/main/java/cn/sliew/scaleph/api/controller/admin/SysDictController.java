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

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.api.annotation.AnonymousAccess;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.system.vo.ResponseVO;
import cn.sliew.scaleph.system.cache.DictTypeCache;
import cn.sliew.scaleph.system.service.SysDictService;
import cn.sliew.scaleph.system.service.SysDictTypeService;
import cn.sliew.scaleph.system.service.dto.SysDictDTO;
import cn.sliew.scaleph.system.service.dto.SysDictTypeDTO;
import cn.sliew.scaleph.system.service.param.SysDictParam;
import cn.sliew.scaleph.system.service.param.SysDictTypeParam;
import cn.sliew.scaleph.system.service.vo.DictVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 分页查询数据字典
     *
     * @param sysDictParam 查询参数
     * @return ResponseEntity
     */
    @Logging
    @GetMapping(path = "/data")
    @ApiOperation(value = "查询数据字典", notes = "分页查询数据字典")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_DATA_SELECT)")
    public ResponseEntity<Page<SysDictDTO>> listDict(SysDictParam sysDictParam) {
        Page<SysDictDTO> pageDTO = this.sysDictService.listByPage(sysDictParam);
        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }

    @AnonymousAccess
    @GetMapping(path = "/data/{dictTypeCode}")
    @ApiOperation(value = "查询数据字典", notes = "根据字典类型code查询数据字典")
    public ResponseEntity<List<DictVO>> listDictByType(
        @NotNull @PathVariable(value = "dictTypeCode") String dictTypeCode) {
        List<SysDictDTO> list = this.sysDictService.selectByType(dictTypeCode);
        List<DictVO> result = new ArrayList<>(list.size());
        list.forEach(d -> {
            result.add(new DictVO(d.getDictCode(), d.getDictValue()));
        });
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 新增数据字典
     *
     * @param sysDictDTO 数据字典
     * @return ResponseEntity
     */
    @Logging
    @PostMapping(path = "/data")
    @ApiOperation(value = "新增数据字典", notes = "新增数据字典")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_DATA_ADD)")
    public ResponseEntity<ResponseVO> addDict(@Validated @RequestBody SysDictDTO sysDictDTO) {
        this.sysDictService.insert(sysDictDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    /**
     * 编辑数据字典
     *
     * @param sysDictDTO 数据字典
     * @return ResponseEntity
     */
    @Logging
    @PutMapping(path = "/data")
    @ApiOperation(value = "修改数据字典", notes = "修改数据字典")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_DATA_EDIT)")
    public ResponseEntity<ResponseVO> editDict(@Validated @RequestBody SysDictDTO sysDictDTO) {
        this.sysDictService.update(sysDictDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    /**
     * 根据id删除
     *
     * @param id id
     * @return ResponseEntity
     */
    @Logging
    @DeleteMapping(path = "/data/{id}")
    @ApiOperation(value = "删除数据字典", notes = "根据id删除数据字典")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_DATA_DELETE)")
    public ResponseEntity<ResponseVO> deleteDict(@PathVariable(value = "id") String id) {
        this.sysDictService.deleteById(Long.valueOf(id));
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    /**
     * 删除数据字典
     *
     * @param map 待删除的id列表
     * @return ResponseEntity
     */
    @Logging
    @PostMapping(path = "/data/batch")
    @ApiOperation(value = "批量删除数据字典", notes = "根据id列表批量数据字典")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_DATA_DELETE)")
    public ResponseEntity<ResponseVO> deleteBatchDict(@RequestBody Map<Integer, String> map) {
        this.sysDictService.deleteBatch(map);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }


    /**
     * 分页查询数据字典类型
     *
     * @param sysDictTypeParam 查询参数
     * @return ResponseEntity
     */
    @Logging
    @GetMapping(path = "/type")
    @ApiOperation(value = "查询数据字典类型", notes = "分页查询数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_TYPE_SELECT)")
    public ResponseEntity<Page<SysDictTypeDTO>> listDictType(SysDictTypeParam sysDictTypeParam) {
        Page<SysDictTypeDTO> pageDTO = this.sysDictTypeService.listByPage(sysDictTypeParam);
        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }

    /**
     * 查询全部数据字典类型
     *
     * @return ResponseEntity
     */
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

    /**
     * 新增数据字典类型
     *
     * @param sysDictTypeDTO 数据字典类型
     * @return ResponseEntity
     */
    @Logging
    @PostMapping(path = "/type")
    @ApiOperation(value = "新增数据字典类型", notes = "新增数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_TYPE_ADD)")
    public ResponseEntity<ResponseVO> addDictType(@Validated @RequestBody
                                                  SysDictTypeDTO sysDictTypeDTO) {
        this.sysDictTypeService.insert(sysDictTypeDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    /**
     * 编辑数据字典类型
     *
     * @param sysDictTypeDTO 数据字典类型
     * @return ResponseEntity
     */
    @Logging
    @PutMapping(path = "/type")
    @ApiOperation(value = "修改数据字典类型", notes = "修改数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_TYPE_EDIT)")
    public ResponseEntity<ResponseVO> editDictType(
        @Validated @RequestBody SysDictTypeDTO sysDictTypeDTO) {
        this.sysDictTypeService.update(sysDictTypeDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    /**
     * 根据id删除
     *
     * @param id id
     * @return ResponseEntity
     */
    @Logging
    @DeleteMapping(path = "/type/{id}")
    @ApiOperation(value = "删除数据字典类型", notes = "根据id删除数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_TYPE_DELETE)")
    public ResponseEntity<ResponseVO> deleteDictType(@PathVariable(value = "id") String id) {
        this.sysDictTypeService.deleteById(Long.valueOf(id));
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    /**
     * 删除数据字典类型
     *
     * @param map 待删除的id列表
     * @return ResponseEntity
     */
    @Logging
    @PostMapping(path = "/type/batch")
    @ApiOperation(value = "批量删除数据字典类型", notes = "根据id列表批量数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_TYPE_DELETE)")
    public ResponseEntity<ResponseVO> deleteBatchDictType(@RequestBody Map<Integer, String> map) {
        this.sysDictTypeService.deleteBatch(map);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}

