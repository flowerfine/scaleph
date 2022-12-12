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
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkJob;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkJobForJar;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkJobForSeaTunnel;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkJobMapper;
import cn.sliew.scaleph.engine.flink.service.FlinkJobService;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkJobConvert;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkJobForJarConvert;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkJobForSeaTunnelConvert;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobDTO;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobForJarDTO;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobForSeaTunnelDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobListByTypeParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobListParam;
import cn.sliew.scaleph.system.snowflake.UidGenerator;
import cn.sliew.scaleph.system.snowflake.exception.UidGenerateException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class FlinkJobServiceImpl implements FlinkJobService {

    @Autowired
    private UidGenerator defaultUidGenerator;
    @Autowired
    private WsFlinkJobMapper flinkJobMapper;

    @Override
    public Page<FlinkJobDTO> list(FlinkJobListParam param) {
        final Page<WsFlinkJob> page = new Page<>(param.getCurrent(), param.getPageSize());
        final WsFlinkJob wsFlinkJob = BeanUtil.copy(param, new WsFlinkJob());
        final Page<WsFlinkJob> flinkJobPage = flinkJobMapper.list(page, wsFlinkJob);
        Page<FlinkJobDTO> result =
                new Page<>(flinkJobPage.getCurrent(), flinkJobPage.getSize(), flinkJobPage.getTotal());
        List<FlinkJobDTO> dtoList = FlinkJobConvert.INSTANCE.toDto(flinkJobPage.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public FlinkJobDTO selectOne(Long id) {
        final WsFlinkJob record = flinkJobMapper.selectById(id);
        return FlinkJobConvert.INSTANCE.toDto(record);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public int insert(FlinkJobDTO dto) {
        try {
            final WsFlinkJob record = FlinkJobConvert.INSTANCE.toDo(dto);
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
        final WsFlinkJob record = FlinkJobConvert.INSTANCE.toDo(dto);
        return flinkJobMapper.updateById(record);
    }

    @Override
    public Page<FlinkJobForJarDTO> listJobsForJar(FlinkJobListByTypeParam param) {
        final Page<WsFlinkJob> page = new Page<>(param.getCurrent(), param.getPageSize());
        WsFlinkJob wsFlinkJob = BeanUtil.copy(param, new WsFlinkJob());

        final Page<WsFlinkJobForJar> flinkJobForJarPage = flinkJobMapper.listJobsForJar(page, wsFlinkJob);
        Page<FlinkJobForJarDTO> result =
                new Page<>(flinkJobForJarPage.getCurrent(), flinkJobForJarPage.getSize(), flinkJobForJarPage.getTotal());
        List<FlinkJobForJarDTO> dtoList = FlinkJobForJarConvert.INSTANCE.toDto(flinkJobForJarPage.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public FlinkJobForJarDTO getJobForJarById(Long id) {
        final WsFlinkJobForJar record = flinkJobMapper.getJobForJarById(id);
        if (record == null) {
            throw new IllegalStateException("flink job for jar not exists for id: " + id);
        }
        return FlinkJobForJarConvert.INSTANCE.toDto(record);
    }

    @Override
    public Page<FlinkJobForSeaTunnelDTO> listJobsForSeaTunnel(FlinkJobListByTypeParam param) {
        final Page<WsFlinkJob> page = new Page<>(param.getCurrent(), param.getPageSize());
        WsFlinkJob wsFlinkJob = BeanUtil.copy(param, new WsFlinkJob());

        final Page<WsFlinkJobForSeaTunnel> flinkJobForSeaTunnelPage = flinkJobMapper.listJobsForSeaTunnel(page, wsFlinkJob);
        Page<FlinkJobForSeaTunnelDTO> result =
                new Page<>(flinkJobForSeaTunnelPage.getCurrent(), flinkJobForSeaTunnelPage.getSize(), flinkJobForSeaTunnelPage.getTotal());
        List<FlinkJobForSeaTunnelDTO> dtoList = FlinkJobForSeaTunnelConvert.INSTANCE.toDto(flinkJobForSeaTunnelPage.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public FlinkJobForSeaTunnelDTO getJobForSeaTunnelById(Long id) {
        final WsFlinkJobForSeaTunnel record = flinkJobMapper.getJobForSeaTunnelById(id);
        if (record == null) {
            throw new IllegalStateException("flink job for seatunnel not exists for id: " + id);
        }
        return FlinkJobForSeaTunnelConvert.INSTANCE.toDto(record);
    }
}
