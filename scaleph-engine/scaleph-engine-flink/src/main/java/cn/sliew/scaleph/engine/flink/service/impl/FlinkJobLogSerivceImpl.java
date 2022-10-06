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
import cn.sliew.scaleph.dao.entity.master.flink.FlinkJobInstance;
import cn.sliew.scaleph.dao.entity.master.flink.FlinkJobLog;
import cn.sliew.scaleph.dao.mapper.master.flink.FlinkJobLogMapper;
import cn.sliew.scaleph.engine.flink.service.FlinkJobLogService;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkJobInstanceConvert;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkJobLogConvert;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobInstanceDTO;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobLogDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobLogListParam;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FlinkJobLogSerivceImpl implements FlinkJobLogService {

    @Autowired
    private FlinkJobLogMapper flinkJobLogMapper;

    @Override
    public Page<FlinkJobLogDTO> list(FlinkJobLogListParam param) {
        final Page<FlinkJobLog> page = flinkJobLogMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(FlinkJobLog.class)
                        .eq(FlinkJobLog::getFlinkJobCode, param.getFlinkJobCode()));
        Page<FlinkJobLogDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<FlinkJobLogDTO> dtoList = FlinkJobLogConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public int insert(FlinkJobInstanceDTO dto) {
        final FlinkJobInstance flinkJobInstance = FlinkJobInstanceConvert.INSTANCE.toDo(dto);
        final FlinkJobLog record = BeanUtil.copy(flinkJobInstance, new FlinkJobLog());
        return flinkJobLogMapper.insert(record);
    }
}
