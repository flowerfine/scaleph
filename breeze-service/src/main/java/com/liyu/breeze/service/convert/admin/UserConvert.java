package com.liyu.breeze.service.convert.admin;

import com.liyu.breeze.dao.entity.User;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.convert.DictVoConvert;
import com.liyu.breeze.service.dto.admin.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class})
public interface UserConvert extends BaseConvert<User, UserDTO> {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    @Override
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.ID_CARD_TYPE,entity.getIdCardType()))", target = "idCardType")
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.GENDER,entity.getGender()))", target = "gender")
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.NATION,entity.getNation()))", target = "nation")
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.USER_STATUS,entity.getUserStatus()))", target = "userStatus")
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.REGISTER_CHANNEL,entity.getRegisterChannel()))", target = "registerChannel")
    UserDTO toDto(User entity);
}
