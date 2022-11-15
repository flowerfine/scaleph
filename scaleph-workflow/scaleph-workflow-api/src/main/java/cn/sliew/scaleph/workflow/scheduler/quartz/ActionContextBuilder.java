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

import cn.sliew.scaleph.workflow.engine.action.ActionContext;

import java.util.Date;
import java.util.Map;

public class ActionContextBuilder {

    public static ActionContextBuilder newBuilder() {
        return new ActionContextBuilder();
    }

    private ActionContext context = new ActionContext();

    public ActionContextBuilder withWorkflowDefinitionId(Long workflowDefinitionId) {
        context.setWorkflowDefinitionId(workflowDefinitionId);
        return this;
    }

    public ActionContextBuilder withWorkflowInstanceId(Long workflowInstanceId) {
        context.setWorkflowInstanceId(workflowInstanceId);
        return this;
    }

    public ActionContextBuilder withPreviousFireTime(Date previousFireTime) {
        context.setPreviousFireTime(previousFireTime);
        return this;
    }

    public ActionContextBuilder withNextFireTime(Date nextFireTime) {
        context.setNextFireTime(nextFireTime);
        return this;
    }

    public ActionContextBuilder withScheduledFireTime(Date scheduledFireTime) {
        context.setScheduledFireTime(scheduledFireTime);
        return this;
    }

    public ActionContextBuilder withFireTime(Date fireTime) {
        context.setFireTime(fireTime);
        return this;
    }

    public ActionContextBuilder withParams(Map<String, Object> params) {
        context.setParams(params);
        return this;
    }

    public ActionContext validateAndBuild() {
        return context;
    }
}
