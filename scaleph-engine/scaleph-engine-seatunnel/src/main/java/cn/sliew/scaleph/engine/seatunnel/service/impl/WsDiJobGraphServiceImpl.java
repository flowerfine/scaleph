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

package cn.sliew.scaleph.engine.seatunnel.service.impl;

import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginName;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginType;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dag.service.vo.DagGraphVO;
import cn.sliew.scaleph.dag.service.vo.EdgeCellVO;
import cn.sliew.scaleph.dag.service.vo.NodeCellVO;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobGraphService;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobLinkService;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobStepService;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobDTO;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobLinkDTO;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobStepDTO;
import cn.sliew.scaleph.engine.seatunnel.service.param.WsDiJobStepParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WsDiJobGraphServiceImpl implements WsDiJobGraphService {

    @Autowired
    private WsDiJobStepService wsDiJobStepService;
    @Autowired
    private WsDiJobLinkService wsDiJobLinkService;

    @Override
    public void queryJobGraph(WsDiJobDTO job) {
        job.setJobStepList(wsDiJobStepService.listJobStep(job.getId()));
        job.setJobLinkList(wsDiJobLinkService.listJobLink(job.getId()));
    }

    @Override
    public void saveJobGraph(Long jobId, DagGraphVO jobGraph) {
        saveJobSteps(jobId, jobGraph.getNodes());
        saveJobLinks(jobId, jobGraph.getEdges());
    }

    private void saveJobSteps(Long jobId, List<NodeCellVO> nodes) {
        List<String> stopCodes = nodes.stream().map(NodeCellVO::getId)
                .collect(Collectors.toList());
        wsDiJobStepService.deleteSurplusStep(jobId, stopCodes);
        for (NodeCellVO node : nodes) {
            WsDiJobStepDTO jobStep = new WsDiJobStepDTO();
            jobStep.setJobId(jobId);
            jobStep.setStepCode(node.getId());
            jobStep.setStepTitle(node.getLabel());
            jobStep.setStepType(SeaTunnelPluginType.of(String.valueOf(node.getData().get("type"))));
            jobStep.setStepName(SeaTunnelPluginName.of(String.valueOf(node.getData().get("name"))));
            jobStep.setPositionX(node.getX());
            jobStep.setPositionY(node.getY());
            wsDiJobStepService.upsert(jobStep);
        }
    }

    private void saveJobLinks(Long jobId, List<EdgeCellVO> edges) {
        List<String> linkCodes = edges.stream().map(EdgeCellVO::getId)
                .collect(Collectors.toList());
        wsDiJobLinkService.deleteSurplusLink(jobId, linkCodes);

        for (EdgeCellVO edge : edges) {
            WsDiJobLinkDTO jobLink = new WsDiJobLinkDTO();
            jobLink.setJobId(jobId);
            jobLink.setLinkCode(edge.getId());
            jobLink.setFromStepCode(edge.getSource());
            jobLink.setToStepCode(edge.getTarget());
            wsDiJobLinkService.upsert(jobLink);
        }
    }

    @Override
    public void updateJobStep(WsDiJobStepParam param) {
        WsDiJobStepDTO dto = BeanUtil.copy(param, new WsDiJobStepDTO());
        dto.setStepTitle((String) param.getStepAttrs().get(Constants.JOB_STEP_TITLE));
        wsDiJobStepService.update(dto);
    }

    @Override
    public void clone(Long sourceJobId, Long targetJobId) {
        wsDiJobStepService.clone(sourceJobId, targetJobId);
        wsDiJobLinkService.clone(sourceJobId, targetJobId);
    }

    @Override
    public void deleteBatch(List<Long> jobIds) {
        wsDiJobStepService.deleteByJobId(jobIds);
        wsDiJobLinkService.deleteByJobId(jobIds);
    }

    @Override
    public int deleteByProjectId(Collection<Long> projectIds) {
        wsDiJobStepService.deleteByProjectId(projectIds);
        wsDiJobLinkService.deleteByProjectId(projectIds);
        return projectIds.size();
    }
}
