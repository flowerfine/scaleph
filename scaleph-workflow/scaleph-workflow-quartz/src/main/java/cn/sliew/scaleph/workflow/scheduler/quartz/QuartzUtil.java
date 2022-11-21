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

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.workflow.service.dto.WorkflowScheduleDTO;
import org.quartz.*;

import java.util.Map;
import java.util.TimeZone;

public enum QuartzUtil {
    ;

    public static final String WORKFLOW_SCHEDULE = "workflowSchedule";

    public static JobDataMap buildDataMap(WorkflowScheduleDTO schedule) {
        WorkflowScheduleDTO copy = BeanUtil.copy(schedule, new WorkflowScheduleDTO());
        copy.setStatus(null);
        return new JobDataMap(Map.of(WORKFLOW_SCHEDULE, JacksonUtil.toJsonString(copy)));
    }

    public static JobKey getJobKey(Long projectId, WorkflowScheduleDTO schedule) {
        String jobGroup = String.format("%s_%s", Constants.JOB_GROUP_PREFIX, projectId);
        String jobName = String.format("%s_%s", Constants.JOB_PREFIX, schedule.getId());
        return new JobKey(jobName, jobGroup);
    }

    public static TriggerKey getTriggerKey(Long projectId, WorkflowScheduleDTO schedule) {
        String triggerGroup = String.format("%s_%s", Constants.TRIGGER_GROUP_PREFIX, projectId);
        String triggerName = String.format("%s_%s", Constants.TRIGGER_PREFIX, schedule.getId());
        return TriggerKey.triggerKey(triggerName, triggerGroup);
    }

    private static ScheduleBuilder buildSchedule(WorkflowScheduleDTO schedule) {
        return CronScheduleBuilder
                .cronSchedule(schedule.getCrontab())
                .inTimeZone(TimeZone.getTimeZone(schedule.getTimezone()));
    }

    public static Trigger getTrigger(Long projectId, WorkflowScheduleDTO schedule) {
        return TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(projectId, schedule))
                .startAt(schedule.getStartTime())
                .endAt(schedule.getEndTime())
                .withSchedule(buildSchedule(schedule))
                .build();
    }

}
