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

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.security.service.SecAuthorizeService;
import cn.sliew.scaleph.security.service.dto.SecResourceWebWithAuthorizeDTO;
import cn.sliew.scaleph.security.service.dto.SecRoleDTO;
import cn.sliew.scaleph.security.service.dto.SecUserDTO;
import cn.sliew.scaleph.security.service.dto.UmiRoute;
import cn.sliew.scaleph.security.service.param.*;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/authorize")
@Tag(name = "系统管理-授权管理")
public class SecAuthorizeController {

    @Autowired
    private SecAuthorizeService secAuthorizeService;

    @Logging
    @GetMapping("routes")
    @Operation(summary = "查询 umi.js 路由", description = "查询 umi.js 路由")
    public ResponseEntity<List<UmiRoute>> getUmiRoutes() {
        List<UmiRoute> routes = secAuthorizeService.getWebRoute();
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    @Logging
    @GetMapping("resource-web/authorized-roles")
    @Operation(summary = "查询 资源-web 绑定角色列表", description = "查询 资源-web 绑定角色列表")
    public ResponseEntity<Page<SecRoleDTO>> listAuthorizedRolesByResourceWebId(@Valid SecRoleListByResourceWebParam param) {
        Page<SecRoleDTO> result = secAuthorizeService.listAuthorizedRolesByResourceWebId(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("resource-web/unauthorized-roles")
    @Operation(summary = "查询 资源-web 未绑定角色列表", description = "查询 资源-web 未绑定角色列表")
    public ResponseEntity<Page<SecRoleDTO>> listUnauthorizedRolesByResourceWebId(@Valid SecRoleListByResourceWebParam param) {
        Page<SecRoleDTO> result = secAuthorizeService.listUnauthorizedRolesByResourceWebId(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping("resource-web/roles")
    @Operation(summary = "批量为 资源-web 绑定角色", description = "批量为 资源-web 绑定角色")
    public ResponseEntity<ResponseVO> authorize(@Valid @RequestBody SecRoleBatchAuthorizeForResourceWebParam param) {
        secAuthorizeService.authorize(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("resource-web/roles")
    @Operation(summary = "批量为 资源-web 解除角色绑定", description = "批量为 资源-web 解除角色绑定")
    public ResponseEntity<ResponseVO> unauthorize(@Valid @RequestBody SecRoleBatchAuthorizeForResourceWebParam param) {
        secAuthorizeService.unauthorize(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("role/resource-webs")
    @Operation(summary = "查询所有 资源-web 和指定角色绑定状态", description = "查询所有 资源-web 和指定角色绑定状态")
    public ResponseEntity<List<SecResourceWebWithAuthorizeDTO>> listResourceWebsByRole(@Valid SecResourceWebListByRoleParam param) {
        List<SecResourceWebWithAuthorizeDTO> result = secAuthorizeService.listResourceWebsByRoleId(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping("role/resource-webs")
    @Operation(summary = "批量为角色绑定 资源-web", description = "批量为角色绑定 资源-web")
    public ResponseEntity<ResponseVO> authorize(@Valid @RequestBody SecResourceWebBatchAuthorizeForRoleParam param) {
        secAuthorizeService.authorize(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("role/resource-webs")
    @Operation(summary = "批量为角色解除 资源-web 绑定", description = "批量为角色解除 资源-web 绑定")
    public ResponseEntity<ResponseVO> unauthorize(@Valid @RequestBody SecResourceWebBatchAuthorizeForRoleParam param) {
        secAuthorizeService.unauthorize(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("role/authorized-users")
    @Operation(summary = "查询角色绑定用户列表", description = "查询角色绑定用户列表")
    public ResponseEntity<Page<SecUserDTO>> listAuthorizedUsersByRoleId(@Valid SecUserListByRoleParam param) {
        Page<SecUserDTO> result = secAuthorizeService.listAuthorizedUsersByRoleId(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("role/unauthorized-users")
    @Operation(summary = "查询角色未绑定用户列表", description = "查询角色未绑定用户列表")
    public ResponseEntity<Page<SecUserDTO>> listUnauthorizedUsersByRoleId(@Valid SecUserListByRoleParam param) {
        Page<SecUserDTO> result = secAuthorizeService.listUnauthorizedUsersByRoleId(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping("role/users")
    @Operation(summary = "批量为角色绑定用户", description = "批量为角色绑定用户")
    public ResponseEntity<ResponseVO> authorize(@Valid @RequestBody SecUserBatchAuthorizeForRoleParam param) {
        secAuthorizeService.authorize(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("role/users")
    @Operation(summary = "批量为角色解除用户绑定", description = "批量为角色解除用户绑定")
    public ResponseEntity<ResponseVO> unauthorize(@Valid @RequestBody SecUserBatchAuthorizeForRoleParam param) {
        secAuthorizeService.unauthorize(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("user/authorized-roles")
    @Operation(summary = "查询用户绑定角色列表", description = "查询用户绑定角色列表")
    public ResponseEntity<List<SecRoleDTO>> listAuthorizedRolesByUserId(@Valid SecRoleListByUserParam param) {
        List<SecRoleDTO> result = secAuthorizeService.listAuthorizedRolesByUserId(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("user/unauthorized-roles")
    @Operation(summary = "查询用户未绑定角色列表", description = "查询用户未绑定角色列表")
    public ResponseEntity<List<SecRoleDTO>> listUnauthorizedRolesByUserId(@Valid SecRoleListByUserParam param) {
        List<SecRoleDTO> result = secAuthorizeService.listUnauthorizedRolesByUserId(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping("user/roles")
    @Operation(summary = "批量为用户绑定角色", description = "批量为用户绑定角色")
    public ResponseEntity<ResponseVO> authorize(@Valid @RequestBody SecRoleBatchAuthorizeForUserParam param) {
        secAuthorizeService.authorize(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("user/roles")
    @Operation(summary = "批量为用户解除角色绑定", description = "批量为用户解除角色绑定")
    public ResponseEntity<ResponseVO> unauthorize(@Valid @RequestBody SecRoleBatchAuthorizeForUserParam param) {
        secAuthorizeService.unauthorize(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
