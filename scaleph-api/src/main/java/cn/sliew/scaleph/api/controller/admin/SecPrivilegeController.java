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
import java.util.stream.Collectors;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.json.JSONUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.api.security.OnlineUserService;
import cn.sliew.scaleph.api.vo.ResponseVO;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.security.service.SecPrivilegeService;
import cn.sliew.scaleph.security.service.SecRolePrivilegeService;
import cn.sliew.scaleph.security.service.dto.SecPrivilegeDTO;
import cn.sliew.scaleph.security.service.dto.SecRolePrivilegeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author liyu
 */
@RestController
@RequestMapping("/api/admin/privilege")
@Api(tags = "系统管理-权限管理")
public class SecPrivilegeController {

    @Autowired
    private SecPrivilegeService secPrivilegeService;
    @Autowired
    private SecRolePrivilegeService secRolePrivilegeService;
    @Autowired
    private OnlineUserService onlineUserService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询权限树", notes = "查询权限树")
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
    @GetMapping("/role")
    @ApiOperation(value = "查询权限树", notes = "查询权限树")
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
    @ApiOperation(value = "角色授权权限", notes = "角色授权权限")
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
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

}

