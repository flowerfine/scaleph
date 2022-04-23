package com.liyu.breeze.service.convert.admin;

import com.liyu.breeze.dao.entity.DeptRole;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.dto.admin.DeptRoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface DeptRoleConvert extends BaseConvert<DeptRole, DeptRoleDTO> {

    DeptRoleConvert INSTANCE = Mappers.getMapper(DeptRoleConvert.class);
}
