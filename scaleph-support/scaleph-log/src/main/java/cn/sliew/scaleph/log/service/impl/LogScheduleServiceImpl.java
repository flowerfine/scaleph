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

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.dao.entity.log.LogSchedule;
import cn.sliew.scaleph.dao.mapper.log.LogScheduleMapper;
import cn.sliew.scaleph.log.service.LogScheduleService;
import cn.sliew.scaleph.log.service.convert.LogScheduleConvert;
import cn.sliew.scaleph.log.service.dto.LogScheduleDTO;
import cn.sliew.scaleph.log.service.param.LogScheduleParam;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gleiyu
 */
@Service
public class LogScheduleServiceImpl implements LogScheduleService {

    @Autowired
    private LogScheduleMapper logScheduleMapper;

    @Override
    public int insert(LogScheduleDTO logSchedule) {
        LogSchedule log = LogScheduleConvert.INSTANCE.toDo(logSchedule);
        return this.logScheduleMapper.insert(log);
    }

    @Override
    public LogScheduleDTO selectOne(Long id) {
        LogSchedule log = this.logScheduleMapper.selectById(id);
        return LogScheduleConvert.INSTANCE.toDto(log);
    }

    @Override
    public Page<LogScheduleDTO> listByPage(LogScheduleParam param) {
        Page<LogScheduleDTO> result = new Page<>();
        Page<LogSchedule> list = this.logScheduleMapper.selectPage(
            new Page<>(param.getCurrent(), param.getPageSize()),
            Wrappers.lambdaQuery(LogSchedule.class)
                .eq(StrUtil.isNotEmpty(param.getTaskGroup()), LogSchedule::getTaskGroup,
                    param.getTaskGroup())
                .eq(StrUtil.isNotEmpty(param.getTaskName()), LogSchedule::getTaskName,
                    param.getTaskName())
                .eq(StrUtil.isNotEmpty(param.getResult()), LogSchedule::getResult,
                    param.getResult())
                .gt(ObjectUtil.isNotNull(param.getStartTime()), LogSchedule::getStartTime,
                    param.getStartTime())
                .lt(ObjectUtil.isNotNull(param.getEndTime()), LogSchedule::getEndTime,
                    param.getEndTime())
                .orderByDesc(LogSchedule::getStartTime)
        );
        List<LogScheduleDTO> dtoList = LogScheduleConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }
}
