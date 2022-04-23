package com.liyu.breeze.service.convert.admin;

import com.liyu.breeze.dao.entity.UserActive;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.dto.admin.UserActiveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface UserActiveConvert extends BaseConvert<UserActive, UserActiveDTO> {

    UserActiveConvert INSTANCE = Mappers.getMapper(UserActiveConvert.class);
}
