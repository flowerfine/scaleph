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
import cn.sliew.scaleph.security.service.SecDeptService;
import cn.sliew.scaleph.security.service.SecUserDeptService;
import cn.sliew.scaleph.security.service.dto.SecDeptDTO;
import cn.sliew.scaleph.security.service.dto.SecDeptTreeDTO;
import cn.sliew.scaleph.security.service.dto.SecUserDeptDTO;
import cn.sliew.scaleph.security.service.param.SecDeptListParam;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author liyu
 */
@RestController
@RequestMapping("/api/admin/dept")
@Tag(name = "系统管理-部门管理")
public class SecDeptController {

    @Autowired
    private SecDeptService secDeptService;
    @Autowired
    private SecUserDeptService secUserDeptService;

    @Logging
    @GetMapping("list")
    @Operation(summary = "查询部门树", description = "查询部门树")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PagePrivilege).ADMIN_DEPT_SHOW)")
    public ResponseEntity<Page<SecDeptTreeDTO>> listByPage(@Valid SecDeptListParam param) {
        Page<SecDeptTreeDTO> result = secDeptService.listByPage(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping
    @Operation(summary = "查询部门树", description = "查询部门树")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PagePrivilege).ADMIN_DEPT_SHOW)")
    public ResponseEntity<List<Tree<Long>>> listDept() {
        List<SecDeptDTO> list = this.secDeptService.listAll();
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("deptId");
        treeNodeConfig.setParentIdKey("pid");
        treeNodeConfig.setNameKey("deptName");
        treeNodeConfig.setWeightKey("deptCode");
        List<Tree<Long>> treeList = TreeUtil.build(list, 0L, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getPid());
            tree.setName(treeNode.getDeptName());
            tree.setWeight(treeNode.getDeptCode());
        });
        return new ResponseEntity<>(treeList, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/{pid}")
    @Operation(summary = "查询子节点部门树", description = "查询子节点部门树")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PagePrivilege).ADMIN_DEPT_SHOW)")
    public ResponseEntity<List<Tree<Long>>> listChildDept(@PathVariable("pid") Long pid) {
        return new ResponseEntity<>(selectChilds(pid), HttpStatus.OK);
    }

    private List<Tree<Long>> selectChilds(Long pid) {
        List<SecDeptDTO> list = this.secDeptService.listAll();
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("deptId");
        treeNodeConfig.setParentIdKey("pid");
        treeNodeConfig.setNameKey("deptName");
        treeNodeConfig.setWeightKey("deptCode");
        List<Tree<Long>> treeList =
                TreeUtil.build(list, pid, treeNodeConfig, (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getPid());
                    tree.setName(treeNode.getDeptName());
                    tree.setWeight(treeNode.getDeptCode());
                });
        return treeList;
    }

    @Logging
    @PostMapping
    @Operation(summary = "新增部门", description = "新增部门")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.ButtonPrivilege).ADMIN_DEPT_ADD)")
    public ResponseEntity<ResponseVO> addDept(@Validated @RequestBody SecDeptDTO secDeptDTO) {
        this.secDeptService.insert(secDeptDTO);
        SecDeptDTO dept = this.secDeptService.selectOne(secDeptDTO.getDeptCode());
        return new ResponseEntity<>(ResponseVO.success(dept.getId()), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "修改部门", description = "修改部门")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.ButtonPrivilege).ADMIN_DEPT_EDIT)")
    public ResponseEntity<ResponseVO> editDept(@Validated @RequestBody SecDeptDTO secDeptDTO) {
        this.secDeptService.update(secDeptDTO);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "删除部门", description = "删除部门")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.ButtonPrivilege).ADMIN_DEPT_DELETE)")
    public ResponseEntity<ResponseVO> deleteDept(@PathVariable("id") Long id) {
        secDeptService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @Operation(summary = "删除部门", description = "删除部门")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.ButtonPrivilege).ADMIN_DEPT_DELETE)")
    public ResponseEntity<ResponseVO> deleteDept(@RequestBody List<Long> ids) {
        secDeptService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/grant")
    @Operation(summary = "部门分配用户", description = "部门分配用户")
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.ButtonPrivilege).ADMIN_DEPT_AUTHORIZE)")
    public ResponseEntity<ResponseVO> grantDept(@RequestParam("deptId") Long deptId, @RequestParam("userIds") String userIds) {
        List<Long> userList = JSONUtil.toList(userIds, Long.class);
        List<SecUserDeptDTO> oldUserList = this.secUserDeptService.listByDeptId(deptId);
        List<Long> tmpList = new ArrayList<>(userList.size());
        tmpList.addAll(userList);
        //grant new user
        tmpList.removeAll(
                oldUserList.stream().map(SecUserDeptDTO::getUserId).collect(Collectors.toList()));
        for (Long userId : tmpList) {
            SecUserDeptDTO userDept = new SecUserDeptDTO();
            userDept.setDeptId(deptId);
            userDept.setUserId(userId);
            this.secUserDeptService.insert(userDept);
        }
        //revoke removed user
        for (SecUserDeptDTO userDept : oldUserList) {
            if (!userList.contains(userDept.getUserId())) {
                this.secUserDeptService.delete(userDept);
            }
        }
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }
}
