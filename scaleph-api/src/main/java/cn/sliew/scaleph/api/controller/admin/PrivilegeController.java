package cn.sliew.scaleph.api.controller.admin;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.json.JSONUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.api.security.OnlineUserService;
import cn.sliew.scaleph.api.vo.ResponseVO;
import cn.sliew.scaleph.security.service.PrivilegeService;
import cn.sliew.scaleph.security.service.RolePrivilegeService;
import cn.sliew.scaleph.security.service.dto.PrivilegeDTO;
import cn.sliew.scaleph.security.service.dto.RolePrivilegeDTO;
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
@Api(tags = "系统管理-权限管理")
public class PrivilegeController {

    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private RolePrivilegeService rolePrivilegeService;
    @Autowired
    private OnlineUserService onlineUserService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询权限树", notes = "查询权限树")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).ROLE_GRANT)")
    public ResponseEntity<List<Tree<Long>>> listAllPrivilege(@NotNull String resourceType) {
        List<PrivilegeDTO> privilegeList = this.privilegeService.listAll(resourceType);
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("privilegeId");
        treeNodeConfig.setParentIdKey("pid");
        treeNodeConfig.setNameKey("privilegeName");
        treeNodeConfig.setWeightKey("privilegeCode");
        List<Tree<Long>> treeList = TreeUtil.build(privilegeList, 0L, treeNodeConfig, (treeNode, tree) -> {
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
    public ResponseEntity<List<PrivilegeDTO>> listPrivilege(@NotNull Long roleId, @NotNull String resourceType) {
        List<PrivilegeDTO> result = new ArrayList<>();
        this.rolePrivilegeService.listByRoleId(roleId, resourceType).forEach(d -> {
            PrivilegeDTO p = new PrivilegeDTO();
            p.setId(d.getPrivilegeId());
            result.add(p);
        });
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "角色授权权限", notes = "角色授权权限")
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).ROLE_GRANT)")
    public ResponseEntity<ResponseVO> grantPrivilege(@NotNull Long roleId, @NotNull String privilegeIds, @NotNull String resourceType) {
        List<Long> privilegeList = JSONUtil.toList(privilegeIds, Long.class);
        List<RolePrivilegeDTO> oldPrivilegeList = this.rolePrivilegeService.listByRoleId(roleId, resourceType);
        List<Long> tmpList = new ArrayList<>(privilegeList.size());
        tmpList.addAll(privilegeList);
        //grant new user
        tmpList.removeAll(oldPrivilegeList.stream().map(RolePrivilegeDTO::getPrivilegeId).collect(Collectors.toList()));
        for (Long privilegeId : tmpList) {
            RolePrivilegeDTO userPrivilege = new RolePrivilegeDTO();
            userPrivilege.setRoleId(roleId);
            userPrivilege.setPrivilegeId(privilegeId);
            this.rolePrivilegeService.insert(userPrivilege);
        }
        //revoke removed user
        for (RolePrivilegeDTO rolePrivilege : oldPrivilegeList) {
            if (!privilegeList.contains(rolePrivilege.getPrivilegeId())) {
                this.rolePrivilegeService.delete(rolePrivilege);
            }
        }
        this.onlineUserService.disableOnlineCacheRole(roleId);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

}

