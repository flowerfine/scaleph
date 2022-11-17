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

import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowSchedule;
import cn.sliew.scaleph.system.util.SpringApplicationContextUtil;
import cn.sliew.scaleph.workflow.engine.Engine;
import cn.sliew.scaleph.workflow.engine.EngineBuilder;
import cn.sliew.scaleph.workflow.engine.action.Action;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.ParallelFlow;
import cn.sliew.scaleph.workflow.engine.workflow.WorkFlow;
import cn.sliew.scaleph.workflow.service.WorkflowDefinitionService;
import cn.sliew.scaleph.workflow.service.WorkflowInstanceService;
import cn.sliew.scaleph.workflow.service.WorkflowTaskDefinitionService;
import cn.sliew.scaleph.workflow.service.dto.WorkflowDefinitionDTO;
import cn.sliew.scaleph.workflow.service.dto.WorkflowInstanceDTO;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskDefinitionDTO;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.ClassUtils;

import java.util.List;

@Slf4j
public class QuartzJobHandler extends QuartzJobBean {

    private Engine engine = EngineBuilder.newInstance().build();

    @Autowired
    private WorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private WorkflowInstanceService workflowInstanceService;
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
        WorkflowInstanceDTO workflowInstanceDTO = workflowInstanceService.start(workflowDefinitionDTO.getId());
        ActionContext actionContext = buildActionContext(context, workflowDefinitionDTO, workflowInstanceDTO);
        List<WorkflowTaskDefinitionDTO> workflowTaskDefinitionDTOS = workflowTaskDefinitionService.list(workflowDefinitionDTO.getId());
        // 应该是对 task 的上下游关系进行梳理后，进而执行
        Action[] actions = workflowTaskDefinitionDTOS.stream().map(workflowTaskDefinition -> {
            try {
                Class<?> clazz = ClassUtils.forName(workflowTaskDefinition.getHandler(), ClassUtils.getDefaultClassLoader());
                return (Action) SpringApplicationContextUtil.getBean(clazz);
            } catch (ClassNotFoundException e) {
                Rethrower.throwAs(e);
                return null;
            }
        }).toArray(length -> new Action[length]);
        WorkFlow workFlow = ParallelFlow.newParallelFlow()
                .name(workflowDefinitionDTO.getName())
                .execute(actions)
                .build();
        engine.run(workFlow, actionContext, new ActionListener<ActionResult>() {
            @Override
            public void onResponse(ActionResult result) {
                log.info("workflow {} run success!", workflowDefinitionDTO.getName());
            }

            @Override
            public void onFailure(Exception e) {
                log.error("workflow {} run failure!", workflowDefinitionDTO.getName(), e);
            }
        });
    }

    private ActionContext buildActionContext(JobExecutionContext context, WorkflowDefinitionDTO definitionDTO, WorkflowInstanceDTO instanceDTO) {
        return ActionContextBuilder.newBuilder()
                .withWorkflowDefinitionId(definitionDTO.getId())
                .withWorkflowInstanceId(instanceDTO.getId())
                .withParams(definitionDTO.getParam())
                .withPreviousFireTime(context.getPreviousFireTime())
                .withNextFireTime(context.getNextFireTime())
                .withScheduledFireTime(context.getScheduledFireTime())
                .withFireTime(context.getFireTime())
                .validateAndBuild();
    }

}
