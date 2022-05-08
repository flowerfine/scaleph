package cn.sliew.breeze.service.convert.di;

import cn.sliew.breeze.dao.entity.DiJobLog;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.convert.DictVoConvert;
import cn.sliew.breeze.service.dto.di.DiJobLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiJobLogConvert extends BaseConvert<DiJobLog, DiJobLogDTO> {
    DiJobLogConvert INSTANCE = Mappers.getMapper(DiJobLogConvert.class);

    @Override
    @Mapping(expression = "java(cn.sliew.breeze.service.vo.DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.JOB_INSTANCE_STATE,entity.getJobInstanceState()))", target = "jobInstanceState")
    DiJobLogDTO toDto(DiJobLog entity);
}
