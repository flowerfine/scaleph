package cn.sliew.breeze.service.convert.admin;

import cn.sliew.breeze.dao.entity.DeptRole;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.dto.admin.DeptRoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface DeptRoleConvert extends BaseConvert<DeptRole, DeptRoleDTO> {

    DeptRoleConvert INSTANCE = Mappers.getMapper(DeptRoleConvert.class);
}
