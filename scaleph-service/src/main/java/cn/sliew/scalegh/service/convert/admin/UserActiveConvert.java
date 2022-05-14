package cn.sliew.scalegh.service.convert.admin;

import cn.sliew.breeze.dao.entity.master.system.UserActive;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.dto.admin.UserActiveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface UserActiveConvert extends BaseConvert<UserActive, UserActiveDTO> {

    UserActiveConvert INSTANCE = Mappers.getMapper(UserActiveConvert.class);
}
