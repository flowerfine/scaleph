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

import cn.sliew.flinkful.rest.base.JobClient;
import cn.sliew.flinkful.rest.base.RestClient;
import cn.sliew.flinkful.rest.client.FlinkRestClient;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.constant.DictConstants;
import cn.sliew.scaleph.common.dict.job.RuntimeState;
import cn.sliew.scaleph.common.enums.JobRuntimeStateEnum;
import cn.sliew.scaleph.core.di.service.DiClusterConfigService;
import cn.sliew.scaleph.core.di.service.DiJobLogService;
import cn.sliew.scaleph.core.di.service.DiJobService;
import cn.sliew.scaleph.core.di.service.DiProjectService;
import cn.sliew.scaleph.core.di.service.dto.DiClusterConfigDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobLogDTO;
import cn.sliew.scaleph.core.di.service.dto.DiProjectDTO;
import cn.sliew.scaleph.core.scheduler.service.ScheduleService;
import cn.sliew.scaleph.engine.seatunnel.service.util.QuartzJobUtil;
import cn.sliew.scaleph.log.service.dto.LogScheduleDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import org.apache.flink.api.common.JobStatus;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.configuration.JobManagerOptions;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.runtime.rest.messages.job.JobDetailsInfo;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class FlinkJobStatusSyncJob extends QuartzJobBean {

    @Autowired
    private DiJobService diJobService;
    @Autowired
    private DiJobLogService diJobLogService;
    @Autowired
    private DiClusterConfigService diClusterConfigService;
    @Autowired
    private DiProjectService diProjectService;
    @Autowired
    private ScheduleService scheduleService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        LogScheduleDTO logDTO = (LogScheduleDTO) dataMap.get(Constants.JOB_LOG_KEY);
        logDTO.appendLog("start synchronization task status...");
        List<DiJobLogDTO> list = this.diJobLogService.listRunningJobInstance(null);
        logDTO.appendLog(String.format("There are %d jobs is running", list.size()));
        Configuration configuration = GlobalConfiguration.loadConfiguration();
        for (DiJobLogDTO jobLog : list) {
            DiClusterConfigDTO clusterInfo =
                    diClusterConfigService.selectOne(jobLog.getClusterId());
            String host = clusterInfo.getConfig().get(JobManagerOptions.ADDRESS.key());
            int restPort = Integer.parseInt(clusterInfo.getConfig().get(RestOptions.PORT.key()));
            RestClient client = new FlinkRestClient(host, restPort, configuration);
            JobClient jobClient = client.job();
            logDTO.appendLog(String.format(
                    "start synchronizing the job status,jobId = %s,clusterHost = %s,clusterPort = %s",
                    jobLog.getJobInstanceId(), host, restPort));
            try {
                CompletableFuture<JobDetailsInfo> jobDetailsInfoFuture =
                        jobClient.jobDetail(jobLog.getJobInstanceId());
                JobDetailsInfo jobDetailsInfo = jobDetailsInfoFuture.join();
                if (jobDetailsInfo != null) {
                    jobLog.setStartTime(new Date(jobDetailsInfo.getStartTime()));
                    if (jobDetailsInfo.getEndTime() > jobDetailsInfo.getStartTime()) {
                        jobLog.setEndTime(new Date(jobDetailsInfo.getEndTime()));
                    }
                    jobLog.setDuration(jobDetailsInfo.getDuration());
                    JobStatus jobStatus = jobDetailsInfo.getJobStatus();
                    jobLog.setJobInstanceState(
                            DictVO.toVO(DictConstants.JOB_INSTANCE_STATE, jobStatus.toString()));
                    if (jobStatus.isGloballyTerminalState()) {
                        DiJobDTO diJob = this.diJobService.selectOne(jobLog.getJobId());
                        DiProjectDTO diProject =
                                this.diProjectService.selectOne(diJob.getProjectId());
                        String jobName = diProject.getProjectCode() + '_' + diJob.getJobCode();
                        JobKey seatunnelJobKey = scheduleService.getJobKey(QuartzJobUtil.getFlinkBatchJobName(jobName), Constants.INTERNAL_GROUP);
                        if (scheduleService.checkExists(seatunnelJobKey)) {
                            diJob.setRuntimeState(RuntimeState.WAITING);
                        } else {
                            diJob.setRuntimeState(RuntimeState.STOP);
                        }
//                        this.diJobService.update(diJob);
                    }
                    this.diJobLogService.update(jobLog);
                    logDTO.appendLog(String.format("success synchronizing job status of job %s",
                            jobLog.getJobInstanceId()));
                } else {
                    logDTO.appendLog(
                            String.format("job status of job %s is null", jobLog.getJobInstanceId()));
                }
            } catch (IOException | SchedulerException e) {
                throw new JobExecutionException(
                        String.format(
                                "exception to get jobDetailsInfo with JobId: %s. exception info: %s",
                                jobLog.getJobInstanceId(),
                                e.getMessage()
                        )
                );
            }
        }
        logDTO.appendLog("finish synchronization task status...");
    }
}
