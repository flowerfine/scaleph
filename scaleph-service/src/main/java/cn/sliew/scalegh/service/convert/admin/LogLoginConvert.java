package cn.sliew.scalegh.service.convert.admin;

import cn.sliew.breeze.dao.entity.log.LogLogin;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.convert.DictVoConvert;
import cn.sliew.scalegh.service.dto.admin.LogLoginDTO;
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
    @Mapping(expression = "java(DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.LOGIN_TYPE,entity.getLoginType()))", target = "loginType")
    LogLoginDTO toDto(LogLogin entity);
}
