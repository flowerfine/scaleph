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
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.action.ActionStatus;
import cn.sliew.scaleph.workflow.engine.action.DefaultActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.AbstractWorkFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class FlinkJobStatusSyncJobStepOne extends AbstractWorkFlow {

    public FlinkJobStatusSyncJobStepOne() {
        super("FLINK_JOB_STATUS_SYNC_JOB_STEP_ONE");
    }

    @Override
    protected Runnable doExecute(ActionContext context, ActionListener<ActionResult> listener) {
        return () -> process(context, listener);
    }

    private void process(ActionContext context, ActionListener<ActionResult> listener) {
        log.info("update flink kubernetes job status step-1, globalInputs: {}, inputs: {}",
                JacksonUtil.toJsonString(context.getGlobalInputs()), JacksonUtil.toJsonString(context.getInputs()));
        Map<String, Object> outputs = context.getOutputs();
        outputs.put("output1", "value1");
        listener.onResponse(new DefaultActionResult(ActionStatus.SUCCESS, context));
    }

}
