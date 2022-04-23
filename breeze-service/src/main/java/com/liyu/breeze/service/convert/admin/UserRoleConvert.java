package com.liyu.breeze.service.convert.admin;

import com.liyu.breeze.dao.entity.UserRole;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.dto.admin.UserRoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface UserRoleConvert extends BaseConvert<UserRole, UserRoleDTO> {

    UserRoleConvert INSTANCE = Mappers.getMapper(UserRoleConvert.class);
}
