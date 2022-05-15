package cn.sliew.scaleph.service.convert.admin;

import cn.sliew.scaleph.dao.entity.master.system.UserActive;
import cn.sliew.scaleph.service.convert.BaseConvert;
import cn.sliew.scaleph.service.dto.admin.UserActiveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface UserActiveConvert extends BaseConvert<UserActive, UserActiveDTO> {

    UserActiveConvert INSTANCE = Mappers.getMapper(UserActiveConvert.class);
}
