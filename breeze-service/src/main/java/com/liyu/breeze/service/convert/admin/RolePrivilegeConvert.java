package com.liyu.breeze.service.convert.admin;

import com.liyu.breeze.dao.entity.RolePrivilege;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.dto.admin.RolePrivilegeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface RolePrivilegeConvert extends BaseConvert<RolePrivilege, RolePrivilegeDTO> {

    RolePrivilegeConvert INSTANCE = Mappers.getMapper(RolePrivilegeConvert.class);
}
