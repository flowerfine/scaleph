package cn.sliew.scaleph.security.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.security.Role;
import cn.sliew.scaleph.security.service.dto.RoleDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
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
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.ROLE_TYPE,entity.getRoleType()))", target = "roleType")
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.ROLE_STATUS,entity.getRoleStatus()))", target = "roleStatus")
    RoleDTO toDto(Role entity);
}
