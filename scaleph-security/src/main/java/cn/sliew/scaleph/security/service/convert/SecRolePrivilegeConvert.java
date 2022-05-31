package cn.sliew.scaleph.security.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.security.SecRolePrivilege;
import cn.sliew.scaleph.security.service.dto.SecRolePrivilegeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface SecRolePrivilegeConvert extends BaseConvert<SecRolePrivilege, SecRolePrivilegeDTO> {

    SecRolePrivilegeConvert INSTANCE = Mappers.getMapper(SecRolePrivilegeConvert.class);
}
