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
import cn.sliew.scaleph.dao.entity.master.flink.FlinkJobConfigJar;
import cn.sliew.scaleph.dao.entity.master.flink.FlinkJobConfigJarVO;
import cn.sliew.scaleph.dao.mapper.master.flink.FlinkJobConfigJarMapper;
import cn.sliew.scaleph.engine.flink.service.FlinkJobConfigJarService;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkJobConfigConvert;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkJobConfigJarVOConvert;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobConfigJarDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobConfigJarListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class FlinkJobConfigJarServiceImpl implements FlinkJobConfigJarService {

    @Autowired
    private FlinkJobConfigJarMapper flinkJobConfigJarMapper;

    @Override
    public Page<FlinkJobConfigJarDTO> list(FlinkJobConfigJarListParam param) {
        final Page<FlinkJobConfigJar> page = new Page<>(param.getCurrent(), param.getPageSize());
        FlinkJobConfigJar flinkJobConfigJar = BeanUtil.copy(page, new FlinkJobConfigJar());

        final Page<FlinkJobConfigJarVO> flinkJobConfigJarVOPage = flinkJobConfigJarMapper.list(page, flinkJobConfigJar);
        Page<FlinkJobConfigJarDTO> result =
                new Page<>(flinkJobConfigJarVOPage.getCurrent(), flinkJobConfigJarVOPage.getSize(), flinkJobConfigJarVOPage.getTotal());
        List<FlinkJobConfigJarDTO> dtoList = FlinkJobConfigJarVOConvert.INSTANCE.toDto(flinkJobConfigJarVOPage.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public FlinkJobConfigJarDTO selectOne(Long id) {
        final FlinkJobConfigJarVO record = flinkJobConfigJarMapper.getById(id);
        if (record == null) {
            throw new IllegalStateException("flink job config not exists for id: " + id);
        }
        return FlinkJobConfigJarVOConvert.INSTANCE.toDto(record);
    }

    @Override
    public int insert(FlinkJobConfigJarDTO dto) {
        final FlinkJobConfigJar record = FlinkJobConfigConvert.INSTANCE.toDo(dto);
        return flinkJobConfigJarMapper.insert(record);
    }

    @Override
    public int update(FlinkJobConfigJarDTO dto) {
        final FlinkJobConfigJar record = FlinkJobConfigConvert.INSTANCE.toDo(dto);
        return flinkJobConfigJarMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return flinkJobConfigJarMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        flinkJobConfigJarMapper.deleteBatchIds(ids);
        return ids.size();
    }
}
