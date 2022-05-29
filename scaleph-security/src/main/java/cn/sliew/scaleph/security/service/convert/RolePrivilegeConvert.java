package cn.sliew.scaleph.security.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.security.RolePrivilege;
import cn.sliew.scaleph.security.service.dto.RolePrivilegeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface RolePrivilegeConvert extends BaseConvert<RolePrivilege, RolePrivilegeDTO> {

    RolePrivilegeConvert INSTANCE = Mappers.getMapper(RolePrivilegeConvert.class);
}
