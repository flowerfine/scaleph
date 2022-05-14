package cn.sliew.scalegh.service.convert.admin;

import cn.sliew.breeze.dao.entity.master.security.User;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.convert.DictVoConvert;
import cn.sliew.scalegh.service.dto.admin.UserDTO;
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
    @Mapping(expression = "java(DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.ID_CARD_TYPE,entity.getIdCardType()))", target = "idCardType")
    @Mapping(expression = "java(DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.GENDER,entity.getGender()))", target = "gender")
    @Mapping(expression = "java(DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.NATION,entity.getNation()))", target = "nation")
    @Mapping(expression = "java(DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.USER_STATUS,entity.getUserStatus()))", target = "userStatus")
    @Mapping(expression = "java(DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.REGISTER_CHANNEL,entity.getRegisterChannel()))", target = "registerChannel")
    UserDTO toDto(User entity);
}
