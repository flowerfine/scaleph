package com.liyu.breeze.service.convert.di;

import com.liyu.breeze.dao.entity.DiJobAttr;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.convert.DictVoConvert;
import com.liyu.breeze.service.dto.di.DiJobAttrDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiJobAttrConvert extends BaseConvert<DiJobAttr, DiJobAttrDTO> {
    DiJobAttrConvert INSTANCE = Mappers.getMapper(DiJobAttrConvert.class);

    @Override
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.JOB_ATTR_TYPE,entity.getJobAttrType()))", target = "jobAttrType")
    DiJobAttrDTO toDto(DiJobAttr entity);
}
