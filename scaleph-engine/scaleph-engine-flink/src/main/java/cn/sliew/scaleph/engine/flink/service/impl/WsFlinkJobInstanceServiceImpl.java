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

import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkJobInstance;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkJobInstanceMapper;
import cn.sliew.scaleph.engine.flink.service.WsFlinkJobInstanceService;
import cn.sliew.scaleph.engine.flink.service.WsFlinkJobLogService;
import cn.sliew.scaleph.engine.flink.service.convert.WsFlinkJobInstanceConvert;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkJobInstanceDTO;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkJobInstanceListParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WsFlinkJobInstanceServiceImpl
        extends ServiceImpl<WsFlinkJobInstanceMapper, WsFlinkJobInstance>
        implements WsFlinkJobInstanceService {

    @Autowired
    private WsFlinkJobInstanceMapper flinkJobInstanceMapper;
    @Autowired
    private WsFlinkJobLogService wsFlinkJobLogService;

    @Override
    public Page<WsFlinkJobInstanceDTO> list(WsFlinkJobInstanceListParam param) {
        final Page<WsFlinkJobInstance> page = flinkJobInstanceMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(WsFlinkJobInstance.class)
                        .eq(WsFlinkJobInstance::getFlinkJobCode, param.getFlinkJobCode()));
        Page<WsFlinkJobInstanceDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<WsFlinkJobInstanceDTO> dtoList = WsFlinkJobInstanceConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public WsFlinkJobInstanceDTO selectByCode(Long flinkJobCode) {
        final LambdaQueryWrapper<WsFlinkJobInstance> wrapper = Wrappers.lambdaQuery(WsFlinkJobInstance.class)
                .eq(WsFlinkJobInstance::getFlinkJobCode, flinkJobCode);
        final WsFlinkJobInstance flinkJobInstance = flinkJobInstanceMapper.selectOne(wrapper);
        return Optional.ofNullable(flinkJobInstance)
                .map(record -> WsFlinkJobInstanceConvert.INSTANCE.toDto(record))
                .orElse(null);
    }

    @Override
    public WsFlinkJobInstanceDTO selectOne(Long id) {
        final WsFlinkJobInstance record = flinkJobInstanceMapper.selectById(id);
        if (record == null) {
            throw new IllegalStateException("flink job instance not exists for id: " + id);
        }
        return WsFlinkJobInstanceConvert.INSTANCE.toDto(record);
    }

    @Override
    public boolean upsert(WsFlinkJobInstanceDTO dto) {
        WsFlinkJobInstance record = WsFlinkJobInstanceConvert.INSTANCE.toDo(dto);
        LambdaUpdateWrapper<WsFlinkJobInstance> wrapper = new UpdateWrapper<WsFlinkJobInstance>()
                .lambda()
                .eq(WsFlinkJobInstance::getFlinkJobCode, dto.getFlinkJobCode())
                .eq(WsFlinkJobInstance::getJobId, dto.getJobId());
        return saveOrUpdate(record, wrapper);
    }

    @Override
    public int insert(WsFlinkJobInstanceDTO dto) {
        WsFlinkJobInstance record = WsFlinkJobInstanceConvert.INSTANCE.toDo(dto);
        return flinkJobInstanceMapper.insert(record);
    }

    @Override
    public int update(WsFlinkJobInstanceDTO dto) {
        WsFlinkJobInstance record = WsFlinkJobInstanceConvert.INSTANCE.toDo(dto);
        return flinkJobInstanceMapper.updateById(record);
    }

    @Override
    public WsFlinkJobInstanceDTO deleteById(Long id) {
        final WsFlinkJobInstanceDTO dto = selectOne(id);
        flinkJobInstanceMapper.deleteById(id);
        return dto;
    }

    @Override
    public int transferToLog(WsFlinkJobInstanceDTO dto) {
        update(dto);
        WsFlinkJobInstanceDTO wsFlinkJobInstanceDTO = deleteById(dto.getId());
        return wsFlinkJobLogService.insert(wsFlinkJobInstanceDTO);
    }
}
