package cn.sliew.scaleph.log.service.convert;

import cn.sliew.scaleph.dao.entity.log.LogSchedule;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.log.service.dto.LogScheduleDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogScheduleConvert extends BaseConvert<LogSchedule, LogScheduleDTO> {
    LogScheduleConvert INSTANCE = Mappers.getMapper(LogScheduleConvert.class);

    @Override
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.TASK_RESULT,entity.getResult()))", target = "result")
    LogScheduleDTO toDto(LogSchedule entity);
}
