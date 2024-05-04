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

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.workflow.WorkflowInstanceState;
import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskInstanceStage;
import cn.sliew.scaleph.common.util.UUIDUtil;
import cn.sliew.scaleph.dag.service.*;
import cn.sliew.scaleph.dag.service.dto.*;
import cn.sliew.scaleph.dag.service.param.DagSimpleAddParam;
import cn.sliew.scaleph.dag.service.param.DagSimpleUpdateParam;
import cn.sliew.scaleph.dag.service.vo.DagGraphVO;
import cn.sliew.scaleph.dag.service.vo.EdgeCellVO;
import cn.sliew.scaleph.dag.service.vo.NodeCellVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public DagInstanceComplexDTO selectOne(Long id) {
        DagInstanceComplexDTO dagInstanceComplexDTO = new DagInstanceComplexDTO();
        DagInstanceDTO instanceDTO = dagInstanceService.selectOne(id);
        BeanUtils.copyProperties(instanceDTO, dagInstanceComplexDTO);
        dagInstanceComplexDTO.setLinks(dagLinkService.listLinks(id));
        dagInstanceComplexDTO.setSteps(dagStepService.listSteps(id));
        return dagInstanceComplexDTO;
    }

    @Override
    public DagInstanceDTO selectSimpleOne(Long id) {
        return dagInstanceService.selectOne(id);
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
                dagStepDTO.setInputs(dagConfigStepDTO.getStepAttrs());
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
