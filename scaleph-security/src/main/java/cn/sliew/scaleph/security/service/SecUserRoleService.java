package cn.sliew.scaleph.security.service;

import java.io.Serializable;
import java.util.List;

import cn.sliew.scaleph.security.service.dto.SecUserRoleDTO;

/**
 * <p>
 * 用户角色关联表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface SecUserRoleService {
    /**
     * 新增用户角色关系
     *
     * @param secUserRoleDTO userrole
     * @return int
     */
    int insert(SecUserRoleDTO secUserRoleDTO);

    /**
     * 根据角色删除
     *
     * @param roleId roleid
     * @return int
     */
    int deleteByRoleId(Serializable roleId);

    /**
     * 删除用户角色关系
     *
     * @param secUserRoleDTO userrole
     * @return int
     */
    int delete(SecUserRoleDTO secUserRoleDTO);

    /**
     * 查询角色相关用户列表
     *
     * @param roleId role
     * @return list
     */
    List<SecUserRoleDTO> listByRoleId(Serializable roleId);
}
