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
import cn.sliew.scaleph.system.service.SysDictService;
import cn.sliew.scaleph.system.service.SysDictTypeService;
import cn.sliew.scaleph.system.service.dto.SysDictDTO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
    public ResponseEntity<List<DictVO>> listDictByType(@PathVariable("dictTypeCode") String dictTypeCode) {
        List<SysDictDTO> list = sysDictService.selectByType(dictTypeCode);
        List<DictVO> result = list.stream()
                .map(d -> new DictVO(d.getDictCode(), d.getDictValue()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @AnonymousAccess
    @GetMapping(path = "/data/v2/{dictTypeCode}")
    @ApiOperation(value = "查询数据字典", notes = "根据字典类型code查询数据字典")
    public ResponseEntity<List<DictInstance>> listDictByType(@PathVariable("dictTypeCode") DictType dictType) {
        List<DictInstance> list = sysDictService.selectByType2(dictType);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/type")
    @ApiOperation(value = "查询数据字典类型", notes = "分页查询数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DICT_TYPE_SELECT)")
    public ResponseEntity<Page<DictType>> listDictType(SysDictTypeParam sysDictTypeParam) {
        Page<DictType> pageDTO = sysDictTypeService.listByPage(sysDictTypeParam);
        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }

    @AnonymousAccess
    @GetMapping(path = "/type/all")
    @ApiOperation(value = "查询数据字典类型", notes = "从缓存中查询所有数据字典类型")
    public ResponseEntity<List<DictType>> listDictTypeAll() {
        List<DictType> result = sysDictTypeService.selectAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}

