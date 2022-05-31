package cn.sliew.scaleph.security.service;

import java.io.Serializable;
import java.util.List;

import cn.sliew.scaleph.security.service.dto.SecRolePrivilegeDTO;

/**
 * <p>
 * 角色权限关联表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface SecRolePrivilegeService {
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
     * @param secRolePrivilegeDTO role privilege
     * @return int
     */
    int insert(SecRolePrivilegeDTO secRolePrivilegeDTO);

    /**
     * 删除角色权限关系
     *
     * @param secRolePrivilegeDTO role privilege
     * @return int
     */
    int delete(SecRolePrivilegeDTO secRolePrivilegeDTO);

    /**
     * 查询角色相关权限列表
     *
     * @param roleId role
     * @return list
     */
    List<SecRolePrivilegeDTO> listByRoleId(Serializable roleId);

    /**
     * 查询角色相关权限列表
     *
     * @param roleId       role
     * @param resourceType resource Type
     * @return list
     */
    List<SecRolePrivilegeDTO> listByRoleId(Serializable roleId, String resourceType);
}
