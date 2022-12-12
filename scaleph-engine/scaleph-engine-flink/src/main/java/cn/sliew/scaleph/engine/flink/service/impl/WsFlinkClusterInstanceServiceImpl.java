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

import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkClusterInstance;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkClusterInstanceMapper;
import cn.sliew.scaleph.engine.flink.service.FlinkClusterInstanceService;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkClusterInstanceConvert;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkClusterInstanceDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkClusterInstanceParam;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class FlinkClusterInstanceServiceImpl implements FlinkClusterInstanceService {

    @Autowired
    private WsFlinkClusterInstanceMapper flinkClusterInstanceMapper;

    @Override
    public Page<FlinkClusterInstanceDTO> list(FlinkClusterInstanceParam param) {
        final Page<WsFlinkClusterInstance> page = flinkClusterInstanceMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(WsFlinkClusterInstance.class)
                        .like(StringUtils.hasText(param.getName()), WsFlinkClusterInstance::getName, param.getName())
                        .eq(param.getFlinkClusterConfigId() != null, WsFlinkClusterInstance::getFlinkClusterConfigId, param.getFlinkClusterConfigId())
                        .eq(param.getStatus() != null, WsFlinkClusterInstance::getStatus, param.getStatus())
                        .eq(param.getProjectId() != null, WsFlinkClusterInstance::getProjectId, param.getProjectId())
        );
        Page<FlinkClusterInstanceDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<FlinkClusterInstanceDTO> dtoList = FlinkClusterInstanceConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public int insert(FlinkClusterInstanceDTO dto) {
        final WsFlinkClusterInstance record = FlinkClusterInstanceConvert.INSTANCE.toDo(dto);
        return flinkClusterInstanceMapper.insert(record);
    }

    @Override
    public int update(FlinkClusterInstanceDTO dto) {
        final WsFlinkClusterInstance record = FlinkClusterInstanceConvert.INSTANCE.toDo(dto);
        return flinkClusterInstanceMapper.updateById(record);
    }

    @Override
    public FlinkClusterInstanceDTO selectOne(Long id) {
        final WsFlinkClusterInstance record = flinkClusterInstanceMapper.selectById(id);
        return FlinkClusterInstanceConvert.INSTANCE.toDto(record);
    }

    @Override
    public Long totalCnt() {
        return flinkClusterInstanceMapper.selectCount(null);
    }
}
