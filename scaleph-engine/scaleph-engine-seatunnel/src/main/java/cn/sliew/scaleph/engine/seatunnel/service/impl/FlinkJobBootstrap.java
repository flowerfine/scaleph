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

package cn.sliew.scaleph.engine.seatunnel.service.impl;

import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.core.scheduler.service.ScheduleService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class FlinkJobBootstrap implements ApplicationRunner {

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        JobKey syncJobStatusKey =
                scheduleService.getJobKey("SYNC_JOB_STATUS_FROM_CLUSTER_JOB", Constants.INTERNAL_GROUP);
        JobDetail syncJob = JobBuilder.newJob(FlinkJobStatusSyncJob.class)
                .withIdentity(syncJobStatusKey)
                .storeDurably()
                .build();
        TriggerKey syncJobTriggerKey =
                scheduleService.getTriggerKey("SYNC_JOB_STATUS_FROM_CLUSTER_TRI", Constants.INTERNAL_GROUP);
        Trigger syncJobTri = TriggerBuilder.newTrigger()
                .withIdentity(syncJobTriggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(Constants.CRON_EVERY_THREE_SECONDS))
                .build();
        if (scheduleService.checkExists(syncJobStatusKey)) {
            scheduleService.deleteScheduleJob(syncJobStatusKey);
        }
        this.scheduleService.addScheduleJob(syncJob, syncJobTri);
    }
}
