package cn.sliew.scaleph.service.convert.admin;

import cn.sliew.scaleph.dao.entity.master.security.User;
import cn.sliew.scaleph.service.convert.BaseConvert;
import cn.sliew.scaleph.service.convert.DictVoConvert;
import cn.sliew.scaleph.service.dto.admin.UserDTO;
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
    @Mapping(expression = "java(cn.sliew.scaleph.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.ID_CARD_TYPE,entity.getIdCardType()))", target = "idCardType")
    @Mapping(expression = "java(cn.sliew.scaleph.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.GENDER,entity.getGender()))", target = "gender")
    @Mapping(expression = "java(cn.sliew.scaleph.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.NATION,entity.getNation()))", target = "nation")
    @Mapping(expression = "java(cn.sliew.scaleph.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.USER_STATUS,entity.getUserStatus()))", target = "userStatus")
    @Mapping(expression = "java(cn.sliew.scaleph.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.REGISTER_CHANNEL,entity.getRegisterChannel()))", target = "registerChannel")
    UserDTO toDto(User entity);
}
