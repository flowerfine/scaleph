package cn.sliew.breeze.service.convert.admin;

import cn.sliew.breeze.dao.entity.master.system.UserActive;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.dto.admin.UserActiveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface UserActiveConvert extends BaseConvert<UserActive, UserActiveDTO> {

    UserActiveConvert INSTANCE = Mappers.getMapper(UserActiveConvert.class);
}
