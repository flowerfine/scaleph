package cn.sliew.scalegh.service.convert.di;

import cn.sliew.breeze.dao.entity.master.di.DiJobStep;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.convert.DictVoConvert;
import cn.sliew.scalegh.service.dto.di.DiJobStepDTO;
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
    @Mapping(expression = "java(DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.JOB_STEP_TYPE,entity.getStepType()))", target = "stepType")
    DiJobStepDTO toDto(DiJobStep entity);
}
