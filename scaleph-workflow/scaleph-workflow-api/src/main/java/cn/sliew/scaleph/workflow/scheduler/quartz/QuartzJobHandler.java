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

import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowSchedule;
import cn.sliew.scaleph.workflow.engine.Engine;
import cn.sliew.scaleph.workflow.engine.EngineBuilder;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.service.WorkflowDefinitionService;
import cn.sliew.scaleph.workflow.service.WorkflowTaskDefinitionService;
import cn.sliew.scaleph.workflow.service.dto.WorkflowDefinitionDTO;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskDefinitionDTO;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

public class QuartzJobHandler extends QuartzJobBean {

    private Engine engine = EngineBuilder.newInstance().build();

    @Autowired
    private WorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private WorkflowTaskDefinitionService workflowTaskDefinitionService;

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
        WorkflowDefinitionDTO workflowDefinitionDTO = workflowDefinitionService.get(workflowSchedule.getWorkflowDefinitionId());
        ActionContext actionContext = buildActionContext(context, workflowDefinitionDTO);
        List<WorkflowTaskDefinitionDTO> workflowTaskDefinitionDTOS = workflowTaskDefinitionService.list(workflowDefinitionDTO.getId());
        // 应该是对 task 的上下游关系进行梳理后，进而执行
        engine.run(null, actionContext, new ActionListener<ActionResult>() {
            @Override
            public void onResponse(ActionResult result) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private ActionContext buildActionContext(JobExecutionContext context, WorkflowDefinitionDTO definitionDTO) {
        return ActionContextBuilder.newBuilder()
                .withWorkflowDefinitionId(definitionDTO.getId())
                .withWorkflowInstanceId(null)
                .withParams(definitionDTO.getParam())
                .withPreviousFireTime(context.getPreviousFireTime())
                .withNextFireTime(context.getNextFireTime())
                .withScheduledFireTime(context.getScheduledFireTime())
                .withFireTime(context.getFireTime())
                .validateAndBuild();
    }

}
