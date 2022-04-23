package com.liyu.breeze.service.convert.admin;

import com.liyu.breeze.dao.entity.Privilege;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.convert.DictVoConvert;
import com.liyu.breeze.service.dto.admin.PrivilegeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = DictVoConvert.class)
public interface PrivilegeConvert extends BaseConvert<Privilege, PrivilegeDTO> {

    PrivilegeConvert INSTANCE = Mappers.getMapper(PrivilegeConvert.class);

    @Override
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.RESOURCE_TYPE,entity.getResourceType()))", target = "resourceType")
    PrivilegeDTO toDto(Privilege entity);
}
