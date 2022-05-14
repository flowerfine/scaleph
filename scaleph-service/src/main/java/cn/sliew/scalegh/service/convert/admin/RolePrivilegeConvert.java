package cn.sliew.scalegh.service.convert.admin;

import cn.sliew.breeze.dao.entity.master.security.RolePrivilege;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.dto.admin.RolePrivilegeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface RolePrivilegeConvert extends BaseConvert<RolePrivilege, RolePrivilegeDTO> {

    RolePrivilegeConvert INSTANCE = Mappers.getMapper(RolePrivilegeConvert.class);
}
