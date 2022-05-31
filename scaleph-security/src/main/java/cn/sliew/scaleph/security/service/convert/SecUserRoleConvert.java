package cn.sliew.scaleph.security.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.security.SecUserRole;
import cn.sliew.scaleph.security.service.dto.SecUserRoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface SecUserRoleConvert extends BaseConvert<SecUserRole, SecUserRoleDTO> {

    SecUserRoleConvert INSTANCE = Mappers.getMapper(SecUserRoleConvert.class);
}
