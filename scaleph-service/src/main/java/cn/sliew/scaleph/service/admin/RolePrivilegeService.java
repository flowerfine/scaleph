package cn.sliew.scaleph.service.admin;

import cn.sliew.scaleph.service.dto.admin.RolePrivilegeDTO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 角色权限关联表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface RolePrivilegeService {
    /**
     * 根据角色删除
     *
     * @param roleId roleid
     * @return int
     */
    int deleteByRoleId(Serializable roleId);

    /**
     * 新增角色权限关系
     *
     * @param rolePrivilegeDTO role privilege
     * @return int
     */
    int insert(RolePrivilegeDTO rolePrivilegeDTO);

    /**
     * 删除角色权限关系
     *
     * @param rolePrivilegeDTO role privilege
     * @return int
     */
    int delete(RolePrivilegeDTO rolePrivilegeDTO);

    /**
     * 查询角色相关权限列表
     *
     * @param roleId role
     * @return list
     */
    List<RolePrivilegeDTO> listByRoleId(Serializable roleId);

    /**
     * 查询角色相关权限列表
     *
     * @param roleId       role
     * @param resourceType resource Type
     * @return list
     */
    List<RolePrivilegeDTO> listByRoleId(Serializable roleId, String resourceType);
}
