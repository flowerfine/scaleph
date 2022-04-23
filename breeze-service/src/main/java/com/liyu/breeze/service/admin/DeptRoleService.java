package com.liyu.breeze.service.admin;

import com.liyu.breeze.service.dto.admin.DeptRoleDTO;

import java.io.Serializable;

/**
 * <p>
 * 部门角色关联表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface DeptRoleService {
    /**
     * 根据部门删除
     *
     * @param deptId deptid
     * @return int
     */
    int deleteByDeptId(Serializable deptId);

    /**
     * 根据角色删除
     *
     * @param roleId role id
     * @return int
     */
    int deleteByRoleId(Serializable roleId);

    /**
     * 授权
     *
     * @param deptRoleDTO dept role
     * @return int
     */
    int insert(DeptRoleDTO deptRoleDTO);

    /**
     * 删除授权
     *
     * @param deptRoleDTO dept role
     * @return int
     */
    int delete(DeptRoleDTO deptRoleDTO);
}
