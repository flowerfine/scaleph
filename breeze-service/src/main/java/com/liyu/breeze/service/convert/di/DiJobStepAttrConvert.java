package com.liyu.breeze.service.convert.di;

import com.liyu.breeze.dao.entity.DiJobStepAttr;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.dto.di.DiJobStepAttrDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiJobStepAttrConvert extends BaseConvert<DiJobStepAttr, DiJobStepAttrDTO> {
    DiJobStepAttrConvert INSTANCE = Mappers.getMapper(DiJobStepAttrConvert.class);

}
