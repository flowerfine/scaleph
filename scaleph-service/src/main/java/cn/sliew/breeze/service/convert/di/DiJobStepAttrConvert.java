package cn.sliew.breeze.service.convert.di;

import cn.sliew.breeze.dao.entity.master.di.DiJobStepAttr;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.dto.di.DiJobStepAttrDTO;
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
