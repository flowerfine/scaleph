package cn.sliew.scaleph.service.convert.admin;

import cn.sliew.scaleph.dao.entity.log.LogAction;
import cn.sliew.scaleph.service.convert.BaseConvert;
import cn.sliew.scaleph.service.dto.admin.LogActionDTO;
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
