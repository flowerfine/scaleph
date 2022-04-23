package com.liyu.breeze.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyu.breeze.dao.entity.RolePrivilege;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 角色权限关联表 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Repository
public interface RolePrivilegeMapper extends BaseMapper<RolePrivilege> {
    /**
     * 查询角色对应的权限信息
     *
     * @param roleId       role
     * @param resourceType resource type
     * @return privilege list
     */
    List<RolePrivilege> selectByRoleId(@Param("roleId") Serializable roleId, @Param("resourceType") String resourceType);
}
