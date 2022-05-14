package cn.sliew.scalegh.service.convert.admin;

import cn.sliew.breeze.dao.entity.master.security.DeptRole;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.dto.admin.DeptRoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface DeptRoleConvert extends BaseConvert<DeptRole, DeptRoleDTO> {

    DeptRoleConvert INSTANCE = Mappers.getMapper(DeptRoleConvert.class);
}
