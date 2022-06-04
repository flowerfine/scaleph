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

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.enums.TaskResultEnum;
import cn.sliew.scaleph.log.service.LogScheduleService;
import cn.sliew.scaleph.log.service.dto.LogScheduleDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import cn.sliew.scaleph.system.util.SpringApplicationContextUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Date;

@Slf4j
public class QuartzJobListener implements JobListener {
    @Override
    public String getName() {
        return "quartz-job-listener";
    }

    @SneakyThrows
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        JobKey jobKey = jobDetail.getKey();
        LogScheduleDTO logDTO = new LogScheduleDTO();
        logDTO.setStartTime(new Date());
        logDTO.setTaskGroup(jobKey.getGroup());
        logDTO.setTaskName(jobKey.getName());
        logDTO.appendLog(String.format("job %s in group %s begin running...", jobKey.getName(), jobKey.getGroup()));
        JobDataMap dataMap = jobDetail.getJobDataMap();
        dataMap.put(Constants.JOB_LOG_KEY, logDTO);
        log.debug("job {} in group {} begin running... ", jobKey.getName(), jobKey.getGroup());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        log.debug("job {} in group {} execute vetoed", jobKey.getName(), jobKey.getGroup());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        JobDetail jobDetail = context.getJobDetail();
        JobKey jobKey = jobDetail.getKey();
        JobDataMap dataMap = jobDetail.getJobDataMap();
        LogScheduleDTO logDTO = (LogScheduleDTO) dataMap.get(Constants.JOB_LOG_KEY);
        logDTO.setEndTime(new Date());
        if (jobException == null) {
            logDTO.setResult(
                    new DictVO(TaskResultEnum.SUCCESS.getCode(), TaskResultEnum.SUCCESS.getValue()));
        } else {
            logDTO.setResult(
                    new DictVO(TaskResultEnum.FAILURE.getCode(), TaskResultEnum.FAILURE.getValue()));
        }
        logDTO.appendLog(StrUtil.format("job {} in group {} execute completed", jobKey.getName(),
                jobKey.getGroup()));
        LogScheduleService
                logScheduleService = SpringApplicationContextUtil.getBean(LogScheduleService.class);
        logScheduleService.insert(logDTO);
        log.debug("job {} in group {} execute completed", jobKey.getName(), jobKey.getGroup());
    }
}
