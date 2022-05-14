package cn.sliew.scalegh.service.convert.admin;

import cn.sliew.scalegh.dao.entity.log.ScheduleLog;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.convert.DictVoConvert;
import cn.sliew.scalegh.service.dto.admin.ScheduleLogDTO;
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
    @Mapping(expression = "java(cn.sliew.scalegh.service.vo.DictVO.toVO(cn.sliew.scalegh.common.constant.DictConstants.TASK_RESULT,entity.getResult()))", target = "result")
    ScheduleLogDTO toDto(ScheduleLog entity);
}
