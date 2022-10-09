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

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.entity.master.flink.FlinkClusterConfig;
import cn.sliew.scaleph.dao.entity.master.flink.FlinkClusterConfigVO;
import cn.sliew.scaleph.dao.mapper.master.flink.FlinkClusterConfigMapper;
import cn.sliew.scaleph.engine.flink.service.FlinkClusterConfigService;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkClusterConfigConvert;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkClusterConfigVOConvert;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkClusterConfigDTO;
import cn.sliew.scaleph.engine.flink.service.dto.KubernetesOptions;
import cn.sliew.scaleph.engine.flink.service.param.FlinkClusterConfigAddParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkClusterConfigListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class FlinkClusterConfigServiceImpl implements FlinkClusterConfigService {

    @Autowired
    private FlinkClusterConfigMapper flinkClusterConfigMapper;

    @Override
    public FlinkClusterConfigDTO insert(FlinkClusterConfigAddParam param) {
        final FlinkClusterConfig record = FlinkClusterConfigConvert.INSTANCE.toDO(param);
        flinkClusterConfigMapper.insert(record);
        return selectOne(record.getId());
    }

    @Override
    public int updateKubernetesOptions(Long id, KubernetesOptions options) {
        FlinkClusterConfig record = new FlinkClusterConfig();
        record.setId(id);
        record.setKubernetesOptions(JacksonUtil.toJsonString(options));
        return flinkClusterConfigMapper.updateById(record);
    }

    @Override
    public int updateFlinkConfig(Long id, Map<String, String> options) {
        if (CollectionUtils.isEmpty(options)) {
            return 0;
        }
        FlinkClusterConfig record = new FlinkClusterConfig();
        record.setId(id);
        record.setConfigOptions(JacksonUtil.toJsonString(options));
        return flinkClusterConfigMapper.updateById(record);
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
    public int deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            deleteById(id);
        }
        return ids.size();
    }

    @Override
    public Page<FlinkClusterConfigDTO> listByPage(FlinkClusterConfigListParam param) {
        final Page<FlinkClusterConfig> page = new Page<>(param.getCurrent(), param.getPageSize());
        FlinkClusterConfig flinkClusterConfig = BeanUtil.copy(param, new FlinkClusterConfig());
        final Page<FlinkClusterConfigVO> flinkClusterConfigVOPage = flinkClusterConfigMapper.list(page, flinkClusterConfig);

        Page<FlinkClusterConfigDTO> result =
                new Page<>(flinkClusterConfigVOPage.getCurrent(), flinkClusterConfigVOPage.getSize(), flinkClusterConfigVOPage.getTotal());
        List<FlinkClusterConfigDTO> dtoList = FlinkClusterConfigVOConvert.INSTANCE.toDto(flinkClusterConfigVOPage.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public FlinkClusterConfigDTO selectOne(Long id) {
        FlinkClusterConfigVO record = flinkClusterConfigMapper.getById(id);
        checkState(record != null, () -> "flink cluster config not exists for id: " + id);
        return FlinkClusterConfigVOConvert.INSTANCE.toDto(record);
    }
}
