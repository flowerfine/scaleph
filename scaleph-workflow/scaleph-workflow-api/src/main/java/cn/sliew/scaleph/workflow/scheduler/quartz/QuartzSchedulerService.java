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
import cn.sliew.scaleph.workflow.scheduler.SchedulerService;
import cn.sliew.scaleph.workflow.service.WorkflowScheduleService;
import cn.sliew.scaleph.workflow.service.dto.WorkflowScheduleDTO;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuartzSchedulerService implements SchedulerService {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private WorkflowScheduleService workflowScheduleService;

    @Override
    public boolean exists(Long id) {
        try {
            WorkflowScheduleDTO schedule = getSchedule(id);
            JobKey jobKey = QuartzUtil.getJobKey(0L, schedule);
            return scheduler.checkExists(jobKey);
        } catch (SchedulerException e) {
            Rethrower.throwAs(e);
            return false;
        }
    }

    @Override
    public void schedule(Long id) {

    }

    @Override
    public void unschedule(Long id) {

    }

    @Override
    public void suspend(Long id) {

    }

    @Override
    public void resume(Long id) {

    }

    @Override
    public void terminate(Long id) {

    }

    private WorkflowScheduleDTO getSchedule(Long id) {
        return workflowScheduleService.get(id);
    }

}
