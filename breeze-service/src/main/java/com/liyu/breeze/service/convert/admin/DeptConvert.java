package com.liyu.breeze.service.convert.admin;

import com.liyu.breeze.dao.entity.Dept;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.convert.DictVoConvert;
import com.liyu.breeze.service.dto.admin.DeptDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = DictVoConvert.class)
public interface DeptConvert extends BaseConvert<Dept, DeptDTO> {
    DeptConvert INSTANCE = Mappers.getMapper(DeptConvert.class);

    @Override
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.DEPT_STATUS,entity.getDeptStatus()))", target = "deptStatus")
    DeptDTO toDto(Dept entity);
}
