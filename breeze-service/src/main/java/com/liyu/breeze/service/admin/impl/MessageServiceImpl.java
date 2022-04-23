package com.liyu.breeze.service.admin.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyu.breeze.common.enums.BoolEnum;
import com.liyu.breeze.dao.entity.Message;
import com.liyu.breeze.dao.mapper.MessageMapper;
import com.liyu.breeze.service.admin.MessageService;
import com.liyu.breeze.service.convert.admin.MessageConvert;
import com.liyu.breeze.service.dto.admin.MessageDTO;
import com.liyu.breeze.service.param.admin.MessageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 站内信表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public int insert(MessageDTO messageDTO) {
        Message message = MessageConvert.INSTANCE.toDo(messageDTO);
        return this.messageMapper.insert(message);
    }

    @Override
    public int update(MessageDTO messageDTO) {
        Message message = MessageConvert.INSTANCE.toDo(messageDTO);
        return this.messageMapper.updateById(message);
    }

    @Override
    public int countUnReadMsg(String receiver) {
        return this.messageMapper.selectCount(new LambdaQueryWrapper<Message>()
                .eq(Message::getReceiver, receiver)
                .eq(Message::getIsRead, BoolEnum.NO.getValue())
        );
    }

    @Override
    public Page<MessageDTO> listByPage(MessageParam messageParam) {
        Page<MessageDTO> result = new Page<>();
        Page<Message> list = this.messageMapper.selectPage(
                new Page<>(messageParam.getCurrent(), messageParam.getPageSize()),
                new LambdaQueryWrapper<Message>()
                        .eq(StrUtil.isNotEmpty(messageParam.getReceiver()), Message::getReceiver, messageParam.getReceiver())
                        .eq(StrUtil.isNotEmpty(messageParam.getSender()), Message::getSender, messageParam.getSender())
                        .eq(StrUtil.isNotEmpty(messageParam.getMessageType()), Message::getMessageType, messageParam.getMessageType())
                        .eq(StrUtil.isNotEmpty(messageParam.getIsRead()), Message::getIsRead, messageParam.getIsRead())
        );
        List<MessageDTO> dtoList = MessageConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public int readAll(String receiver) {
        Message message = new Message();
        message.setIsRead(BoolEnum.YES.getValue());
        return this.messageMapper.update(message, new LambdaQueryWrapper<Message>()
                .eq(Message::getReceiver, receiver)
                .eq(Message::getIsRead, BoolEnum.NO.getValue())
        );
    }


}
