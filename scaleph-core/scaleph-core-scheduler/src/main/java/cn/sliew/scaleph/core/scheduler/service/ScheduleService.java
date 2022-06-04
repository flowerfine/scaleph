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

package cn.sliew.scaleph.core.scheduler.service;

import cn.sliew.scaleph.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScheduleService {

    @Autowired
    private Scheduler scheduler;

    public void addScheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        this.scheduler.scheduleJob(jobDetail, trigger);
        log.info("schedule job {} with job group {} add success", jobDetail.getKey().getName(),
                jobDetail.getKey().getGroup());
    }

    public void deleteScheduleJob(JobKey jobKey) throws SchedulerException {
        if (checkExists(jobKey)) {
            this.scheduler.deleteJob(jobKey);
        }
    }

    public void pauseJob(JobKey jobKey) throws SchedulerException {
        if (checkExists(jobKey)) {
            scheduler.pauseJob(jobKey);
        }
    }

    public void resumeJob(JobKey jobKey) throws SchedulerException {
        if (checkExists(jobKey)) {
            scheduler.resumeJob(jobKey);
        }
    }

    public boolean checkExists(JobKey jobKey) throws SchedulerException {
        return scheduler.checkExists(jobKey);
    }

    public JobKey getJobKey(String jobName, String groupName) {
        return JobKey.jobKey(Constants.JOB_PREFIX + jobName,
                Constants.JOB_GROUP_PREFIX + groupName);
    }

    public TriggerKey getTriggerKey(String triggerName, String groupName) {
        return TriggerKey.triggerKey(Constants.TRIGGER_PREFIX + triggerName,
                Constants.TRIGGER_GROUP_PREFIX + groupName);
    }

    public void addJobListener(JobListener jobListener, Matcher<JobKey> matcher) throws SchedulerException {
        scheduler.getListenerManager().addJobListener(jobListener, matcher);
    }

}
