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

package cn.sliew.scaleph.engine.flink.service.action;

import cn.sliew.flinkful.rest.base.JobClient;
import cn.sliew.flinkful.rest.base.RestClient;
import cn.sliew.flinkful.rest.client.FlinkRestClient;
import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.scaleph.common.dict.flink.FlinkJobState;
import cn.sliew.scaleph.engine.flink.service.WsFlinkJobInstanceService;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkJobInstanceDTO;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.AbstractWorkFlow;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.rest.messages.job.JobDetailsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class FlinkJobStatusSyncJob extends AbstractWorkFlow {

    @Autowired
    private WsFlinkJobInstanceService wsFlinkJobInstanceService;

    public FlinkJobStatusSyncJob() {
        super("FLINK_JOB_STATUS_SYNC_JOB");
    }

    @Override
    protected Runnable doExecute(ActionContext context, ActionListener<ActionResult> listener) {
        return () -> {
            log.info("Flink Job Status Sync Action execute!");
            List<WsFlinkJobInstanceDTO> jobList = wsFlinkJobInstanceService.listAll();
            for (WsFlinkJobInstanceDTO instance : jobList) {
                final URL url;
                try {
                    url = new URL(instance.getWebInterfaceUrl());
                    final String jobId = instance.getJobId();
                    RestClient restClient = new FlinkRestClient(url.getHost(), url.getPort(), new Configuration());
                    final JobClient jobClient = restClient.job();
                    JobDetailsInfo jobInfo = jobClient.jobDetail(jobId).get();
                    instance.setJobState(FlinkJobState.of(jobInfo.getJobStatus().name()));
                    instance.setStartTime(new Date(jobInfo.getStartTime()));
                    instance.setEndTime(new Date(jobInfo.getEndTime()));
                    instance.setDuration(jobInfo.getDuration());
                    wsFlinkJobInstanceService.upsert(instance);
                    log.info("Flink Job {} Status Sync Action executed!", instance.getJobId());
                } catch (Exception e) {
                    log.info("Flink Job Status Sync Action Error, job code is {} and job id is {}", instance.getFlinkJobCode(), instance.getJobId());
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
