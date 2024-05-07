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

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowSchedule;
import cn.sliew.scaleph.workflow.manager.WorkflowInstanceManager;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
public class QuartzJobHandler extends QuartzJobBean {

    @Autowired
    private WorkflowInstanceManager workflowInstanceManager;

    /**
     * 路由分发任务
     * 1. 根据调度数据，获取 workflow definition
     * 2. 执行 workflow 下的 task。根据 task 类型，比如 java 类型，则通过反射调用
     * 任务的执行，交由 workflow manager 管理
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        String json = dataMap.getString(QuartzUtil.WORKFLOW_SCHEDULE);
        WorkflowSchedule workflowSchedule = JacksonUtil.parseJsonString(json, WorkflowSchedule.class);
        workflowInstanceManager.deploy(workflowSchedule.getWorkflowDefinitionId());
    }
}
