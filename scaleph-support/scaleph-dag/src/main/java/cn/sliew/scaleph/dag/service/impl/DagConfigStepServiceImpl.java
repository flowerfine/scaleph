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

import cn.sliew.scaleph.dag.service.DagConfigStepService;
import cn.sliew.scaleph.dag.service.convert.DagConfigStepConvert;
import cn.sliew.scaleph.dag.service.dto.DagConfigStepDTO;
import cn.sliew.scaleph.dao.entity.master.dag.DagConfigStep;
import cn.sliew.scaleph.dao.mapper.master.dag.DagConfigStepMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class DagConfigStepServiceImpl implements DagConfigStepService {

    @Autowired
    private DagConfigStepMapper dagConfigStepMapper;

    @Override
    public List<DagConfigStepDTO> listSteps(Long dagId) {
        LambdaQueryWrapper<DagConfigStep> queryWrapper = Wrappers.lambdaQuery(DagConfigStep.class)
                .eq(DagConfigStep::getDagId, dagId);
        List<DagConfigStep> dagConfigSteps = dagConfigStepMapper.selectList(queryWrapper);
        return DagConfigStepConvert.INSTANCE.toDto(dagConfigSteps);
    }

    @Override
    public DagConfigStepDTO selectOne(Long stepId) {
        DagConfigStep record = dagConfigStepMapper.selectById(stepId);
        checkState(record != null, () -> "dag config step not exists for id: " + stepId);
        return DagConfigStepConvert.INSTANCE.toDto(record);
    }

    @Override
    public int insert(DagConfigStepDTO stepDTO) {
        DagConfigStep record = DagConfigStepConvert.INSTANCE.toDo(stepDTO);
        return dagConfigStepMapper.insert(record);
    }

    @Override
    public int update(DagConfigStepDTO stepDTO) {
        LambdaUpdateWrapper<DagConfigStep> updateWrapper = Wrappers.lambdaUpdate(DagConfigStep.class)
                .eq(DagConfigStep::getDagId, stepDTO.getDagId())
                .eq(DagConfigStep::getStepId, stepDTO.getStepId());
        DagConfigStep record = DagConfigStepConvert.INSTANCE.toDo(stepDTO);
        return dagConfigStepMapper.update(record, updateWrapper);
    }

    @Override
    public int upsert(DagConfigStepDTO stepDTO) {
        LambdaQueryWrapper<DagConfigStep> queryWrapper = Wrappers.lambdaQuery(DagConfigStep.class)
                .eq(DagConfigStep::getDagId, stepDTO.getDagId())
                .eq(DagConfigStep::getStepId, stepDTO.getStepId());
        if (dagConfigStepMapper.exists(queryWrapper)) {
            return update(stepDTO);
        } else {
            return insert(stepDTO);
        }
    }

    @Override
    public int deleteByDag(Long dagId) {
        LambdaUpdateWrapper<DagConfigStep> updateWrapper = Wrappers.lambdaUpdate(DagConfigStep.class)
                .eq(DagConfigStep::getDagId, dagId);
        return dagConfigStepMapper.delete(updateWrapper);
    }

    @Override
    public int deleteByDag(List<Long> dagIds) {
        LambdaUpdateWrapper<DagConfigStep> updateWrapper = Wrappers.lambdaUpdate(DagConfigStep.class)
                .in(DagConfigStep::getDagId, dagIds);
        return dagConfigStepMapper.delete(updateWrapper);
    }

    @Override
    public int deleteSurplusSteps(Long dagId, List<String> stepIds) {
        LambdaUpdateWrapper<DagConfigStep> updateWrapper = Wrappers.lambdaUpdate(DagConfigStep.class)
                .eq(DagConfigStep::getDagId, dagId)
                .notIn(CollectionUtils.isEmpty(stepIds) == false, DagConfigStep::getStepId, stepIds);
        return dagConfigStepMapper.delete(updateWrapper);
    }

    @Override
    public int clone(Long sourceDagId, Long targetDagId) {
        List<DagConfigStepDTO> sourceSteps = listSteps(sourceDagId);
        sourceSteps.forEach(stepDTO -> {
            stepDTO.setDagId(targetDagId);
            stepDTO.setId(null);
            stepDTO.setCreator(null);
            stepDTO.setCreateTime(null);
            stepDTO.setEditor(null);
            stepDTO.setUpdateTime(null);
            insert(stepDTO);
        });
        return sourceSteps.size();
    }
}
