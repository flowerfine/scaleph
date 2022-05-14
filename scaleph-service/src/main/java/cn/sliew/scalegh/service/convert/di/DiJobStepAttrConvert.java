package cn.sliew.scalegh.service.convert.di;

import cn.sliew.breeze.dao.entity.master.di.DiJobStepAttr;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.dto.di.DiJobStepAttrDTO;
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
