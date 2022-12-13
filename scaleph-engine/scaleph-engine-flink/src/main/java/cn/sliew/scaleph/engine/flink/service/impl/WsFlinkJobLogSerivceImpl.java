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

package cn.sliew.scaleph.engine.flink.service.impl;

import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkJobInstance;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkJobLog;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkJobLogMapper;
import cn.sliew.scaleph.engine.flink.service.WsFlinkJobLogService;
import cn.sliew.scaleph.engine.flink.service.convert.WsFlinkJobInstanceConvert;
import cn.sliew.scaleph.engine.flink.service.convert.WsFlinkJobLogConvert;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkJobInstanceDTO;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkJobLogDTO;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkJobLogListParam;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WsFlinkJobLogSerivceImpl implements WsFlinkJobLogService {

    @Autowired
    private WsFlinkJobLogMapper flinkJobLogMapper;

    @Override
    public Page<WsFlinkJobLogDTO> list(WsFlinkJobLogListParam param) {
        final Page<WsFlinkJobLog> page = flinkJobLogMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(WsFlinkJobLog.class)
                        .eq(WsFlinkJobLog::getFlinkJobCode, param.getFlinkJobCode()));
        Page<WsFlinkJobLogDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<WsFlinkJobLogDTO> dtoList = WsFlinkJobLogConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public int insert(WsFlinkJobInstanceDTO dto) {
        final WsFlinkJobInstance flinkJobInstance = WsFlinkJobInstanceConvert.INSTANCE.toDo(dto);
        final WsFlinkJobLog record = BeanUtil.copy(flinkJobInstance, new WsFlinkJobLog());
        return flinkJobLogMapper.insert(record);
    }
}
