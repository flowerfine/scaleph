package cn.sliew.scaleph.log.service.convert;

import cn.sliew.scaleph.dao.entity.log.LogAction;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.log.service.dto.LogActionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogActionConvert extends BaseConvert<LogAction, LogActionDTO> {
    LogActionConvert INSTANCE = Mappers.getMapper(LogActionConvert.class);

}
