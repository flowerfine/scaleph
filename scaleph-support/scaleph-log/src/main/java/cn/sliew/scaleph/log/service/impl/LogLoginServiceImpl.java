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

import cn.sliew.scaleph.dao.entity.log.LogLogin;
import cn.sliew.scaleph.dao.mapper.log.LogLoginMapper;
import cn.sliew.scaleph.log.service.LogLoginService;
import cn.sliew.scaleph.log.service.convert.LogLoginConvert;
import cn.sliew.scaleph.log.service.dto.LogLoginDTO;
import cn.sliew.scaleph.log.service.param.LogLoginParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户登录登出日志 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class LogLoginServiceImpl implements LogLoginService {

    @Autowired
    private LogLoginMapper logLoginMapper;

    @Override
    public int insert(LogLoginDTO logLoginDTO) {
        LogLogin log = LogLoginConvert.INSTANCE.toDo(logLoginDTO);
        return this.logLoginMapper.insert(log);
    }

    @Override
    public Page<LogLoginDTO> listByPage(LogLoginParam logLoginParam) {
        Page<LogLoginDTO> result = new Page<>();
        Page<LogLogin> list = this.logLoginMapper.selectPage(
            new Page<>(logLoginParam.getCurrent(), logLoginParam.getPageSize()),
            new QueryWrapper<LogLogin>()
                .lambda()
                .eq(LogLogin::getUserName, logLoginParam.getUserName())
                .gt(LogLogin::getLoginTime, logLoginParam.getLoginTime())
                .orderByDesc(LogLogin::getLoginTime)
        );
        List<LogLoginDTO> dtoList = LogLoginConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }
}
