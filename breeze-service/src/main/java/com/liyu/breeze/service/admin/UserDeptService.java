package com.liyu.breeze.service.admin;

import com.liyu.breeze.service.dto.admin.UserDeptDTO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户和部门关联表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface UserDeptService {
    /**
     * 新增用户部门关系
     *
     * @param userDeptDTO user dept
     * @return int
     */
    int insert(UserDeptDTO userDeptDTO);

    /**
     * 根据部门删除
     *
     * @param deptId deptid
     * @return int
     */
    int deleteBydeptId(Serializable deptId);

    /**
     * 删除用户部门关系
     *
     * @param userDeptDTO user dept
     * @return int
     */
    int delete(UserDeptDTO userDeptDTO);

    /**
     * 查询部门下用户列表
     *
     * @param deptId dept
     * @return list
     */
    List<UserDeptDTO> listByDeptId(Serializable deptId);
}
