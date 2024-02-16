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

package cn.sliew.scaleph.workspace.flink.kubernetes.action;

import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.workspace.flink.kubernetes.operator.status.FlinkDeploymentStatus;
import cn.sliew.scaleph.workspace.flink.kubernetes.service.WsFlinkKubernetesJobInstanceService;
import cn.sliew.scaleph.workspace.flink.kubernetes.service.WsFlinkKubernetesJobService;
import cn.sliew.scaleph.workspace.flink.kubernetes.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.AbstractWorkFlow;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FlinkJobStatusSyncJob extends AbstractWorkFlow {

    @Autowired
    private WsFlinkKubernetesJobService wsFlinkKubernetesJobService;
    @Autowired
    private WsFlinkKubernetesJobInstanceService wsFlinkKubernetesJobInstanceService;

    public FlinkJobStatusSyncJob() {
        super("FLINK_JOB_STATUS_SYNC_JOB");
    }

    @Override
    protected Runnable doExecute(ActionContext context, ActionListener<ActionResult> listener) {
        return () -> process();
    }

    private void process() {
        List<Long> jobIds = wsFlinkKubernetesJobService.listAll();
        jobIds.forEach(this::doProcess);
        log.debug("update flink kubernetes job status success! update size: {}", jobIds.size());
    }

    private void doProcess(Long jobId) {
        try {
            Optional<WsFlinkKubernetesJobInstanceDTO> jobInstanceDTOOptional = wsFlinkKubernetesJobInstanceService.selectCurrent(jobId);
            if (jobInstanceDTOOptional.isEmpty()) {
                return;
            }
            WsFlinkKubernetesJobInstanceDTO jobInstanceDTO = jobInstanceDTOOptional.get();
            Optional<GenericKubernetesResource> optional = wsFlinkKubernetesJobInstanceService.getStatus(jobInstanceDTO.getId());
            if (optional.isPresent()) {
                String json = JacksonUtil.toJsonString(optional.get().get("status"));
                FlinkDeploymentStatus status = JacksonUtil.parseJsonString(json, FlinkDeploymentStatus.class);
                wsFlinkKubernetesJobInstanceService.updateStatus(jobInstanceDTO.getId(), status);
            } else {
                wsFlinkKubernetesJobInstanceService.clearStatus(jobInstanceDTO.getId());
            }
        } catch (Exception e) {
            log.error("update flink kubernetes job status error! id: {}", jobId, e);
        }
    }

}
