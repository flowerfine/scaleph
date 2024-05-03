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
import cn.sliew.scaleph.dag.service.DagInstanceService;
import cn.sliew.scaleph.dag.service.DagLinkService;
import cn.sliew.scaleph.dag.service.DagService;
import cn.sliew.scaleph.dag.service.DagStepService;
import cn.sliew.scaleph.dag.service.dto.DagInstanceComplexDTO;
import cn.sliew.scaleph.dag.service.dto.DagInstanceDTO;
import cn.sliew.scaleph.dag.service.dto.DagLinkDTO;
import cn.sliew.scaleph.dag.service.dto.DagStepDTO;
import cn.sliew.scaleph.dag.service.param.DagSimpleAddParam;
import cn.sliew.scaleph.dag.service.param.DagSimpleUpdateParam;
import cn.sliew.scaleph.dag.service.vo.DagGraphVO;
import cn.sliew.scaleph.dag.service.vo.EdgeCellVO;
import cn.sliew.scaleph.dag.service.vo.NodeCellVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DagServiceImpl implements DagService {

    @Autowired
    private DagInstanceService dagInstanceService;
    @Autowired
    private DagLinkService dagLinkService;
    @Autowired
    private DagStepService dagStepService;

    @Override
    public DagInstanceComplexDTO selectOne(Long dagId) {
        DagInstanceComplexDTO dagInstanceComplexDTO = new DagInstanceComplexDTO();
        DagInstanceDTO instanceDTO = dagInstanceService.selectOne(dagId);
        BeanUtils.copyProperties(instanceDTO, dagInstanceComplexDTO);
        dagInstanceComplexDTO.setLinks(dagLinkService.listLinks(dagId));
        dagInstanceComplexDTO.setSteps(dagStepService.listSteps(dagId));
        return dagInstanceComplexDTO;
    }

    @Override
    public DagInstanceDTO selectSimpleOne(Long dagId) {
        return dagInstanceService.selectOne(dagId);
    }

    @Override
    public Long insert(DagSimpleAddParam param) {
        DagInstanceDTO instanceDTO = new DagInstanceDTO();
        BeanUtils.copyProperties(param, instanceDTO);
        return dagInstanceService.insert(instanceDTO);
    }

    @Override
    public int update(DagSimpleUpdateParam param) {
        DagInstanceDTO instanceDTO = new DagInstanceDTO();
        BeanUtils.copyProperties(param, instanceDTO);
        return dagInstanceService.update(instanceDTO);
    }

    @Override
    public void replace(Long dagId, DagGraphVO graph) {
        saveSteps(dagId, graph.getNodes());
        saveLinks(dagId, graph.getEdges());
    }

    private void saveSteps(Long dagId, List<NodeCellVO> nodes) {
        List<String> stepIds = nodes.stream().map(NodeCellVO::getId)
                .collect(Collectors.toList());
        dagStepService.deleteSurplusSteps(dagId, stepIds);
        for (NodeCellVO node : nodes) {
            DagStepDTO stepDTO = new DagStepDTO();
            stepDTO.setDagId(dagId);
            stepDTO.setStepId(node.getId());
            if (node.getData() != null) {
                stepDTO.setStepName(node.getData().getLabel());
                stepDTO.setStepMeta(JacksonUtil.toJsonNode(node.getData().getMeta()));
                stepDTO.setStepAttrs(JacksonUtil.toJsonNode(node.getData().getAttrs()));
            }
            stepDTO.setPositionX(node.getPosition().getX());
            stepDTO.setPositionY(node.getPosition().getY());
            dagStepService.upsert(stepDTO);
        }
    }

    private void saveLinks(Long jobId, List<EdgeCellVO> edges) {
        List<String> linkIds = edges.stream().map(EdgeCellVO::getId)
                .collect(Collectors.toList());
        dagLinkService.deleteSurplusLinks(jobId, linkIds);
        for (EdgeCellVO edge : edges) {
            DagLinkDTO linkDTO = new DagLinkDTO();
            linkDTO.setDagId(jobId);
            linkDTO.setLinkId(edge.getId());
            if (edge.getData() != null) {
                linkDTO.setLinkName(edge.getData().getLabel());
                linkDTO.setLinkMeta(JacksonUtil.toJsonNode(edge.getData().getMeta()));
                linkDTO.setLinkAttrs(JacksonUtil.toJsonNode(edge.getData().getAttrs()));
            }
            linkDTO.setFromStepId(edge.getSource().getCell());
            linkDTO.setToStepId(edge.getTarget().getCell());
            dagLinkService.upsert(linkDTO);
        }
    }

    @Override
    public Long clone(Long dagId) {
        Long cloneDagId = dagInstanceService.clone(dagId);
        dagLinkService.clone(dagId, cloneDagId);
        dagStepService.clone(dagId, cloneDagId);
        return cloneDagId;
    }

    @Override
    public int delete(Long dagId) {
        dagLinkService.deleteByDag(dagId);
        dagStepService.deleteByDag(dagId);
        return dagInstanceService.delete(dagId);
    }

    @Override
    public int deleteBatch(List<Long> dagIds) {
        dagLinkService.deleteByDag(dagIds);
        dagStepService.deleteByDag(dagIds);
        return dagInstanceService.deleteBatch(dagIds);
    }
}
