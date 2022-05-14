package cn.sliew.breeze.service.convert.admin;

import cn.sliew.breeze.dao.entity.master.security.Role;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.convert.DictVoConvert;
import cn.sliew.breeze.service.dto.admin.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class, PrivilegeConvert.class})
public interface RoleConvert extends BaseConvert<Role, RoleDTO> {
    RoleConvert INSTANCE = Mappers.getMapper(RoleConvert.class);

    @Override
    @Mapping(expression = "java(cn.sliew.breeze.service.vo.DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.ROLE_TYPE,entity.getRoleType()))", target = "roleType")
    @Mapping(expression = "java(cn.sliew.breeze.service.vo.DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.ROLE_STATUS,entity.getRoleStatus()))", target = "roleStatus")
    RoleDTO toDto(Role entity);
}
