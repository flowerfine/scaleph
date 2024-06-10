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

package cn.sliew.scaleph.application.doris.action;

import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.application.doris.service.WsDorisOperatorInstanceService;
import cn.sliew.scaleph.application.doris.operator.status.DorisClusterStatus;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.action.ActionStatus;
import cn.sliew.scaleph.workflow.engine.action.DefaultActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.AbstractWorkFlow;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DorisOperatorInstanceStatusSyncJob extends AbstractWorkFlow {

    @Autowired
    private WsDorisOperatorInstanceService wsDorisOperatorInstanceService;

    public DorisOperatorInstanceStatusSyncJob() {
        super("DORIS_OPERATOR_INSTANCE_STATUS_SYNC_JOB");
    }

    @Override
    protected Runnable doExecute(ActionContext context, ActionListener<ActionResult> listener) {
        return () -> process(context, listener);
    }

    private void process(ActionContext context, ActionListener<ActionResult> listener) {
        List<Long> ids = wsDorisOperatorInstanceService.listAll();
        ids.forEach(this::doProcess);
        log.debug("update doris operator instance status success! update size: {}", ids.size());
        listener.onResponse(new DefaultActionResult(ActionStatus.SUCCESS, context));
    }

    private void doProcess(Long id) {
        try {
            Optional<GenericKubernetesResource> optional = wsDorisOperatorInstanceService.getStatusWithoutManagedFields(id);
            if (optional.isPresent()) {
                String json = JacksonUtil.toJsonString(optional.get().get("status"));
                DorisClusterStatus status = JacksonUtil.parseJsonString(json, DorisClusterStatus.class);
                wsDorisOperatorInstanceService.updateStatus(id, status);
            } else {
                wsDorisOperatorInstanceService.clearStatus(id);
            }
        } catch (Exception e) {
            log.error("update doris operator instance status error! id: {}", id, e);
        }
    }
}
