package cn.sliew.breeze.service.convert.di;

import cn.sliew.breeze.dao.entity.DiJobStep;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.convert.DictVoConvert;
import cn.sliew.breeze.service.dto.di.DiJobStepDTO;
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
    @Mapping(expression = "java(cn.sliew.breeze.service.vo.DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.JOB_STEP_TYPE,entity.getStepType()))", target = "stepType")
    DiJobStepDTO toDto(DiJobStep entity);
}
