package cn.sliew.scaleph.security.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.security.SecDeptRole;
import cn.sliew.scaleph.security.service.dto.SecDeptRoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface SecDeptRoleConvert extends BaseConvert<SecDeptRole, SecDeptRoleDTO> {

    SecDeptRoleConvert INSTANCE = Mappers.getMapper(SecDeptRoleConvert.class);
}
