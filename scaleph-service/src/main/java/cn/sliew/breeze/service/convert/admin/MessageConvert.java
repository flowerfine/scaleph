package cn.sliew.breeze.service.convert.admin;

import cn.sliew.breeze.dao.entity.log.Message;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.convert.DictVoConvert;
import cn.sliew.breeze.service.dto.admin.MessageDTO;
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
    @Mapping(expression = "java(cn.sliew.breeze.service.vo.DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.YES_NO,entity.getIsRead()))", target = "isRead")
    @Mapping(expression = "java(cn.sliew.breeze.service.vo.DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.IS_DELETE,entity.getIsDelete()))", target = "isDelete")
    @Mapping(expression = "java(cn.sliew.breeze.service.vo.DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.MESSAGE_TYPE,entity.getMessageType()))", target = "messageType")
    MessageDTO toDto(Message entity);

}
