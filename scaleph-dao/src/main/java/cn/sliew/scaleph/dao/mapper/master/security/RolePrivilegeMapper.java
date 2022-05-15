package cn.sliew.scaleph.dao.mapper.master.security;

import cn.sliew.scaleph.dao.entity.master.security.RolePrivilege;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
