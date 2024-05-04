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

import cn.sliew.scaleph.dag.service.DagStepService;
import cn.sliew.scaleph.dag.service.convert.DagStepConvert;
import cn.sliew.scaleph.dag.service.convert.DagStepVOConvert;
import cn.sliew.scaleph.dag.service.dto.DagStepDTO;
import cn.sliew.scaleph.dao.entity.master.dag.DagStep;
import cn.sliew.scaleph.dao.entity.master.dag.DagStepVO;
import cn.sliew.scaleph.dao.mapper.master.dag.DagStepMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class DagStepServiceImpl implements DagStepService {

    @Autowired
    private DagStepMapper dagStepMapper;

    @Override
    public List<DagStepDTO> listSteps(Long dagInstanceId) {
        List<DagStepVO> dagStepVOS = dagStepMapper.listByDagInstanceId(dagInstanceId);
        return DagStepVOConvert.INSTANCE.toDto(dagStepVOS);
    }

    @Override
    public DagStepDTO selectOne(Long id) {
        DagStep record = dagStepMapper.selectById(id);
        checkState(record != null, () -> "dag step not exists for id: " + id);
        return DagStepConvert.INSTANCE.toDto(record);
    }

    @Override
    public int insert(DagStepDTO stepDTO) {
        DagStep record = DagStepConvert.INSTANCE.toDo(stepDTO);
        return dagStepMapper.insert(record);
    }

    @Override
    public int update(DagStepDTO stepDTO) {
        DagStep record = DagStepConvert.INSTANCE.toDo(stepDTO);
        return dagStepMapper.updateById(record);
    }
}
