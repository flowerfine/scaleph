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

package cn.sliew.scaleph.workflow.service.impl;

import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowSchedule;
import cn.sliew.scaleph.dao.mapper.master.workflow.WorkflowScheduleMapper;
import cn.sliew.scaleph.workflow.scheduler.SchedulerService;
import cn.sliew.scaleph.workflow.service.WorkflowScheduleService;
import cn.sliew.scaleph.workflow.service.convert.WorkflowScheduleConvert;
import cn.sliew.scaleph.workflow.service.dto.WorkflowScheduleDTO;
import cn.sliew.scaleph.workflow.service.param.WorkflowScheduleAddParam;
import cn.sliew.scaleph.workflow.service.param.WorkflowScheduleListParam;
import cn.sliew.scaleph.workflow.service.param.WorkflowScheduleUpdateParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WorkflowScheduleServiceImpl implements WorkflowScheduleService {

    @Autowired
    private WorkflowScheduleMapper workflowScheduleMapper;
    @Autowired
    private SchedulerService schedulerService;

    @Override
    public List<WorkflowScheduleDTO> list(WorkflowScheduleListParam param) {
        LambdaQueryWrapper<WorkflowSchedule> queryWrapper = Wrappers.lambdaQuery(WorkflowSchedule.class)
                .eq(WorkflowSchedule::getWorkflowDefinitionId, param.getWorkflowDefinitionId())
                .eq(param.getStatus() != null, WorkflowSchedule::getStatus, param.getStatus());
        List<WorkflowSchedule> workflowSchedules = workflowScheduleMapper.selectList(queryWrapper);
        return WorkflowScheduleConvert.INSTANCE.toDto(workflowSchedules);
    }

    @Override
    public WorkflowScheduleDTO get(Long id) {
        WorkflowSchedule record = workflowScheduleMapper.selectById(id);
        checkState(record != null, () -> "workflow schedule not exists for id: " + id);
        return WorkflowScheduleConvert.INSTANCE.toDto(record);
    }

    @Override
    public void insert(WorkflowScheduleAddParam param) {
        WorkflowSchedule record = BeanUtil.copy(param, new WorkflowSchedule());
        workflowScheduleMapper.insert(record);
    }

    @Override
    public void update(Long id, WorkflowScheduleUpdateParam param) {
        WorkflowSchedule record = BeanUtil.copy(param, new WorkflowSchedule());
        record.setId(id);
        workflowScheduleMapper.updateById(record);
    }

    @Override
    public void delete(Long id) {
        workflowScheduleMapper.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        workflowScheduleMapper.deleteBatchIds(ids);
    }

    @Override
    public void enable(Long id) {
        schedulerService.schedule(id);
    }

    @Override
    public void disable(Long id) {
        schedulerService.unschedule(id);
    }

}
