package cn.sliew.scaleph.security.service.impl;

import java.io.Serializable;
import java.util.List;

import cn.sliew.scaleph.dao.entity.master.security.SecRolePrivilege;
import cn.sliew.scaleph.dao.mapper.master.security.SecRolePrivilegeMapper;
import cn.sliew.scaleph.security.service.SecRolePrivilegeService;
import cn.sliew.scaleph.security.service.convert.SecRolePrivilegeConvert;
import cn.sliew.scaleph.security.service.dto.SecRolePrivilegeDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色权限关联表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class SecRolePrivilegeServiceImpl implements SecRolePrivilegeService {

    @Autowired
    private SecRolePrivilegeMapper secRolePrivilegeMapper;

    @Override
    public int deleteByRoleId(Serializable roleId) {
        return this.secRolePrivilegeMapper.delete(new LambdaQueryWrapper<SecRolePrivilege>()
            .eq(SecRolePrivilege::getRoleId, roleId));
    }

    @Override
    public int insert(SecRolePrivilegeDTO secRolePrivilegeDTO) {
        SecRolePrivilege rp = SecRolePrivilegeConvert.INSTANCE.toDo(secRolePrivilegeDTO);
        return this.secRolePrivilegeMapper.insert(rp);
    }

    @Override
    public int delete(SecRolePrivilegeDTO secRolePrivilegeDTO) {
        return this.secRolePrivilegeMapper.deleteById(secRolePrivilegeDTO.getId());
    }

    @Override
    public List<SecRolePrivilegeDTO> listByRoleId(Serializable roleId) {
        List<SecRolePrivilege> list =
            this.secRolePrivilegeMapper.selectList(new LambdaQueryWrapper<SecRolePrivilege>()
                .eq(SecRolePrivilege::getRoleId, roleId));
        return SecRolePrivilegeConvert.INSTANCE.toDto(list);
    }

    @Override
    public List<SecRolePrivilegeDTO> listByRoleId(Serializable roleId, String resourceType) {
        List<SecRolePrivilege> list = this.secRolePrivilegeMapper.selectByRoleId(roleId, resourceType);
        return SecRolePrivilegeConvert.INSTANCE.toDto(list);
    }
}
