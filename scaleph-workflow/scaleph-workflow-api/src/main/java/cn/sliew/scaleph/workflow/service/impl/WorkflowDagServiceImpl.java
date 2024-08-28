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

import cn.sliew.carp.framework.dag.service.DagConfigComplexService;
import cn.sliew.carp.framework.dag.service.DagConfigStepService;
import cn.sliew.carp.framework.dag.service.dto.DagConfigComplexDTO;
import cn.sliew.carp.framework.dag.service.dto.DagConfigStepDTO;
import cn.sliew.carp.framework.dag.service.param.DagConfigSimpleAddParam;
import cn.sliew.carp.framework.dag.x6.graph.DagGraphVO;
import cn.sliew.scaleph.dag.xflow.dnd.DndDTO;
import cn.sliew.scaleph.workflow.service.WorkflowDagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class WorkflowDagServiceImpl implements WorkflowDagService {

    @Autowired
    private DagConfigComplexService dagConfigComplexService;
    @Autowired
    private DagConfigStepService dagConfigStepService;

    @Override
    public Long initialize(String name, String remark) {
        DagConfigSimpleAddParam param = new DagConfigSimpleAddParam();
        param.setType("WorkFlow");
        param.setName(name);
        param.setRemark(remark);
        return dagConfigComplexService.insert(param);
    }

    @Override
    public void destroy(Long dagId) {
        dagConfigComplexService.delete(dagId);
    }

    @Override
    public DagConfigComplexDTO getDag(Long dagId) {
        return dagConfigComplexService.selectOne(dagId);
    }

    @Override
    public DagConfigStepDTO getStep(Long stepId) {
        return dagConfigStepService.get(stepId);
    }

    @Override
    public void update(Long dagId, DagGraphVO graph) {
        dagConfigComplexService.replace(dagId, graph);
    }

    @Override
    public List<DndDTO> getDnds() {
        return Collections.emptyList();
    }
}
