package com.liyu.breeze.service.convert.admin;

import com.liyu.breeze.dao.entity.Role;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.convert.DictVoConvert;
import com.liyu.breeze.service.dto.admin.RoleDTO;
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
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.ROLE_TYPE,entity.getRoleType()))", target = "roleType")
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.ROLE_STATUS,entity.getRoleStatus()))", target = "roleStatus")
    RoleDTO toDto(Role entity);
}
