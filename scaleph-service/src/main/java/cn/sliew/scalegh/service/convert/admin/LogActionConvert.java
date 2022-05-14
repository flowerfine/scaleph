package cn.sliew.scalegh.service.convert.admin;

import cn.sliew.scalegh.dao.entity.log.LogAction;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.dto.admin.LogActionDTO;
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
