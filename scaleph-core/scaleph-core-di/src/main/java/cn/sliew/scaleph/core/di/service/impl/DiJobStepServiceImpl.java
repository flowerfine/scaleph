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

package cn.sliew.scaleph.core.di.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.sliew.scaleph.core.di.service.DiJobStepService;
import cn.sliew.scaleph.core.di.service.convert.DiJobStepConvert;
import cn.sliew.scaleph.core.di.service.dto.DiJobStepDTO;
import cn.sliew.scaleph.dao.entity.master.di.DiJobStep;
import cn.sliew.scaleph.dao.mapper.master.di.DiJobStepMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author gleiyu
 */
@Service
public class DiJobStepServiceImpl implements DiJobStepService {

    @Autowired
    private DiJobStepMapper diJobStepMapper;

    @Override
    public int update(DiJobStepDTO diJobStepDTO) {
        DiJobStep step = DiJobStepConvert.INSTANCE.toDo(diJobStepDTO);
        return this.diJobStepMapper.update(step, new LambdaUpdateWrapper<DiJobStep>()
                .eq(DiJobStep::getJobId, step.getJobId())
                .eq(DiJobStep::getStepCode, step.getStepCode())
        );
    }

    @Override
    public int upsert(DiJobStepDTO diJobStep) {
        DiJobStep step = this.diJobStepMapper.selectOne(
                new LambdaQueryWrapper<DiJobStep>()
                        .eq(DiJobStep::getJobId, diJobStep.getJobId())
                        .eq(DiJobStep::getStepCode, diJobStep.getStepCode())
        );
        DiJobStep jobStep = DiJobStepConvert.INSTANCE.toDo(diJobStep);
        if (step == null) {
            return this.diJobStepMapper.insert(jobStep);
        } else {
            return this.diJobStepMapper.update(jobStep,
                    new LambdaUpdateWrapper<DiJobStep>()
                            .eq(DiJobStep::getJobId, jobStep.getJobId())
                            .eq(DiJobStep::getStepCode, jobStep.getStepCode())
            );
        }
    }

    @Override
    public int deleteByProjectId(Collection<? extends Serializable> projectIds) {
        return this.diJobStepMapper.deleteByProjectId(projectIds);
    }

    @Override
    public int deleteByJobId(Collection<? extends Serializable> jobIds) {
        return this.diJobStepMapper.deleteByJobId(jobIds);
    }

    @Override
    public int deleteSurplusStep(Long jobId, List<String> stepCodeList) {
        return this.diJobStepMapper.delete(
                new LambdaQueryWrapper<DiJobStep>()
                        .eq(DiJobStep::getJobId, jobId)
                        .notIn(CollectionUtil.isNotEmpty(stepCodeList), DiJobStep::getStepCode,
                                stepCodeList)
        );
    }

    @Override
    public List<DiJobStepDTO> listJobStep(Long jobId) {
        List<DiJobStep> list = this.diJobStepMapper.selectByJobId(jobId);
        return DiJobStepConvert.INSTANCE.toDto(list);
    }

    @Override
    public DiJobStepDTO selectOne(Long jobId, String stepCode) {
        DiJobStep step = this.diJobStepMapper.selectOne(
                new LambdaQueryWrapper<DiJobStep>()
                        .eq(DiJobStep::getJobId, jobId)
                        .eq(DiJobStep::getStepCode, stepCode)
        );
        return DiJobStepConvert.INSTANCE.toDto(step);
    }

    @Override
    public int clone(Long sourceJobId, Long targetJobId) {
        return this.diJobStepMapper.clone(sourceJobId, targetJobId);
    }
}
