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
import cn.sliew.scaleph.dag.service.DagConfigComplexService;
import cn.sliew.scaleph.dag.service.DagConfigLinkService;
import cn.sliew.scaleph.dag.service.DagConfigService;
import cn.sliew.scaleph.dag.service.DagConfigStepService;
import cn.sliew.scaleph.dag.service.dto.DagConfigComplexDTO;
import cn.sliew.scaleph.dag.service.dto.DagConfigDTO;
import cn.sliew.scaleph.dag.service.dto.DagConfigLinkDTO;
import cn.sliew.scaleph.dag.service.dto.DagConfigStepDTO;
import cn.sliew.scaleph.dag.service.param.DagConfigSimpleAddParam;
import cn.sliew.scaleph.dag.service.param.DagConfigSimpleUpdateParam;
import cn.sliew.scaleph.dag.service.vo.DagGraphVO;
import cn.sliew.scaleph.dag.service.vo.EdgeCellVO;
import cn.sliew.scaleph.dag.service.vo.NodeCellVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DagConfigComplexServiceImpl implements DagConfigComplexService {

    @Autowired
    private DagConfigService dagConfigService;
    @Autowired
    private DagConfigLinkService dagConfigLinkService;
    @Autowired
    private DagConfigStepService dagConfigStepService;

    @Override
    public DagConfigComplexDTO selectOne(Long dagId) {
        DagConfigComplexDTO dagConfigComplexDTO = new DagConfigComplexDTO();
        DagConfigDTO configDTO = dagConfigService.selectOne(dagId);
        BeanUtils.copyProperties(configDTO, dagConfigComplexDTO);
        dagConfigComplexDTO.setLinks(dagConfigLinkService.listLinks(dagId));
        dagConfigComplexDTO.setSteps(dagConfigStepService.listSteps(dagId));
        return dagConfigComplexDTO;
    }

    @Override
    public DagConfigDTO selectSimpleOne(Long dagId) {
        return dagConfigService.selectOne(dagId);
    }

    @Override
    public Long insert(DagConfigSimpleAddParam param) {
        DagConfigDTO configDTO = new DagConfigDTO();
        BeanUtils.copyProperties(param, configDTO);
        return dagConfigService.insert(configDTO);
    }

    @Override
    public int update(DagConfigSimpleUpdateParam param) {
        DagConfigDTO configDTO = new DagConfigDTO();
        BeanUtils.copyProperties(param, configDTO);
        return dagConfigService.update(configDTO);
    }

    @Override
    public void replace(Long dagId, DagGraphVO graph) {
        saveSteps(dagId, graph.getNodes());
        saveLinks(dagId, graph.getEdges());
    }

    private void saveSteps(Long dagId, List<NodeCellVO> nodes) {
        List<String> stepIds = nodes.stream().map(NodeCellVO::getId)
                .collect(Collectors.toList());
        dagConfigStepService.deleteSurplusSteps(dagId, stepIds);
        for (NodeCellVO node : nodes) {
            DagConfigStepDTO stepDTO = new DagConfigStepDTO();
            stepDTO.setDagId(dagId);
            stepDTO.setStepId(node.getId());
            if (node.getData() != null) {
                stepDTO.setStepName(node.getData().getLabel());
                stepDTO.setStepMeta(JacksonUtil.toJsonNode(node.getData().getMeta()));
                stepDTO.setStepAttrs(JacksonUtil.toJsonNode(node.getData().getAttrs()));
            }
            stepDTO.setPositionX(node.getPosition().getX());
            stepDTO.setPositionY(node.getPosition().getY());
            dagConfigStepService.upsert(stepDTO);
        }
    }

    private void saveLinks(Long jobId, List<EdgeCellVO> edges) {
        List<String> linkIds = edges.stream().map(EdgeCellVO::getId)
                .collect(Collectors.toList());
        dagConfigLinkService.deleteSurplusLinks(jobId, linkIds);
        for (EdgeCellVO edge : edges) {
            DagConfigLinkDTO linkDTO = new DagConfigLinkDTO();
            linkDTO.setDagId(jobId);
            linkDTO.setLinkId(edge.getId());
            if (edge.getData() != null) {
                linkDTO.setLinkName(edge.getData().getLabel());
                linkDTO.setLinkMeta(JacksonUtil.toJsonNode(edge.getData().getMeta()));
                linkDTO.setLinkAttrs(JacksonUtil.toJsonNode(edge.getData().getAttrs()));
            }
            linkDTO.setFromStepId(edge.getSource().getCell());
            linkDTO.setToStepId(edge.getTarget().getCell());
            dagConfigLinkService.upsert(linkDTO);
        }
    }

    @Override
    public Long clone(Long dagId) {
        Long cloneDagId = dagConfigService.clone(dagId);
        dagConfigStepService.clone(dagId, cloneDagId);
        dagConfigLinkService.clone(dagId, cloneDagId);
        return cloneDagId;
    }

    @Override
    public int delete(Long dagId) {
        dagConfigStepService.deleteByDag(dagId);
        dagConfigLinkService.deleteByDag(dagId);
        return dagConfigService.delete(dagId);
    }

    @Override
    public int deleteBatch(List<Long> dagIds) {
        dagConfigStepService.deleteByDag(dagIds);
        dagConfigLinkService.deleteByDag(dagIds);
        return dagConfigService.deleteBatch(dagIds);
    }
}
