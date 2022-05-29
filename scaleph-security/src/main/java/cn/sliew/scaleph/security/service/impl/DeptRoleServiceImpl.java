package cn.sliew.scaleph.security.service.impl;

import java.io.Serializable;

import cn.sliew.scaleph.dao.entity.master.security.DeptRole;
import cn.sliew.scaleph.dao.mapper.master.security.DeptRoleMapper;
import cn.sliew.scaleph.security.service.DeptRoleService;
import cn.sliew.scaleph.security.service.convert.DeptRoleConvert;
import cn.sliew.scaleph.security.service.dto.DeptRoleDTO;
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
public class DeptRoleServiceImpl implements DeptRoleService {

    @Autowired
    private DeptRoleMapper deptRoleMapper;

    @Override
    public int deleteByDeptId(Serializable deptId) {
        return this.deptRoleMapper.delete(new LambdaQueryWrapper<DeptRole>()
            .eq(DeptRole::getDeptId, deptId));
    }

    @Override
    public int deleteByRoleId(Serializable roleId) {
        return this.deptRoleMapper.delete(new LambdaQueryWrapper<DeptRole>()
            .eq(DeptRole::getRoleId, roleId));
    }

    @Override
    public int insert(DeptRoleDTO deptRoleDTO) {
        DeptRole deptRole = DeptRoleConvert.INSTANCE.toDo(deptRoleDTO);
        return this.deptRoleMapper.insert(deptRole);
    }

    @Override
    public int delete(DeptRoleDTO deptRoleDTO) {
        return this.deptRoleMapper.delete(new LambdaQueryWrapper<DeptRole>()
            .eq(DeptRole::getDeptId, deptRoleDTO.getDeptId())
            .eq(DeptRole::getRoleId, deptRoleDTO.getRoleId()));
    }
}
