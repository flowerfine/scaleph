package cn.sliew.scaleph.security.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.security.UserDept;
import cn.sliew.scaleph.security.service.dto.UserDeptDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface UserDeptConvert extends BaseConvert<UserDept, UserDeptDTO> {

    UserDeptConvert INSTANCE = Mappers.getMapper(UserDeptConvert.class);
}
