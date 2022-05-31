package cn.sliew.scaleph.security.service.impl;

import java.io.Serializable;
import java.util.List;

import cn.sliew.scaleph.dao.entity.master.security.SecUserRole;
import cn.sliew.scaleph.dao.mapper.master.security.SecUserRoleMapper;
import cn.sliew.scaleph.security.service.SecUserRoleService;
import cn.sliew.scaleph.security.service.convert.SecUserRoleConvert;
import cn.sliew.scaleph.security.service.dto.SecUserRoleDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class SecUserRoleServiceImpl implements SecUserRoleService {

    @Autowired
    private SecUserRoleMapper secUserRoleMapper;

    @Override
    public int insert(SecUserRoleDTO secUserRoleDTO) {
        SecUserRole secUserRole = SecUserRoleConvert.INSTANCE.toDo(secUserRoleDTO);
        return this.secUserRoleMapper.insert(secUserRole);
    }

    @Override
    public int deleteByRoleId(Serializable roleId) {
        return this.secUserRoleMapper.delete(new LambdaQueryWrapper<SecUserRole>()
            .eq(SecUserRole::getRoleId, roleId));
    }

    @Override
    public int delete(SecUserRoleDTO secUserRoleDTO) {
        return this.secUserRoleMapper.delete(new LambdaQueryWrapper<SecUserRole>()
            .eq(SecUserRole::getRoleId, secUserRoleDTO.getRoleId())
            .eq(SecUserRole::getUserId, secUserRoleDTO.getUserId())
        );
    }

    @Override
    public List<SecUserRoleDTO> listByRoleId(Serializable roleId) {
        List<SecUserRole> list = this.secUserRoleMapper.selectList(new LambdaQueryWrapper<SecUserRole>()
            .eq(SecUserRole::getRoleId, roleId));
        return SecUserRoleConvert.INSTANCE.toDto(list);
    }
}
