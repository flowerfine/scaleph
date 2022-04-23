package com.liyu.breeze.service.convert.admin;

import com.liyu.breeze.dao.entity.LogLogin;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.convert.DictVoConvert;
import com.liyu.breeze.service.dto.admin.LogLoginDTO;
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
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.LOGIN_TYPE,entity.getLoginType()))", target = "loginType")
    LogLoginDTO toDto(LogLogin entity);
}
