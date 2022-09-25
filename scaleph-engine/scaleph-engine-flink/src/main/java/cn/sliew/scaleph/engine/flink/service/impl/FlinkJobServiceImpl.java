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

import cn.sliew.scaleph.common.exception.Rethrower;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.flink.FlinkArtifact;
import cn.sliew.scaleph.dao.entity.master.flink.FlinkJob;
import cn.sliew.scaleph.dao.mapper.master.flink.FlinkJobMapper;
import cn.sliew.scaleph.engine.flink.service.FlinkJobService;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkArtifactConvert;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkJobConvert;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobListParam;
import cn.sliew.scaleph.system.snowflake.UidGenerator;
import cn.sliew.scaleph.system.snowflake.exception.UidGenerateException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Slf4j
@Service
public class FlinkJobServiceImpl implements FlinkJobService {

    @Autowired
    private UidGenerator defaultUidGenerator;
    @Autowired
    private FlinkJobMapper flinkJobMapper;

    @Override
    public Page<FlinkJobDTO> list(FlinkJobListParam param) {
        final Page<FlinkJob> page = new Page<>(param.getCurrent(), param.getPageSize());
        final FlinkJob flinkJob = BeanUtil.copy(param, new FlinkJob());
        final Page<FlinkJob> flinkJobPage = flinkJobMapper.list(page, flinkJob);
        Page<FlinkJobDTO> result =
                new Page<>(flinkJobPage.getCurrent(), flinkJobPage.getSize(), flinkJobPage.getTotal());
        List<FlinkJobDTO> dtoList = FlinkJobConvert.INSTANCE.toDto(flinkJobPage.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public FlinkJobDTO selectOne(Long id) {
        final FlinkJob record = flinkJobMapper.selectById(id);
        checkState(record != null, () -> "flink job not exists for id: " + id);
        return FlinkJobConvert.INSTANCE.toDto(record);
    }

    @Override
    public int insert(FlinkJobDTO dto) {
        try {
            final FlinkJob record = FlinkJobConvert.INSTANCE.toDo(dto);
            record.setCode(defaultUidGenerator.getUID());
            return flinkJobMapper.insert(record);
        } catch (UidGenerateException e) {
            Rethrower.throwAs(e);
            return -1;
        }
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public int update(FlinkJobDTO dto) {
        final FlinkJob record = FlinkJobConvert.INSTANCE.toDo(dto);
        final FlinkJob flinkJob = flinkJobMapper.selectLatestJob(record.getCode());
        record.setVersion(flinkJob.getVersion() + 1);
        return flinkJobMapper.insert(record);
    }

    @Override
    public int deleteById(Long id) {
        return flinkJobMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {

        return flinkJobMapper.deleteBatchIds(ids);
    }
}
