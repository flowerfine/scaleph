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

import cn.sliew.scaleph.dao.entity.log.LogAction;
import cn.sliew.scaleph.dao.mapper.log.LogActionMapper;
import cn.sliew.scaleph.log.service.LogActionService;
import cn.sliew.scaleph.log.service.convert.LogActionConvert;
import cn.sliew.scaleph.log.service.dto.LogActionDTO;
import cn.sliew.scaleph.log.service.param.LogActionParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户操作日志 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class LogActionServiceImpl implements LogActionService {

    @Autowired
    private LogActionMapper logActionMapper;

    @Override
    public int insert(LogActionDTO logActionDTO) {
        LogAction logAction = LogActionConvert.INSTANCE.toDo(logActionDTO);
        return this.logActionMapper.insert(logAction);
    }

    @Override
    public Page<LogActionDTO> listByPage(LogActionParam logActionParam) {
        Page<LogActionDTO> result = new Page<>();
        Page<LogAction> list = this.logActionMapper.selectPage(
            new Page<>(logActionParam.getCurrent(), logActionParam.getPageSize()),
            new QueryWrapper<LogAction>()
                .lambda()
                .eq(LogAction::getUserName, logActionParam.getUserName())
                .gt(LogAction::getActionTime, logActionParam.getActionTime())
        );
        List<LogActionDTO> dtoList = LogActionConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }
}
