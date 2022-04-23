package com.liyu.breeze.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liyu.breeze.dao.entity.DeptRole;
import com.liyu.breeze.dao.mapper.DeptRoleMapper;
import com.liyu.breeze.service.admin.DeptRoleService;
import com.liyu.breeze.service.convert.admin.DeptRoleConvert;
import com.liyu.breeze.service.dto.admin.DeptRoleDTO;
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
