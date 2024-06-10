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

package cn.sliew.scaleph.dag.service.impl;

import cn.sliew.scaleph.common.dict.workflow.WorkflowInstanceState;
import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskInstanceStage;
import cn.sliew.scaleph.common.jackson.JsonMerger;
import cn.sliew.scaleph.common.util.UUIDUtil;
import cn.sliew.scaleph.dag.service.*;
import cn.sliew.scaleph.dag.service.dto.*;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DagInstanceComplexServiceImpl implements DagInstanceComplexService {

    @Autowired
    private DagConfigComplexService dagConfigComplexService;
    @Autowired
    private DagInstanceService dagInstanceService;
    @Autowired
    private DagLinkService dagLinkService;
    @Autowired
    private DagStepService dagStepService;

    @Override
    public DagInstanceComplexDTO selectOne(Long dagInstanceId) {
        DagInstanceComplexDTO dagInstanceComplexDTO = new DagInstanceComplexDTO();
        DagInstanceDTO instanceDTO = dagInstanceService.selectOne(dagInstanceId);
        BeanUtils.copyProperties(instanceDTO, dagInstanceComplexDTO);
        dagInstanceComplexDTO.setLinks(dagLinkService.listLinks(dagInstanceId));
        dagInstanceComplexDTO.setSteps(dagStepService.listSteps(dagInstanceId));
        return dagInstanceComplexDTO;
    }

    @Override
    public DagInstanceDTO selectSimpleOne(Long dagInstanceId) {
        return dagInstanceService.selectOne(dagInstanceId);
    }

    @Override
    public Graph<DagStepDTO> getDag(Long dagInstanceId, Graph<DagConfigStepDTO> configDag) {
        List<DagStepDTO> dagStepDTOS = dagStepService.listSteps(dagInstanceId);
        MutableGraph<DagStepDTO> graph = GraphBuilder.directed().build();
        Map<Long, DagStepDTO> stepMap = new HashMap<>();
        for (DagStepDTO dagStepDTO : dagStepDTOS) {
            stepMap.put(dagStepDTO.getDagConfigStep().getId(), dagStepDTO);
            graph.addNode(dagStepDTO);
        }
        for (EndpointPair<DagConfigStepDTO> edge : configDag.edges()) {
            DagConfigStepDTO source = edge.source();
            DagConfigStepDTO target = edge.target();
            graph.putEdge(stepMap.get(source.getId()), stepMap.get(target.getId()));
        }
        return graph;
    }

    @Override
    public Long initialize(Long dagConfigId) {
        DagConfigComplexDTO dagConfigComplexDTO = dagConfigComplexService.selectOne(dagConfigId);
        // 插入 dag_instance
        DagInstanceDTO dagInstanceDTO = new DagInstanceDTO();
        dagInstanceDTO.setDagConfig(dagConfigComplexDTO);
        dagInstanceDTO.setInstanceId(UUIDUtil.randomUUId());
        dagInstanceDTO.setInputs(dagConfigComplexDTO.getDagAttrs());
        dagInstanceDTO.setStatus(WorkflowInstanceState.PENDING.getValue());
        dagInstanceDTO.setStartTime(new Date());
        Long dagInstanceId = dagInstanceService.insert(dagInstanceDTO);
        // 插入 dag_step
        if (CollectionUtils.isEmpty(dagConfigComplexDTO.getSteps()) == false) {
            for (DagConfigStepDTO dagConfigStepDTO : dagConfigComplexDTO.getSteps()) {
                DagStepDTO dagStepDTO = new DagStepDTO();
                dagStepDTO.setDagInstanceId(dagInstanceId);
                dagStepDTO.setDagConfigStep(dagConfigStepDTO);
                dagStepDTO.setInstanceId(UUIDUtil.randomUUId());
                dagStepDTO.setInputs(JsonMerger.doMerge(dagInstanceDTO.getInputs(), dagConfigStepDTO.getStepAttrs()));
                dagStepDTO.setStatus(WorkflowTaskInstanceStage.PENDING.getValue());
                dagStepDTO.setStartTime(new Date());
                dagStepService.insert(dagStepDTO);
            }
        }
        // 插入 dag_link
        if (CollectionUtils.isEmpty(dagConfigComplexDTO.getLinks()) == false) {
            for (DagConfigLinkDTO dagConfigLinkDTO : dagConfigComplexDTO.getLinks()) {
                DagLinkDTO dagLinkDTO = new DagLinkDTO();
                dagLinkDTO.setDagInstanceId(dagInstanceId);
                dagLinkDTO.setDagConfigLink(dagConfigLinkDTO);
                dagLinkDTO.setInstanceId(UUIDUtil.randomUUId());
                dagLinkDTO.setInputs(dagConfigLinkDTO.getLinkAttrs());
                dagLinkDTO.setStatus(WorkflowTaskInstanceStage.PENDING.getValue());
                dagLinkDTO.setStartTime(new Date());
                dagLinkService.insert(dagLinkDTO);
            }
        }
        return dagInstanceId;
    }
}
