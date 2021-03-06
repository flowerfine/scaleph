/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.log.service.impl;

import java.util.List;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.common.enums.BoolEnum;
import cn.sliew.scaleph.dao.entity.log.LogMessage;
import cn.sliew.scaleph.dao.mapper.log.LogMessageMapper;
import cn.sliew.scaleph.log.service.LogMessageService;
import cn.sliew.scaleph.log.service.convert.LogMessageConvert;
import cn.sliew.scaleph.log.service.dto.LogMessageDTO;
import cn.sliew.scaleph.log.service.param.LogMessageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 站内信表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class LogMessageServiceImpl implements LogMessageService {

    @Autowired
    private LogMessageMapper logMessageMapper;

    @Override
    public int insert(LogMessageDTO logMessageDTO) {
        LogMessage logMessage = LogMessageConvert.INSTANCE.toDo(logMessageDTO);
        return this.logMessageMapper.insert(logMessage);
    }

    @Override
    public int update(LogMessageDTO logMessageDTO) {
        LogMessage logMessage = LogMessageConvert.INSTANCE.toDo(logMessageDTO);
        return this.logMessageMapper.updateById(logMessage);
    }

    @Override
    public Long countUnReadMsg(String receiver) {
        return this.logMessageMapper.selectCount(new LambdaQueryWrapper<LogMessage>()
            .eq(LogMessage::getReceiver, receiver)
            .eq(LogMessage::getIsRead, BoolEnum.NO.getValue())
        );
    }

    @Override
    public Page<LogMessageDTO> listByPage(LogMessageParam logMessageParam) {
        Page<LogMessageDTO> result = new Page<>();
        Page<LogMessage> list = this.logMessageMapper.selectPage(
            new Page<>(logMessageParam.getCurrent(), logMessageParam.getPageSize()),
            new LambdaQueryWrapper<LogMessage>()
                .eq(StrUtil.isNotEmpty(logMessageParam.getReceiver()), LogMessage::getReceiver,
                    logMessageParam.getReceiver())
                .eq(StrUtil.isNotEmpty(logMessageParam.getSender()), LogMessage::getSender,
                    logMessageParam.getSender())
                .eq(StrUtil.isNotEmpty(logMessageParam.getMessageType()), LogMessage::getMessageType,
                    logMessageParam.getMessageType())
                .eq(StrUtil.isNotEmpty(logMessageParam.getIsRead()), LogMessage::getIsRead,
                    logMessageParam.getIsRead())
        );
        List<LogMessageDTO> dtoList = LogMessageConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public int readAll(String receiver) {
        LogMessage logMessage = new LogMessage();
        logMessage.setIsRead(BoolEnum.YES.getValue());
        return this.logMessageMapper.update(logMessage, new LambdaQueryWrapper<LogMessage>()
            .eq(LogMessage::getReceiver, receiver)
            .eq(LogMessage::getIsRead, BoolEnum.NO.getValue())
        );
    }


}
