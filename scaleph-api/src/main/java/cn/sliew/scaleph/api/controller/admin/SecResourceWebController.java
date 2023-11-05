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

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.common.dict.security.ResourceType;
import cn.sliew.scaleph.security.service.SecResourceWebService;
import cn.sliew.scaleph.security.service.dto.SecResourceWebDTO;
import cn.sliew.scaleph.security.service.param.SecResourceWebAddParam;
import cn.sliew.scaleph.security.service.param.SecResourceWebListParam;
import cn.sliew.scaleph.security.service.param.SecResourceWebUpdateParam;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/admin/resource/web")
@Tag(name = "系统管理-资源管理-Web")
public class SecResourceWebController {

    @Autowired
    private SecResourceWebService secResourceWebService;

    @Logging
    @GetMapping("list")
    @Operation(summary = "分页查询 Web 资源树", description = "分页查询 Web 资源树")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).ROLE_GRANT)")
    public ResponseEntity<Page<SecResourceWebDTO>> listByPage(@Valid SecResourceWebListParam param) {
        Page<SecResourceWebDTO> resourceWebDTOPage = secResourceWebService.listByPage(param);
        return new ResponseEntity<>(resourceWebDTOPage, HttpStatus.OK);
    }

    @Logging
    @GetMapping
    @Operation(summary = "查询 Web 资源树", description = "查询 Web 资源树")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).ROLE_GRANT)")
    public ResponseEntity<List<Tree<Long>>> listAllPrivilege(@NotNull ResourceType resourceType) {
        List<SecResourceWebDTO> secResourceWebDTOS = secResourceWebService.listAll(resourceType);
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("id");
        treeNodeConfig.setParentIdKey("pid");
        treeNodeConfig.setNameKey("name");
        treeNodeConfig.setWeightKey("path");
        List<Tree<Long>> treeList =
                TreeUtil.build(secResourceWebDTOS, 0L, treeNodeConfig, (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getPid());
                    tree.setName(treeNode.getName());
                    tree.setWeight(treeNode.getPath());
                });

        return new ResponseEntity<>(treeList, HttpStatus.OK);
    }

    @Logging
    @GetMapping("list/{pid}")
    @Operation(summary = "查询 Web 资源树", description = "查询 Web 资源树")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).ROLE_GRANT)")
    public ResponseEntity<ResponseVO<List<SecResourceWebDTO>>> listByPid(@PathVariable("pid") Long pid) {
        List<SecResourceWebDTO> secResourceWebDTOS = secResourceWebService.listByPid(pid, null);
        return new ResponseEntity<>(ResponseVO.success(secResourceWebDTOS), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增 Web 资源", description = "新增 Web 资源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_SYSTEM_ADD)")
    public ResponseEntity<ResponseVO> add(@Validated @RequestBody SecResourceWebAddParam param) {
        secResourceWebService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.CREATED);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改 Web 资源", description = "修改 Web 资源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_SYSTEM_EDIT)")
    public ResponseEntity<ResponseVO> editMetaSystem(@Validated @RequestBody SecResourceWebUpdateParam param) {
        secResourceWebService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "删除 Web 资源", description = "删除 Web 资源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_SYSTEM_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaSystem(@PathVariable("id") Long id) {
        secResourceWebService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @Operation(summary = "批量删除 Web 资源", description = "批量删除 Web 资源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_SYSTEM_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaSystem(@RequestBody List<Long> ids) {
        secResourceWebService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
