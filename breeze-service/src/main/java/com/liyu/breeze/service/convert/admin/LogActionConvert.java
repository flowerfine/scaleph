package com.liyu.breeze.service.convert.admin;

import com.liyu.breeze.dao.entity.LogAction;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.dto.admin.LogActionDTO;
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
