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
import cn.sliew.scaleph.ds.service.DsCategoryService;
import cn.sliew.scaleph.ds.service.dto.DsCategoryDTO;
import cn.sliew.scaleph.ds.service.dto.DsTypeDTO;
import cn.sliew.scaleph.ds.service.param.DsTypeListParam;
import cn.sliew.scaleph.system.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "数据源管理-分类管理")
@RestController
@RequestMapping(path = "/api/ds/category")
public class CategoryController {

    @Autowired
    private DsCategoryService dsCategoryService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询分类列表", notes = "查询分类列表")
    public ResponseEntity<ResponseVO<List<DsCategoryDTO>>> list() {
        List<DsCategoryDTO> result = dsCategoryService.list();
        return new ResponseEntity<>(ResponseVO.sucess(result), HttpStatus.OK);
    }

    @Logging
    @GetMapping("type")
    @ApiOperation(value = "查询数据源类型", notes = "查询数据源类型")
    public ResponseEntity<ResponseVO<List<DsTypeDTO>>> listTypes(@Valid DsTypeListParam param) {
        List<DsTypeDTO> result = dsCategoryService.listTypes(param);
        return new ResponseEntity<>(ResponseVO.sucess(result), HttpStatus.OK);
    }

}
