package cn.sliew.scaleph.security.service.impl;

import java.io.Serializable;

import cn.sliew.scaleph.dao.entity.master.security.SecDeptRole;
import cn.sliew.scaleph.dao.mapper.master.security.SecDeptRoleMapper;
import cn.sliew.scaleph.security.service.SecDeptRoleService;
import cn.sliew.scaleph.security.service.convert.SecDeptRoleConvert;
import cn.sliew.scaleph.security.service.dto.SecDeptRoleDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门角色关联表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class SecDeptRoleServiceImpl implements SecDeptRoleService {

    @Autowired
    private SecDeptRoleMapper secDeptRoleMapper;

    @Override
    public int deleteByDeptId(Serializable deptId) {
        return this.secDeptRoleMapper.delete(new LambdaQueryWrapper<SecDeptRole>()
            .eq(SecDeptRole::getDeptId, deptId));
    }

    @Override
    public int deleteByRoleId(Serializable roleId) {
        return this.secDeptRoleMapper.delete(new LambdaQueryWrapper<SecDeptRole>()
            .eq(SecDeptRole::getRoleId, roleId));
    }

    @Override
    public int insert(SecDeptRoleDTO secDeptRoleDTO) {
        SecDeptRole secDeptRole = SecDeptRoleConvert.INSTANCE.toDo(secDeptRoleDTO);
        return this.secDeptRoleMapper.insert(secDeptRole);
    }

    @Override
    public int delete(SecDeptRoleDTO secDeptRoleDTO) {
        return this.secDeptRoleMapper.delete(new LambdaQueryWrapper<SecDeptRole>()
            .eq(SecDeptRole::getDeptId, secDeptRoleDTO.getDeptId())
            .eq(SecDeptRole::getRoleId, secDeptRoleDTO.getRoleId()));
    }
}
