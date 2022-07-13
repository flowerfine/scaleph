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

import cn.sliew.scaleph.dao.entity.master.flink.FlinkJobConfig;
import cn.sliew.scaleph.dao.mapper.master.flink.FlinkJobConfigMapper;
import cn.sliew.scaleph.engine.flink.service.FlinkJobConfigService;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkJobConfigConvert;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobConfigDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobConfigListParam;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class FlinkJobConfigServiceImpl implements FlinkJobConfigService {

    @Autowired
    private FlinkJobConfigMapper flinkJobConfigMapper;

    @Override
    public Page<FlinkJobConfigDTO> list(FlinkJobConfigListParam param) {
        final Page<FlinkJobConfig> page = flinkJobConfigMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(FlinkJobConfig.class)
                        .eq(StringUtils.hasText(param.getType()), FlinkJobConfig::getType, param.getType())
                        .like(StringUtils.hasText(param.getName()), FlinkJobConfig::getName, param.getName())
                        .eq(param.getFlinkClusterConfigId() != null, FlinkJobConfig::getFlinkClusterConfigId, param.getFlinkClusterConfigId()));
        Page<FlinkJobConfigDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<FlinkJobConfigDTO> dtoList = FlinkJobConfigConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public FlinkJobConfigDTO selectOne(Long id) {
        final FlinkJobConfig record = flinkJobConfigMapper.selectById(id);
        if (record == null) {
            throw new IllegalStateException("flink job config not exists for id: " + id);
        }
        return FlinkJobConfigConvert.INSTANCE.toDto(record);
    }

    @Override
    public int insert(FlinkJobConfigDTO dto) {
        final FlinkJobConfig record = FlinkJobConfigConvert.INSTANCE.toDo(dto);
        return flinkJobConfigMapper.insert(record);
    }

    @Override
    public int update(FlinkJobConfigDTO dto) {
        final FlinkJobConfig record = FlinkJobConfigConvert.INSTANCE.toDo(dto);
        return flinkJobConfigMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return flinkJobConfigMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        flinkJobConfigMapper.deleteBatchIds(ids);
        return ids.size();
    }
}
