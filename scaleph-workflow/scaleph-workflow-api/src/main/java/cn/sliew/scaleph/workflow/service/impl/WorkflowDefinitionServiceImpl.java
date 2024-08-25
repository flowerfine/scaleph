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

package cn.sliew.scaleph.workflow.service.impl;

import cn.sliew.carp.framework.dag.service.dto.DagConfigComplexDTO;
import cn.sliew.carp.framework.dag.service.dto.DagConfigLinkDTO;
import cn.sliew.carp.framework.dag.service.dto.DagConfigStepDTO;
import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowDefinition;
import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowDefinitionVO;
import cn.sliew.scaleph.dao.mapper.master.workflow.WorkflowDefinitionMapper;
import cn.sliew.scaleph.workflow.service.WorkflowDagService;
import cn.sliew.scaleph.workflow.service.WorkflowDefinitionService;
import cn.sliew.scaleph.workflow.service.convert.WorkflowDefinitionVOConvert;
import cn.sliew.scaleph.workflow.service.convert.WorkflowTaskDefitionConvert;
import cn.sliew.scaleph.workflow.service.dto.WorkflowDefinitionDTO;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskDefinitionDTO;
import cn.sliew.scaleph.workflow.service.param.WorkflowDefinitionListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WorkflowDefinitionServiceImpl implements WorkflowDefinitionService {

    @Autowired
    private WorkflowDefinitionMapper workflowDefinitionMapper;
    @Autowired
    private WorkflowDagService workflowDagService;

    @Override
    public Page<WorkflowDefinitionDTO> list(WorkflowDefinitionListParam param) {
        Page<WorkflowDefinition> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<WorkflowDefinitionVO> workflowDefinitionPage = workflowDefinitionMapper.list(page, param.getType(), param.getName());
        Page<WorkflowDefinitionDTO> result = new Page<>(workflowDefinitionPage.getCurrent(), workflowDefinitionPage.getSize(), workflowDefinitionPage.getTotal());
        List<WorkflowDefinitionDTO> workflowDefinitionDTOS = WorkflowDefinitionVOConvert.INSTANCE.toDto(workflowDefinitionPage.getRecords());
        result.setRecords(workflowDefinitionDTOS);
        return result;
    }

    @Override
    public WorkflowDefinitionDTO get(Long id) {
        WorkflowDefinitionVO record = workflowDefinitionMapper.get(id);
        checkState(record != null, () -> "workflow definition not exists for id: " + id);
        WorkflowDefinitionDTO dto = WorkflowDefinitionVOConvert.INSTANCE.toDto(record);
        dto.setDag(workflowDagService.getDag(record.getDagId()));
        return dto;
    }

    @Override
    public Graph<WorkflowTaskDefinitionDTO> getDag(Long id) {
        WorkflowDefinitionDTO workflowDefinitionDTO = get(id);
        DagConfigComplexDTO dag = workflowDefinitionDTO.getDag();
        return buildGraph(id, dag);
    }

    private MutableGraph<WorkflowTaskDefinitionDTO> buildGraph(Long id, DagConfigComplexDTO dag) {
        MutableGraph<WorkflowTaskDefinitionDTO> graph = GraphBuilder.directed().build();
        List<DagConfigStepDTO> steps = dag.getSteps();
        List<DagConfigLinkDTO> links = dag.getLinks();
        if (CollectionUtils.isEmpty(steps)) {
            return graph;
        }
        Map<String, WorkflowTaskDefinitionDTO> stepMap = new HashMap<>();
        for (DagConfigStepDTO step : steps) {
            WorkflowTaskDefinitionDTO taskDefinitionDTO = WorkflowTaskDefitionConvert.INSTANCE.toDto(step);
            taskDefinitionDTO.setWorkflowDefinitionId(id);
            graph.addNode(taskDefinitionDTO);
            stepMap.put(taskDefinitionDTO.getStepId(), taskDefinitionDTO);
        }
        links.forEach(link -> graph.putEdge(stepMap.get(link.getFromStepId()), stepMap.get(link.getToStepId())));
        return graph;
    }

    @Override
    public WorkflowTaskDefinitionDTO getTaskDefinition(Long workflowTaskDefinitionId) {
        DagConfigStepDTO step = workflowDagService.getStep(workflowTaskDefinitionId);
        return WorkflowTaskDefitionConvert.INSTANCE.toDto(step);
    }
}
