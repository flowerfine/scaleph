package cn.sliew.scaleph.service.convert.admin;

import cn.sliew.scaleph.dao.entity.master.security.RolePrivilege;
import cn.sliew.scaleph.service.convert.BaseConvert;
import cn.sliew.scaleph.service.dto.admin.RolePrivilegeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface RolePrivilegeConvert extends BaseConvert<RolePrivilege, RolePrivilegeDTO> {

    RolePrivilegeConvert INSTANCE = Mappers.getMapper(RolePrivilegeConvert.class);
}
