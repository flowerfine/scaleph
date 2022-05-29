package cn.sliew.scaleph.security.service;

import java.io.Serializable;
import java.util.List;

import cn.sliew.scaleph.security.service.dto.UserRoleDTO;

/**
 * <p>
 * 用户角色关联表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface UserRoleService {
    /**
     * 新增用户角色关系
     *
     * @param userRoleDTO userrole
     * @return int
     */
    int insert(UserRoleDTO userRoleDTO);

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
     * @param userRoleDTO userrole
     * @return int
     */
    int delete(UserRoleDTO userRoleDTO);

    /**
     * 查询角色相关用户列表
     *
     * @param roleId role
     * @return list
     */
    List<UserRoleDTO> listByRoleId(Serializable roleId);
}
