package com.liyu.breeze.service.convert.di;

import com.liyu.breeze.dao.entity.DiJobStepAttrType;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.dto.di.DiJobStepAttrTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiJobStepAttrTypeConvert extends BaseConvert<DiJobStepAttrType, DiJobStepAttrTypeDTO> {
    DiJobStepAttrTypeConvert INSTANCE = Mappers.getMapper(DiJobStepAttrTypeConvert.class);

}
