package cn.sliew.scalegh.service.convert.admin;

import cn.sliew.breeze.dao.entity.master.security.Role;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.convert.DictVoConvert;
import cn.sliew.scalegh.service.dto.admin.RoleDTO;
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
    @Mapping(expression = "java(DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.ROLE_TYPE,entity.getRoleType()))", target = "roleType")
    @Mapping(expression = "java(DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.ROLE_STATUS,entity.getRoleStatus()))", target = "roleStatus")
    RoleDTO toDto(Role entity);
}
