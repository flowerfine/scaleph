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
import cn.hutool.json.JSONUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.security.service.SecPrivilegeService;
import cn.sliew.scaleph.security.service.SecRolePrivilegeService;
import cn.sliew.scaleph.security.service.dto.SecPrivilegeDTO;
import cn.sliew.scaleph.security.service.dto.SecRolePrivilegeDTO;
import cn.sliew.scaleph.security.service.param.SecPrivilegeAddParam;
import cn.sliew.scaleph.security.service.param.SecPrivilegeListParam;
import cn.sliew.scaleph.security.service.param.SecPrivilegeUpdateParam;
import cn.sliew.scaleph.security.web.OnlineUserService;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author liyu
 */
@RestController
@RequestMapping("/api/admin/privilege")
@Tag(name = "系统管理-权限管理")
public class SecPrivilegeController {

    @Autowired
    private SecPrivilegeService secPrivilegeService;
    @Autowired
    private SecRolePrivilegeService secRolePrivilegeService;
    @Autowired
    private OnlineUserService onlineUserService;

    @Logging
    @GetMapping("list")
    @Operation(summary = "查询权限树", description = "查询权限树")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).ROLE_GRANT)")
    public ResponseEntity<Page<SecPrivilegeDTO>> listByPage(@Valid SecPrivilegeListParam param) {
        Page<SecPrivilegeDTO> privilegeList = this.secPrivilegeService.listByPage(param);
        return new ResponseEntity<>(privilegeList, HttpStatus.OK);
    }

    @Logging
    @GetMapping
    @Operation(summary = "查询权限树", description = "查询权限树")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).ROLE_GRANT)")
    public ResponseEntity<List<Tree<Long>>> listAllPrivilege(@NotNull String resourceType) {
        List<SecPrivilegeDTO> privilegeList = this.secPrivilegeService.listAll(resourceType);
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("privilegeId");
        treeNodeConfig.setParentIdKey("pid");
        treeNodeConfig.setNameKey("privilegeName");
        treeNodeConfig.setWeightKey("privilegeCode");
        List<Tree<Long>> treeList =
                TreeUtil.build(privilegeList, 0L, treeNodeConfig, (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getPid());
                    tree.setName(treeNode.getPrivilegeName());
                    tree.setWeight(treeNode.getPrivilegeCode());
                });

        return new ResponseEntity<>(treeList, HttpStatus.OK);
    }

    @Logging
    @GetMapping("list/{pid}")
    @Operation(summary = "查询权限树", description = "查询权限树")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).ROLE_GRANT)")
    public ResponseEntity<ResponseVO<List<SecPrivilegeDTO>>> listByPid(@PathVariable("pid") Long pid) {
        List<SecPrivilegeDTO> privilegeList = this.secPrivilegeService.listByPid(pid, null);
        return new ResponseEntity<>(ResponseVO.success(privilegeList), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/role")
    @Operation(summary = "查询权限树", description = "查询权限树")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).ROLE_GRANT)")
    public ResponseEntity<List<SecPrivilegeDTO>> listPrivilege(@NotNull Long roleId,
                                                               @NotNull String resourceType) {
        List<SecPrivilegeDTO> result = new ArrayList<>();
        this.secRolePrivilegeService.listByRoleId(roleId, resourceType).forEach(d -> {
            SecPrivilegeDTO p = new SecPrivilegeDTO();
            p.setId(d.getPrivilegeId());
            result.add(p);
        });
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @Operation(summary = "角色授权权限", description = "角色授权权限")
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).ROLE_GRANT)")
    public ResponseEntity<ResponseVO> grantPrivilege(@NotNull Long roleId,
                                                     @NotNull String privilegeIds,
                                                     @NotNull String resourceType) {
        List<Long> privilegeList = JSONUtil.toList(privilegeIds, Long.class);
        List<SecRolePrivilegeDTO> oldPrivilegeList =
                this.secRolePrivilegeService.listByRoleId(roleId, resourceType);
        List<Long> tmpList = new ArrayList<>(privilegeList.size());
        tmpList.addAll(privilegeList);
        //grant new user
        tmpList.removeAll(oldPrivilegeList.stream().map(SecRolePrivilegeDTO::getPrivilegeId)
                .collect(Collectors.toList()));
        for (Long privilegeId : tmpList) {
            SecRolePrivilegeDTO userPrivilege = new SecRolePrivilegeDTO();
            userPrivilege.setRoleId(roleId);
            userPrivilege.setPrivilegeId(privilegeId);
            this.secRolePrivilegeService.insert(userPrivilege);
        }
        //revoke removed user
        for (SecRolePrivilegeDTO rolePrivilege : oldPrivilegeList) {
            if (!privilegeList.contains(rolePrivilege.getPrivilegeId())) {
                this.secRolePrivilegeService.delete(rolePrivilege);
            }
        }
        this.onlineUserService.disableOnlineCacheRole(roleId);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增权限", description = "新增权限")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_SYSTEM_ADD)")
    public ResponseEntity<ResponseVO> add(@Validated @RequestBody SecPrivilegeAddParam param) {
        secPrivilegeService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.CREATED);
    }

    @Logging
    @PostMapping("{id}")
    @Operation(summary = "修改权限", description = "修改权限")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_SYSTEM_EDIT)")
    public ResponseEntity<ResponseVO> editMetaSystem(@PathVariable("id") Long id, @Validated @RequestBody SecPrivilegeUpdateParam param) {
        secPrivilegeService.update(id, param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "删除权限", description = "删除权限")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_SYSTEM_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaSystem(@PathVariable("id") Long id) {
        secPrivilegeService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @Operation(summary = "批量删除权限", description = "批量删除权限")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_SYSTEM_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaSystem(@RequestBody List<Long> ids) {
        secPrivilegeService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
