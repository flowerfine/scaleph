package com.liyu.breeze.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyu.breeze.dao.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 查询部门对应的角色信息
     *
     * @param grant  是否授权
     * @param deptId 部门id
     * @return list
     */
    List<Role> selectRoleByDept(@Param("grant") String grant, @Param("deptId") Serializable deptId);
}
