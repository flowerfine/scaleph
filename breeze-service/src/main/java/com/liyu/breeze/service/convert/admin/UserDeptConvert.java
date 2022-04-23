package com.liyu.breeze.service.convert.admin;

import com.liyu.breeze.dao.entity.UserDept;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.dto.admin.UserDeptDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface UserDeptConvert extends BaseConvert<UserDept, UserDeptDTO> {

    UserDeptConvert INSTANCE = Mappers.getMapper(UserDeptConvert.class);
}
