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

import cn.sliew.scaleph.dag.service.DagInstanceService;
import cn.sliew.scaleph.dag.service.convert.DagInstanceConvert;
import cn.sliew.scaleph.dag.service.dto.DagInstanceDTO;
import cn.sliew.scaleph.dao.entity.master.dag.DagInstance;
import cn.sliew.scaleph.dao.mapper.master.dag.DagInstanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DagInstanceServiceImpl implements DagInstanceService {

    @Autowired
    private DagInstanceMapper dagInstanceMapper;

    @Override
    public DagInstanceDTO selectOne(Long dagInstanceId) {
        DagInstance record = dagInstanceMapper.selectById(dagInstanceId);
        return DagInstanceConvert.INSTANCE.toDto(record);
    }

    @Override
    public Long insert(DagInstanceDTO instanceDTO) {
        DagInstance record = DagInstanceConvert.INSTANCE.toDo(instanceDTO);
        dagInstanceMapper.insert(record);
        return record.getId();
    }

    @Override
    public int update(DagInstanceDTO instanceDTO) {
        DagInstance record = DagInstanceConvert.INSTANCE.toDo(instanceDTO);
        return dagInstanceMapper.updateById(record);
    }
}
