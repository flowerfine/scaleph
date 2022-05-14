package cn.sliew.scalegh.service.admin.impl;

import cn.sliew.breeze.dao.entity.master.security.UserRole;
import cn.sliew.breeze.dao.mapper.master.security.UserRoleMapper;
import cn.sliew.scalegh.service.admin.UserRoleService;
import cn.sliew.scalegh.service.convert.admin.UserRoleConvert;
import cn.sliew.scalegh.service.dto.admin.UserRoleDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public int insert(UserRoleDTO userRoleDTO) {
        UserRole userRole = UserRoleConvert.INSTANCE.toDo(userRoleDTO);
        return this.userRoleMapper.insert(userRole);
    }

    @Override
    public int deleteByRoleId(Serializable roleId) {
        return this.userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleId, roleId));
    }

    @Override
    public int delete(UserRoleDTO userRoleDTO) {
        return this.userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleId, userRoleDTO.getRoleId())
                .eq(UserRole::getUserId, userRoleDTO.getUserId())
        );
    }

    @Override
    public List<UserRoleDTO> listByRoleId(Serializable roleId) {
        List<UserRole> list = this.userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleId, roleId));
        return UserRoleConvert.INSTANCE.toDto(list);
    }
}
