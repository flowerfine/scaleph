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

package cn.sliew.scaleph.application.flink.action;

import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.application.flink.operator.status.FlinkDeploymentStatus;
import cn.sliew.scaleph.application.flink.service.WsFlinkKubernetesSessionClusterService;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.action.ActionStatus;
import cn.sliew.scaleph.workflow.engine.action.DefaultActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.AbstractWorkFlow;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FlinkSessionClusterStatusSyncJob extends AbstractWorkFlow {

    @Autowired
    private WsFlinkKubernetesSessionClusterService wsFlinkKubernetesSessionClusterService;

    public FlinkSessionClusterStatusSyncJob() {
        super("FLINK_SESSION_CLUSTER_STATUS_SYNC_JOB");
    }

    @Override
    protected Runnable doExecute(ActionContext context, ActionListener<ActionResult> listener) {
        return () -> process(context, listener);
    }

    private void process(ActionContext context, ActionListener<ActionResult> listener) {
        List<Long> sessionClusterIds = wsFlinkKubernetesSessionClusterService.listAll();
        sessionClusterIds.forEach(this::doProcess);
        log.debug("update flink kubernetes session-cluster status success! update size: {}", sessionClusterIds.size());
        listener.onResponse(new DefaultActionResult(ActionStatus.SUCCESS, context));
    }

    private void doProcess(Long sessionClusterId) {
        try {
            Optional<GenericKubernetesResource> optional = wsFlinkKubernetesSessionClusterService.getStatus(sessionClusterId);
            if (optional.isPresent()) {
                String json = JacksonUtil.toJsonString(optional.get().get("status"));
                FlinkDeploymentStatus status = JacksonUtil.parseJsonString(json, FlinkDeploymentStatus.class);
                wsFlinkKubernetesSessionClusterService.updateStatus(sessionClusterId, status);
            } else {
                wsFlinkKubernetesSessionClusterService.clearStatus(sessionClusterId);
            }
        } catch (Exception e) {
            log.error("update flink kubernetes session-cluster status error! id: {}", sessionClusterId, e);
        }
    }
}
