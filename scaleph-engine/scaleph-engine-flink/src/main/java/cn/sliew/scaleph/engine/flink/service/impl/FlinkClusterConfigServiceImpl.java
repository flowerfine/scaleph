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

import cn.sliew.scaleph.dao.entity.master.flink.FlinkClusterConfig;
import cn.sliew.scaleph.dao.mapper.master.flink.FlinkClusterConfigMapper;
import cn.sliew.scaleph.engine.flink.service.FlinkClusterConfigService;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkClusterConfigConvert;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkClusterConfigDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkClusterConfigListParam;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class FlinkClusterConfigServiceImpl implements FlinkClusterConfigService {

    @Autowired
    private FlinkClusterConfigMapper flinkClusterConfigMapper;

    @Override
    public int insert(FlinkClusterConfigDTO dto) {
        final FlinkClusterConfig record = FlinkClusterConfigConvert.INSTANCE.toDo(dto);
        return flinkClusterConfigMapper.insert(record);
    }

    @Override
    public int update(FlinkClusterConfigDTO dto) {
        final FlinkClusterConfig record = FlinkClusterConfigConvert.INSTANCE.toDo(dto);
        return flinkClusterConfigMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return flinkClusterConfigMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        return flinkClusterConfigMapper.deleteBatchIds(map.values());
    }

    @Override
    public Page<FlinkClusterConfigDTO> listByPage(FlinkClusterConfigListParam param) {
        final Page<FlinkClusterConfig> page = flinkClusterConfigMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(FlinkClusterConfig.class)
                        .like(StringUtils.hasText(param.getName()), FlinkClusterConfig::getName, param.getName())
                        .eq(StringUtils.hasText(param.getFlinkVersion()), FlinkClusterConfig::getFlinkVersion, param.getFlinkVersion())
                        .eq(StringUtils.hasText(param.getResourceProvider()), FlinkClusterConfig::getResourceProvider, param.getResourceProvider())
                        .eq(StringUtils.hasText(param.getDeployMode()), FlinkClusterConfig::getDeployMode, param.getDeployMode()));
        Page<FlinkClusterConfigDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<FlinkClusterConfigDTO> dtoList = FlinkClusterConfigConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public FlinkClusterConfigDTO selectOne(Long id) {
        FlinkClusterConfig record = flinkClusterConfigMapper.selectById(id);
        checkState(record != null, () -> "flink cluster config not exists for id: " + id);
        return FlinkClusterConfigConvert.INSTANCE.toDto(record);
    }
}
