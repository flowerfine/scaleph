package cn.sliew.scaleph.core.di.service.convert;

import cn.sliew.scaleph.dao.entity.master.di.DiJobStep;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.core.di.service.dto.DiJobStepDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class, DiJobStepAttrConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiJobStepConvert extends BaseConvert<DiJobStep, DiJobStepDTO> {
    DiJobStepConvert INSTANCE = Mappers.getMapper(DiJobStepConvert.class);

    @Override
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.JOB_STEP_TYPE,entity.getStepType()))", target = "stepType")
    DiJobStepDTO toDto(DiJobStep entity);
}
