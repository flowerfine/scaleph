package cn.sliew.breeze.service.convert.admin;

import cn.sliew.breeze.dao.entity.master.security.RolePrivilege;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.dto.admin.RolePrivilegeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface RolePrivilegeConvert extends BaseConvert<RolePrivilege, RolePrivilegeDTO> {

    RolePrivilegeConvert INSTANCE = Mappers.getMapper(RolePrivilegeConvert.class);
}
