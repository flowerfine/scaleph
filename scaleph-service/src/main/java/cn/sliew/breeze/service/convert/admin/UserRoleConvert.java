package cn.sliew.breeze.service.convert.admin;

import cn.sliew.breeze.dao.entity.master.security.UserRole;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.dto.admin.UserRoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface UserRoleConvert extends BaseConvert<UserRole, UserRoleDTO> {

    UserRoleConvert INSTANCE = Mappers.getMapper(UserRoleConvert.class);
}
