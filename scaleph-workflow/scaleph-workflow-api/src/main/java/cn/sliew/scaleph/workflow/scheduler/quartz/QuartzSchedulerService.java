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

package cn.sliew.scaleph.workflow.scheduler.quartz;

import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.scaleph.common.dict.workflow.ScheduleStatus;
import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowSchedule;
import cn.sliew.scaleph.dao.mapper.master.workflow.WorkflowScheduleMapper;
import cn.sliew.scaleph.workflow.scheduler.SchedulerService;
import cn.sliew.scaleph.workflow.service.convert.WorkflowScheduleConvert;
import cn.sliew.scaleph.workflow.service.dto.WorkflowScheduleDTO;
import org.quartz.*;
import org.quartz.impl.matchers.EverythingMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Component
public class QuartzSchedulerService implements SchedulerService, InitializingBean {

    private static final Long PROJECT_ID = 1L;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private WorkflowScheduleMapper workflowScheduleMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        scheduler.getListenerManager().addJobListener(new QuartzJobListener(), EverythingMatcher.allJobs());
    }

    @Override
    public boolean exists(Long id) {
        try {
            WorkflowScheduleDTO schedule = getSchedule(id);
            JobKey jobKey = QuartzUtil.getJobKey(PROJECT_ID, schedule);
            return scheduler.checkExists(jobKey);
        } catch (SchedulerException e) {
            Rethrower.throwAs(e);
            return false;
        }
    }

    @Override
    public void schedule(Long id) {
        try {
            WorkflowScheduleDTO schedule = getSchedule(id);
            JobKey jobKey = QuartzUtil.getJobKey(PROJECT_ID, schedule);
            if (scheduler.checkExists(jobKey) == true) {
                return;
            }
            JobDataMap dataMap = QuartzUtil.buildDataMap(schedule);
            JobDetail job = JobBuilder.newJob(QuartzJobHandler.class)
                    .withIdentity(jobKey)
                    .usingJobData(dataMap)
                    .storeDurably()
                    .build();
            Trigger trigger = QuartzUtil.getTrigger(PROJECT_ID, schedule);
            scheduler.scheduleJob(job, trigger);
            updateScheduelStatus(id, ScheduleStatus.RUNNING);
        } catch (SchedulerException e) {
            Rethrower.throwAs(e);
        }
    }

    @Override
    public void unschedule(Long id) {
        try {
            WorkflowScheduleDTO schedule = getSchedule(id);
            JobKey jobKey = QuartzUtil.getJobKey(PROJECT_ID, schedule);
            if (scheduler.checkExists(jobKey) == false) {
                return;
            }
            scheduler.deleteJob(jobKey);
            updateScheduelStatus(id, ScheduleStatus.STOP);
        } catch (SchedulerException e) {
            Rethrower.throwAs(e);
        }
    }

    @Override
    public void suspend(Long id) {
        try {
            WorkflowScheduleDTO schedule = getSchedule(id);
            JobKey jobKey = QuartzUtil.getJobKey(PROJECT_ID, schedule);
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            Rethrower.throwAs(e);
        }
    }

    @Override
    public void resume(Long id) {
        try {
            WorkflowScheduleDTO schedule = getSchedule(id);
            JobKey jobKey = QuartzUtil.getJobKey(PROJECT_ID, schedule);
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            Rethrower.throwAs(e);
        }
    }

    @Override
    public void terminate(Long id) {
        try {
            WorkflowScheduleDTO schedule = getSchedule(id);
            JobKey jobKey = QuartzUtil.getJobKey(PROJECT_ID, schedule);
            scheduler.interrupt(jobKey);
        } catch (UnableToInterruptJobException e) {
            Rethrower.throwAs(e);
        }
    }

    @Override
    public List<Date> listNext5FireTime(String crontabStr) throws ParseException {
        CronTriggerImpl cronTrigger = new CronTriggerImpl();
        cronTrigger.setCronExpression(crontabStr);
        return TriggerUtils.computeFireTimes(cronTrigger, null, 5);
    }

    private WorkflowScheduleDTO getSchedule(Long id) {
        WorkflowSchedule record = workflowScheduleMapper.selectById(id);
        return WorkflowScheduleConvert.INSTANCE.toDto(record);
    }

    private void updateScheduelStatus(Long id, ScheduleStatus status) {
        WorkflowSchedule record = new WorkflowSchedule();
        record.setId(id);
        record.setStatus(status);
        workflowScheduleMapper.updateById(record);
    }

}
