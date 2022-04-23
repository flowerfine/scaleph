package com.liyu.breeze.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyu.breeze.dao.entity.Privilege;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

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
