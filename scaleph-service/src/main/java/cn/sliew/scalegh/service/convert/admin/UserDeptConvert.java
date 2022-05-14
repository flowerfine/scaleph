package cn.sliew.scalegh.service.convert.admin;

import cn.sliew.breeze.dao.entity.master.security.UserDept;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.dto.admin.UserDeptDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface UserDeptConvert extends BaseConvert<UserDept, UserDeptDTO> {

    UserDeptConvert INSTANCE = Mappers.getMapper(UserDeptConvert.class);
}
