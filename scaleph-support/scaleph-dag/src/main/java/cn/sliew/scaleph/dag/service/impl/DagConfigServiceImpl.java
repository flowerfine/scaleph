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

package cn.sliew.scaleph.dag.service.impl;

import cn.sliew.scaleph.common.util.UUIDUtil;
import cn.sliew.scaleph.dag.service.DagConfigService;
import cn.sliew.scaleph.dag.service.convert.DagConfigConvert;
import cn.sliew.scaleph.dag.service.dto.DagConfigDTO;
import cn.sliew.scaleph.dao.entity.master.dag.DagConfig;
import cn.sliew.scaleph.dao.mapper.master.dag.DagConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DagConfigServiceImpl implements DagConfigService {

    @Autowired
    private DagConfigMapper dagConfigMapper;

    @Override
    public DagConfigDTO selectOne(Long dagId) {
        DagConfig record = dagConfigMapper.selectById(dagId);
        return DagConfigConvert.INSTANCE.toDto(record);
    }

    @Override
    public Long insert(DagConfigDTO configDTO) {
        DagConfig record = DagConfigConvert.INSTANCE.toDo(configDTO);
        record.setConfigId(UUIDUtil.randomUUId());
        record.setVersion(0);
        dagConfigMapper.insert(record);
        return record.getId();
    }

    @Override
    public int update(DagConfigDTO configDTO) {
        DagConfig record = DagConfigConvert.INSTANCE.toDo(configDTO);
        return dagConfigMapper.updateById(record);
    }

    @Override
    public void upsert(DagConfigDTO configDTO) {
        if (configDTO.getId() != null) {
            update(configDTO);
        } else {
            insert(configDTO);
        }
    }

    @Override
    public int delete(Long dagId) {
        return dagConfigMapper.deleteById(dagId);
    }

    @Override
    public int deleteBatch(List<Long> dagIds) {
        return dagConfigMapper.deleteBatchIds(dagIds);
    }

    @Override
    public Long clone(Long dagId) {
        DagConfigDTO configDTO = selectOne(dagId);
        configDTO.setId(null);
        configDTO.setCreator(null);
        configDTO.setCreateTime(null);
        configDTO.setEditor(null);
        configDTO.setUpdateTime(null);
        configDTO.setName(configDTO.getName() + "_copy_" + UUIDUtil.randomUUId());
        return insert(configDTO);
    }
}
