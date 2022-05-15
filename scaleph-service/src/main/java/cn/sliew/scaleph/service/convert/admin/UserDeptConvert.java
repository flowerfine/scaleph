package cn.sliew.scaleph.service.convert.admin;

import cn.sliew.scaleph.dao.entity.master.security.UserDept;
import cn.sliew.scaleph.service.convert.BaseConvert;
import cn.sliew.scaleph.service.dto.admin.UserDeptDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface UserDeptConvert extends BaseConvert<UserDept, UserDeptDTO> {

    UserDeptConvert INSTANCE = Mappers.getMapper(UserDeptConvert.class);
}
