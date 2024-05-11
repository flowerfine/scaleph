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

package cn.sliew.scaleph.api.controller.ws;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.workspace.project.service.WsProjectService;
import cn.sliew.scaleph.workspace.project.service.dto.WsProjectDTO;
import cn.sliew.scaleph.workspace.project.service.param.WsProjectParam;
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

@Tag(name = "数据开发-项目管理")
@RestController
@RequestMapping(path = {"/api/datadev/project", "/api/di/project"})
public class WsProjectController {

    @Autowired
    private WsProjectService wsProjectService;

    @Logging
    @GetMapping
    @Operation(summary = "查询项目列表", description = "分页查询项目列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_PROJECT_SELECT)")
    public ResponseEntity<Page<WsProjectDTO>> listProject(@Valid WsProjectParam param) {
        Page<WsProjectDTO> page = wsProjectService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "{id}")
    @Operation(summary = "查询项目信息", description = "查询项目信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_PROJECT_SELECT)")
    public ResponseEntity<WsProjectDTO> selectOne(@PathVariable("id") Long id) {
        WsProjectDTO project = wsProjectService.selectOne(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增项目", description = "新增项目")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_PROJECT_ADD)")
    public ResponseEntity<ResponseVO> addProject(@Validated @RequestBody WsProjectDTO wsProjectDTO) {
        wsProjectService.insert(wsProjectDTO);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.CREATED);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改项目", description = "修改项目")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_PROJECT_EDIT)")
    public ResponseEntity<ResponseVO> editProject(@Validated @RequestBody WsProjectDTO wsProjectDTO) {
        wsProjectService.update(wsProjectDTO);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "{id}")
    @Operation(summary = "删除项目", description = "删除项目")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_PROJECT_DELETE)")
    public ResponseEntity<ResponseVO> deleteProject(@PathVariable(value = "id") Long projectId) {
        wsProjectService.deleteById(projectId);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "batch")
    @Operation(summary = "批量删除项目", description = "批量删除项目")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_PROJECT_DELETE)")
    public ResponseEntity<ResponseVO> deleteProject(@RequestBody List<Long> ids) {
        wsProjectService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }
}
