package cn.sliew.scaleph.service.convert.admin;

import cn.sliew.scaleph.dao.entity.log.ScheduleLog;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.service.dto.admin.ScheduleLogDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleLogConvert extends BaseConvert<ScheduleLog, ScheduleLogDTO> {
    ScheduleLogConvert INSTANCE = Mappers.getMapper(ScheduleLogConvert.class);

    @Override
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.TASK_RESULT,entity.getResult()))", target = "result")
    ScheduleLogDTO toDto(ScheduleLog entity);
}
