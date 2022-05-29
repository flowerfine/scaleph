package cn.sliew.scaleph.core.di.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.core.di.service.dto.DiJobStepAttrDTO;
import cn.sliew.scaleph.dao.entity.master.di.DiJobStepAttr;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiJobStepAttrConvert extends BaseConvert<DiJobStepAttr, DiJobStepAttrDTO> {
    DiJobStepAttrConvert INSTANCE = Mappers.getMapper(DiJobStepAttrConvert.class);

}
