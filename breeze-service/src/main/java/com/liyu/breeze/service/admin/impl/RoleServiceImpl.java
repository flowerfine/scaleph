package com.liyu.breeze.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liyu.breeze.dao.entity.Role;
import com.liyu.breeze.dao.mapper.RoleMapper;
import com.liyu.breeze.service.admin.DeptRoleService;
import com.liyu.breeze.service.admin.RolePrivilegeService;
import com.liyu.breeze.service.admin.RoleService;
import com.liyu.breeze.service.admin.UserRoleService;
import com.liyu.breeze.service.convert.admin.RoleConvert;
import com.liyu.breeze.service.dto.admin.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePrivilegeService rolePrivilegeService;
    @Autowired
    private DeptRoleService deptRoleService;

    @Override
    public int insert(RoleDTO roleDTO) {
        Role role = RoleConvert.INSTANCE.toDo(roleDTO);
        return this.roleMapper.insert(role);
    }

    @Override
    public int update(RoleDTO roleDTO) {
        Role role = RoleConvert.INSTANCE.toDo(roleDTO);
        return this.roleMapper.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        this.userRoleService.deleteByRoleId(id);
        this.rolePrivilegeService.deleteByRoleId(id);
        this.deptRoleService.deleteByRoleId(id);
        return this.roleMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        if (CollectionUtils.isEmpty(map)) {
            return 0;
        }
        for (Serializable id : map.values()) {
            this.userRoleService.deleteByRoleId(id);
            this.rolePrivilegeService.deleteByRoleId(id);
            this.deptRoleService.deleteByRoleId(id);
        }
        return this.roleMapper.deleteBatchIds(map.values());
    }

    @Override
    public RoleDTO selectOne(Long id) {
        Role role = this.roleMapper.selectById(id);
        return RoleConvert.INSTANCE.toDto(role);
    }

    @Override
    public RoleDTO selectOne(String roleCode) {
        Role role = this.roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleCode, roleCode));
        return RoleConvert.INSTANCE.toDto(role);
    }

    @Override
    public List<RoleDTO> listAll() {
        List<Role> list = this.roleMapper.selectList(new LambdaQueryWrapper<Role>().orderByAsc(Role::getCreateTime));
        return RoleConvert.INSTANCE.toDto(list);
    }

    @Override
    public List<RoleDTO> selectRoleByDept(String grant, Long deptId) {
        List<Role> list = this.roleMapper.selectRoleByDept(grant, deptId);
        return RoleConvert.INSTANCE.toDto(list);
    }
}
