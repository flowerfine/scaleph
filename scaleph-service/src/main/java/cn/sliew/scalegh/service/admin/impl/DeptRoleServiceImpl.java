package cn.sliew.scalegh.service.admin.impl;

import cn.sliew.breeze.dao.entity.master.security.DeptRole;
import cn.sliew.breeze.dao.mapper.master.security.DeptRoleMapper;
import cn.sliew.scalegh.service.admin.DeptRoleService;
import cn.sliew.scalegh.service.convert.admin.DeptRoleConvert;
import cn.sliew.scalegh.service.dto.admin.DeptRoleDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

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
