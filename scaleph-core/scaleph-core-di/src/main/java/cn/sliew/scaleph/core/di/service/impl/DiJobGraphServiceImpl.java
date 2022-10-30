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

package cn.sliew.scaleph.core.di.service.impl;

import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginName;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginType;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.core.di.service.DiJobGraphService;
import cn.sliew.scaleph.core.di.service.DiJobLinkService;
import cn.sliew.scaleph.core.di.service.DiJobStepService;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobLinkDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobStepDTO;
import cn.sliew.scaleph.core.di.service.param.DiJobStepParam;
import cn.sliew.scaleph.core.di.service.vo.EdgeCellVO;
import cn.sliew.scaleph.core.di.service.vo.JobGraphVO;
import cn.sliew.scaleph.core.di.service.vo.NodeCellVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiJobGraphServiceImpl implements DiJobGraphService {

    @Autowired
    private DiJobStepService diJobStepService;
    @Autowired
    private DiJobLinkService diJobLinkService;

    @Override
    public void queryJobGraph(DiJobDTO job) {
        job.setJobStepList(diJobStepService.listJobStep(job.getId()));
        job.setJobLinkList(diJobLinkService.listJobLink(job.getId()));
    }

    @Override
    public void saveJobGraph(Long jobId, JobGraphVO jobGraph) {
        saveJobSteps(jobId, jobGraph.getNodes());
        saveJobLinks(jobId, jobGraph.getEdges());
    }

    /**
     * 在新建 job step 时, {@link NodeCellVO#data} 是 {@link cn.sliew.scaleph.core.di.service.vo.DagPanalVO}
     * 在更新 job step 时, {@link NodeCellVO#data} 是 服务端返回的 job step 数据组装的
     */
    private void saveJobSteps(Long jobId, List<NodeCellVO> nodes) {
        List<String> stopCodes = nodes.stream().map(NodeCellVO::getId)
                .collect(Collectors.toList());
        diJobStepService.deleteSurplusStep(jobId, stopCodes);
        for (NodeCellVO node : nodes) {
            DiJobStepDTO jobStep = new DiJobStepDTO();
            jobStep.setJobId(jobId);
            jobStep.setStepCode(node.getId());
            jobStep.setStepTitle(node.getLabel());
            jobStep.setStepType(SeaTunnelPluginType.of(String.valueOf(node.getData().get("type"))));
            jobStep.setStepName(SeaTunnelPluginName.of(String.valueOf(node.getData().get("name"))));
            jobStep.setPositionX(node.getX());
            jobStep.setPositionY(node.getY());
            diJobStepService.upsert(jobStep);
        }
    }

    private void saveJobLinks(Long jobId, List<EdgeCellVO> edges) {
        List<String> linkCodes = edges.stream().map(EdgeCellVO::getId)
                .collect(Collectors.toList());
        diJobLinkService.deleteSurplusLink(jobId, linkCodes);

        for (EdgeCellVO edge : edges) {
            DiJobLinkDTO jobLink = new DiJobLinkDTO();
            jobLink.setJobId(jobId);
            jobLink.setLinkCode(edge.getId());
            jobLink.setFromStepCode(edge.getSource());
            jobLink.setToStepCode(edge.getTarget());
            diJobLinkService.upsert(jobLink);
        }
    }

    @Override
    public void updateJobStep(DiJobStepParam param) {
        DiJobStepDTO dto = BeanUtil.copy(param, new DiJobStepDTO());
        dto.setStepTitle((String) param.getStepAttrs().get(Constants.JOB_STEP_TITLE));
        diJobStepService.update(dto);
    }

    @Override
    public void clone(Long sourceJobId, Long targetJobId) {
        diJobStepService.clone(sourceJobId, targetJobId);
        diJobLinkService.clone(sourceJobId, targetJobId);
    }

    @Override
    public void deleteBatch(List<Long> jobIds) {
        diJobStepService.deleteByJobId(jobIds);
        diJobLinkService.deleteByJobId(jobIds);
    }

    @Override
    public int deleteByProjectId(Collection<Long> projectIds) {
        diJobStepService.deleteByProjectId(projectIds);
        diJobLinkService.deleteByProjectId(projectIds);
        return projectIds.size();
    }
}
