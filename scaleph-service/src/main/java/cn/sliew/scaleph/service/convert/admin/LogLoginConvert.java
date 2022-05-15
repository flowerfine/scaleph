package cn.sliew.scaleph.service.convert.admin;

import cn.sliew.scaleph.dao.entity.log.LogLogin;
import cn.sliew.scaleph.service.convert.BaseConvert;
import cn.sliew.scaleph.service.convert.DictVoConvert;
import cn.sliew.scaleph.service.dto.admin.LogLoginDTO;
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
    @Mapping(expression = "java(cn.sliew.scaleph.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.LOGIN_TYPE,entity.getLoginType()))", target = "loginType")
    LogLoginDTO toDto(LogLogin entity);
}
