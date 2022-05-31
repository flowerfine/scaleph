package cn.sliew.scaleph.dao.mapper.master.security;

import java.io.Serializable;
import java.util.List;

import cn.sliew.scaleph.dao.entity.master.security.SecRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Repository
public interface SecRoleMapper extends BaseMapper<SecRole> {
    /**
     * 查询部门对应的角色信息
     *
     * @param grant  是否授权
     * @param deptId 部门id
     * @return list
     */
    List<SecRole> selectRoleByDept(@Param("grant") String grant, @Param("deptId") Serializable deptId);
}
