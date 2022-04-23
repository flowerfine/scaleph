package com.liyu.breeze.api.controller.admin;


import cn.hutool.json.JSONUtil;
import com.liyu.breeze.api.annotation.Logging;
import com.liyu.breeze.api.security.OnlineUserService;
import com.liyu.breeze.api.vo.ResponseVO;
import com.liyu.breeze.common.constant.Constants;
import com.liyu.breeze.common.enums.RoleTypeEnum;
import com.liyu.breeze.service.admin.DeptRoleService;
import com.liyu.breeze.service.admin.RoleService;
import com.liyu.breeze.service.admin.UserRoleService;
import com.liyu.breeze.service.admin.UserService;
import com.liyu.breeze.service.dto.admin.DeptRoleDTO;
import com.liyu.breeze.service.dto.admin.RoleDTO;
import com.liyu.breeze.service.dto.admin.UserDTO;
import com.liyu.breeze.service.dto.admin.UserRoleDTO;
import com.liyu.breeze.service.vo.DictVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author liyu
 */
@RestController
@RequestMapping("/api/admin/role")
@Api(tags = "系统管理-角色管理")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private DeptRoleService deptRoleService;

    @Autowired
    private OnlineUserService onlineUserService;

    @Autowired
    private UserService userService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询角色列表", notes = "查询全部角色信息")
    @PreAuthorize("@svs.validate(T(com.liyu.breeze.common.constant.PrivilegeConstants).ROLE_SELECT)")
    public ResponseEntity<List<RoleDTO>> listAll() {
        List<RoleDTO> list = this.roleService.listAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "新增角色", notes = "新增角色")
    @PreAuthorize("@svs.validate(T(com.liyu.breeze.common.constant.PrivilegeConstants).ROLE_ADD)")
    public ResponseEntity<ResponseVO> addRole(@Validated @RequestBody RoleDTO roleDTO) {
        if (roleDTO.getRoleType() == null) {
            roleDTO.setRoleType(new DictVO(RoleTypeEnum.USER_DEF.getValue(), RoleTypeEnum.USER_DEF.getLabel()));
        }
        String roleCode = Constants.USER_DEFINE_ROLE_PREFIX + roleDTO.getRoleCode();
        roleDTO.setRoleCode(roleCode);
        this.roleService.insert(roleDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }


    @Logging
    @PutMapping
    @ApiOperation(value = "修改角色", notes = "修改角色")
    @PreAuthorize("@svs.validate(T(com.liyu.breeze.common.constant.PrivilegeConstants).ROLE_EDIT)")
    public ResponseEntity<ResponseVO> editRole(@Validated @RequestBody RoleDTO roleDTO) {
        this.roleService.update(roleDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @PreAuthorize("@svs.validate(T(com.liyu.breeze.common.constant.PrivilegeConstants).ROLE_DELETE)")
    public ResponseEntity<ResponseVO> deleteRole(@PathVariable(value = "id") String id) {
        this.roleService.deleteById(Long.valueOf(id));
        this.onlineUserService.disableOnlineCacheRole(Long.valueOf(id));
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/grant")
    @ApiOperation(value = "用户授权角色", notes = "用户授权角色")
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("@svs.validate(T(com.liyu.breeze.common.constant.PrivilegeConstants).ROLE_GRANT)")
    public ResponseEntity<ResponseVO> grantRole(@NotNull Long roleId, @NotNull String userIds) {
        List<Long> userList = JSONUtil.toList(userIds, Long.class);
        List<UserRoleDTO> oldUserList = this.userRoleService.listByRoleId(roleId);
        List<Long> tmpList = new ArrayList<>(userList.size());
        tmpList.addAll(userList);
        //grant new user
        tmpList.removeAll(oldUserList.stream().map(UserRoleDTO::getUserId).collect(Collectors.toList()));
        for (Long userId : tmpList) {
            UserRoleDTO userRole = new UserRoleDTO();
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            this.userRoleService.insert(userRole);
        }
        //revoke removed user
        for (UserRoleDTO userRole : oldUserList) {
            if (!userList.contains(userRole.getUserId())) {
                this.userRoleService.delete(userRole);
            }
        }
        userList.forEach(d -> {
            UserDTO user = this.userService.selectOne(d);
            this.onlineUserService.disableOnlineCacheUser(user.getUserName());
        });
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/dept")
    @ApiOperation(value = "查询部门对应角色列表", notes = "查询部门对应角色列表")
    @PreAuthorize("@svs.validate(T(com.liyu.breeze.common.constant.PrivilegeConstants).DEPT_GRANT)")
    public ResponseEntity<List<RoleDTO>> listRoleByDept(String grant, @NotNull Long deptId) {
        List<RoleDTO> list = this.roleService.selectRoleByDept(grant, deptId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/dept/grant")
    @ApiOperation(value = "部门角色授权", notes = "部门角色授权")
    @PreAuthorize("@svs.validate(T(com.liyu.breeze.common.constant.PrivilegeConstants).DEPT_GRANT)")
    public ResponseEntity<ResponseVO> grantDeptRole(@NotNull DeptRoleDTO deptRole) {
        this.deptRoleService.insert(deptRole);
        List<UserDTO> userList = this.userService.listByDept(deptRole.getDeptId(), "", "1");
        userList.forEach(user -> {
            this.onlineUserService.disableOnlineCacheUser(user.getUserName());
        });
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/dept/revoke")
    @ApiOperation(value = "回收部门角色权限", notes = "回收部门角色权限")
    @PreAuthorize("@svs.validate(T(com.liyu.breeze.common.constant.PrivilegeConstants).DEPT_GRANT)")
    public ResponseEntity<ResponseVO> revokeDeptRole(@NotNull DeptRoleDTO deptRole) {
        this.deptRoleService.delete(deptRole);
        List<UserDTO> userList = this.userService.listByDept(deptRole.getDeptId(), "", "1");
        userList.forEach(user -> {
            this.onlineUserService.disableOnlineCacheUser(user.getUserName());
        });
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}

