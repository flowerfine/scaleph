package cn.sliew.scaleph.log.service.convert;

import cn.sliew.scaleph.dao.entity.log.LogLogin;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.log.service.dto.LogLoginDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogLoginConvert extends BaseConvert<LogLogin, LogLoginDTO> {
    LogLoginConvert INSTANCE = Mappers.getMapper(LogLoginConvert.class);

    @Override
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.LOGIN_TYPE,entity.getLoginType()))", target = "loginType")
    LogLoginDTO toDto(LogLogin entity);
}
