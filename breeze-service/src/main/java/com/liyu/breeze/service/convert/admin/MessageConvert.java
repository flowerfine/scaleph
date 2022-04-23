package com.liyu.breeze.service.convert.admin;

import com.liyu.breeze.dao.entity.Message;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.convert.DictVoConvert;
import com.liyu.breeze.service.dto.admin.MessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageConvert extends BaseConvert<Message, MessageDTO> {
    MessageConvert INSTANCE = Mappers.getMapper(MessageConvert.class);

    @Override
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.YES_NO,entity.getIsRead()))", target = "isRead")
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.IS_DELETE,entity.getIsDelete()))", target = "isDelete")
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.MESSAGE_TYPE,entity.getMessageType()))", target = "messageType")
    MessageDTO toDto(Message entity);

}
