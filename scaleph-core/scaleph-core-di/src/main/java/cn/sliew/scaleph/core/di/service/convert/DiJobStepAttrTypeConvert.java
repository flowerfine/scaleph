package cn.sliew.scaleph.core.di.service.convert;

import cn.sliew.scaleph.dao.entity.master.di.DiJobStepAttrType;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.core.di.service.dto.DiJobStepAttrTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiJobStepAttrTypeConvert extends BaseConvert<DiJobStepAttrType, DiJobStepAttrTypeDTO> {
    DiJobStepAttrTypeConvert INSTANCE = Mappers.getMapper(DiJobStepAttrTypeConvert.class);

}
