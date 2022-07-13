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

import cn.sliew.scaleph.dao.entity.master.flink.FlinkJobInstance;
import cn.sliew.scaleph.dao.mapper.master.flink.FlinkJobInstanceMapper;
import cn.sliew.scaleph.engine.flink.service.FlinkJobInstanceService;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkJobInstanceConvert;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobInstanceDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobInstanceListParam;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class FlinkJobInstanceServiceImpl implements FlinkJobInstanceService {

    @Autowired
    private FlinkJobInstanceMapper flinkJobInstanceMapper;

    @Override
    public Page<FlinkJobInstanceDTO> list(FlinkJobInstanceListParam param) {
        final Page<FlinkJobInstance> page = flinkJobInstanceMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(FlinkJobInstance.class)
                        .eq(param.getFlinkJobConfigId() != null, FlinkJobInstance::getFlinkJobConfigId, param.getFlinkJobConfigId())
                        .eq(param.getFlinkClusterInstanceId() != null, FlinkJobInstance::getFlinkClusterInstanceId, param.getFlinkClusterInstanceId())
                        .eq(StringUtils.hasText(param.getStatus()), FlinkJobInstance::getStatus, param.getStatus()));
        Page<FlinkJobInstanceDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<FlinkJobInstanceDTO> dtoList = FlinkJobInstanceConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public FlinkJobInstanceDTO selectOne(Long id) {
        final FlinkJobInstance record = flinkJobInstanceMapper.selectById(id);
        if (record == null) {
            throw new IllegalStateException("flink job instance not exists for id: " + id);
        }
        return FlinkJobInstanceConvert.INSTANCE.toDto(record);
    }
}
