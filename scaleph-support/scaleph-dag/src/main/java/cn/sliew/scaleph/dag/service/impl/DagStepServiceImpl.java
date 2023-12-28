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
import cn.sliew.scaleph.dag.service.dto.DagStepDTO;
import cn.sliew.scaleph.dao.entity.master.dag.DagStep;
import cn.sliew.scaleph.dao.mapper.master.dag.DagStepMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DagStepServiceImpl implements DagStepService {

    @Autowired
    private DagStepMapper dagStepMapper;

    @Override
    public List<DagStepDTO> listSteps(Long dagId) {
        LambdaQueryWrapper<DagStep> queryWrapper = Wrappers.lambdaQuery(DagStep.class)
                .eq(DagStep::getDagId, dagId);
        List<DagStep> dagSteps = dagStepMapper.selectList(queryWrapper);
        return DagStepConvert.INSTANCE.toDto(dagSteps);
    }

    @Override
    public int insert(DagStepDTO stepDTO) {
        DagStep record = DagStepConvert.INSTANCE.toDo(stepDTO);
        return dagStepMapper.insert(record);
    }

    @Override
    public int update(DagStepDTO stepDTO) {
        LambdaUpdateWrapper<DagStep> updateWrapper = Wrappers.lambdaUpdate(DagStep.class)
                .eq(DagStep::getDagId, stepDTO.getDagId())
                .eq(DagStep::getStepId, stepDTO.getStepId());
        DagStep record = DagStepConvert.INSTANCE.toDo(stepDTO);
        return dagStepMapper.update(record, updateWrapper);
    }

    @Override
    public int upsert(DagStepDTO stepDTO) {
        LambdaQueryWrapper<DagStep> queryWrapper = Wrappers.lambdaQuery(DagStep.class)
                .eq(DagStep::getDagId, stepDTO.getDagId())
                .eq(DagStep::getStepId, stepDTO.getStepId());
        if (dagStepMapper.exists(queryWrapper)) {
            return update(stepDTO);
        } else {
            return insert(stepDTO);
        }
    }

    @Override
    public int deleteByDag(Long dagId) {
        LambdaUpdateWrapper<DagStep> updateWrapper = Wrappers.lambdaUpdate(DagStep.class)
                .eq(DagStep::getDagId, dagId);
        return dagStepMapper.delete(updateWrapper);
    }

    @Override
    public int deleteByDag(List<Long> dagIds) {
        LambdaUpdateWrapper<DagStep> updateWrapper = Wrappers.lambdaUpdate(DagStep.class)
                .in(DagStep::getDagId, dagIds);
        return dagStepMapper.delete(updateWrapper);
    }

    @Override
    public int deleteSurplusSteps(Long dagId, List<String> stepIds) {
        LambdaUpdateWrapper<DagStep> updateWrapper = Wrappers.lambdaUpdate(DagStep.class)
                .eq(DagStep::getDagId, dagId)
                .notIn(DagStep::getStepId, stepIds);
        return dagStepMapper.delete(updateWrapper);
    }

    @Override
    public int clone(Long sourceDagId, Long targetDagId) {
        List<DagStepDTO> sourceSteps = listSteps(sourceDagId);
        sourceSteps.forEach(stepDTO -> {
            stepDTO.setDagId(targetDagId);
            stepDTO.setId(null);
            stepDTO.setCreator(null);
            stepDTO.setCreateTime(null);
            stepDTO.setEditor(null);
            stepDTO.setUpdateTime(null);
            dagStepMapper.insert(DagStepConvert.INSTANCE.toDo(stepDTO));
        });
        return sourceSteps.size();
    }
}
