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

import cn.sliew.scaleph.dag.service.DagService;
import cn.sliew.scaleph.dag.service.DagStepService;
import cn.sliew.scaleph.dag.service.dto.DagDTO;
import cn.sliew.scaleph.dag.service.dto.DagStepDTO;
import cn.sliew.scaleph.dag.service.param.DagSimpleAddParam;
import cn.sliew.scaleph.dag.service.vo.DagGraphVO;
import cn.sliew.scaleph.dag.xflow.dnd.DndDTO;
import cn.sliew.scaleph.workflow.service.WorkflowDagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class WorkflowDagServiceImpl implements WorkflowDagService {

    @Autowired
    private DagService dagService;
    @Autowired
    private DagStepService dagStepService;

    @Override
    public Long initialize() {
        return dagService.insert(new DagSimpleAddParam());
    }

    @Override
    public void destroy(Long dagId) {
        dagService.delete(dagId);
    }

    @Override
    public DagDTO getDag(Long dagId) {
        return dagService.selectOne(dagId);
    }

    @Override
    public DagStepDTO getStep(Long stepId) {
        return dagStepService.selectOne(stepId);
    }

    @Override
    public void update(Long dagId, DagGraphVO graph) {
        dagService.replace(dagId, graph);
    }

    @Override
    public List<DndDTO> getDnds() {
        return Collections.emptyList();
    }
}
