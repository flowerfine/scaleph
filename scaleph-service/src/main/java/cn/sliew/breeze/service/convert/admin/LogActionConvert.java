package cn.sliew.breeze.service.convert.admin;

import cn.sliew.breeze.dao.entity.log.LogAction;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.dto.admin.LogActionDTO;
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
