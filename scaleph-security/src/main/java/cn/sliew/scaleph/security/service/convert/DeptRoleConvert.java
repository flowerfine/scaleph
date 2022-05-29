package cn.sliew.scaleph.security.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.security.DeptRole;
import cn.sliew.scaleph.security.service.dto.DeptRoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface DeptRoleConvert extends BaseConvert<DeptRole, DeptRoleDTO> {

    DeptRoleConvert INSTANCE = Mappers.getMapper(DeptRoleConvert.class);
}
