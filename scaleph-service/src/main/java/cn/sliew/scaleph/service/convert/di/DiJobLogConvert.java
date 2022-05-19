package cn.sliew.scaleph.service.convert.di;

import cn.sliew.scaleph.dao.entity.master.di.DiJobLog;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.service.dto.di.DiJobLogDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class, DiProjectConvert.class, DiClusterConfigConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiJobLogConvert extends BaseConvert<DiJobLog, DiJobLogDTO> {
    DiJobLogConvert INSTANCE = Mappers.getMapper(DiJobLogConvert.class);

    @Override
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.JOB_INSTANCE_STATE,entity.getJobInstanceState()))", target = "jobInstanceState")
    DiJobLogDTO toDto(DiJobLog entity);
}
