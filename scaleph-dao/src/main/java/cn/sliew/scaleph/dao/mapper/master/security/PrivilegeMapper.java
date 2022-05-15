package cn.sliew.scaleph.dao.mapper.master.security;

import cn.sliew.scaleph.dao.entity.master.security.Privilege;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Repository
public interface PrivilegeMapper extends BaseMapper<Privilege> {
    /**
     * 查询角色对应权限信息
     *
     * @param roleId role id
     * @return list
     */
//    List<Privilege> selectByRoleId(Serializable roleId);
}
