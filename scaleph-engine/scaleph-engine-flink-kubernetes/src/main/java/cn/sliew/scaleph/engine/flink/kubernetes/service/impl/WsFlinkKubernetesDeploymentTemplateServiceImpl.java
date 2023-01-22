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

package cn.sliew.scaleph.engine.flink.kubernetes.service.impl;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkKubernetesDeploymentTemplate;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkKubernetesDeploymentTemplateMapper;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.entity.DeploymentTemplate;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.factory.DeploymentTemplateFactory;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.util.JsonPatchMerger;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesDeploymentTemplateService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.convert.WsFlinkKubernetesDeploymentTemplateConvert;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesDeploymentTemplateDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesDeploymentTemplateListParam;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class WsFlinkKubernetesDeploymentTemplateServiceImpl implements WsFlinkKubernetesDeploymentTemplateService {

    @Autowired
    private WsFlinkKubernetesDeploymentTemplateMapper wsFlinkKubernetesDeploymentTemplateMapper;

    @Override
    public Page<WsFlinkKubernetesDeploymentTemplateDTO> list(WsFlinkKubernetesDeploymentTemplateListParam param) {
        Page<WsFlinkKubernetesDeploymentTemplate> page = wsFlinkKubernetesDeploymentTemplateMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(WsFlinkKubernetesDeploymentTemplate.class)
                        .like(StringUtils.hasText(param.getName()), WsFlinkKubernetesDeploymentTemplate::getName, param.getName()));
        Page<WsFlinkKubernetesDeploymentTemplateDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<WsFlinkKubernetesDeploymentTemplateDTO> dtoList = WsFlinkKubernetesDeploymentTemplateConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public WsFlinkKubernetesDeploymentTemplateDTO selectOne(Long id) {
        WsFlinkKubernetesDeploymentTemplate record = wsFlinkKubernetesDeploymentTemplateMapper.selectById(id);
        return WsFlinkKubernetesDeploymentTemplateConvert.INSTANCE.toDto(record);
    }

    @Override
    public WsFlinkKubernetesDeploymentTemplateDTO mergeDefault(WsFlinkKubernetesDeploymentTemplateDTO dto) {
        DeploymentTemplate customTemplate = DeploymentTemplateFactory.from(dto);
        DeploymentTemplate defaultTemplate = DeploymentTemplateFactory.create("default", "default", customTemplate);
        return doMergeDefault(DeploymentTemplateFactory.to(defaultTemplate));
    }

    private WsFlinkKubernetesDeploymentTemplateDTO doMergeDefault(WsFlinkKubernetesDeploymentTemplateDTO dto) {
        WsFlinkKubernetesDeploymentTemplateDTO globalDefault = getGlobalDefault();
        WsFlinkKubernetesDeploymentTemplateDTO result = new WsFlinkKubernetesDeploymentTemplateDTO();
        result.setName(dto.getName());
        result.setMetadata(JsonPatchMerger.mergePatch(globalDefault.getMetadata(), dto.getMetadata()));
        result.setSpec(JsonPatchMerger.mergePatch(globalDefault.getSpec(), dto.getSpec()));
        return result;
    }

    private WsFlinkKubernetesDeploymentTemplateDTO getGlobalDefault() {
        DeploymentTemplate template = DeploymentTemplateFactory.getGlobal();
        WsFlinkKubernetesDeploymentTemplateDTO dto = new WsFlinkKubernetesDeploymentTemplateDTO();
        dto.setName(template.getMetadata().getName());
        dto.setMetadata(JacksonUtil.toJsonNode(template.getMetadata()));
        dto.setSpec(JacksonUtil.toJsonNode(template.getSpec()));
        return dto;
    }

    @Override
    public int insert(WsFlinkKubernetesDeploymentTemplateDTO dto) {
        WsFlinkKubernetesDeploymentTemplate record = WsFlinkKubernetesDeploymentTemplateConvert.INSTANCE.toDo(dto);
        return wsFlinkKubernetesDeploymentTemplateMapper.insert(record);
    }

    @Override
    public int update(WsFlinkKubernetesDeploymentTemplateDTO dto) {
        WsFlinkKubernetesDeploymentTemplate record = WsFlinkKubernetesDeploymentTemplateConvert.INSTANCE.toDo(dto);
        return wsFlinkKubernetesDeploymentTemplateMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return wsFlinkKubernetesDeploymentTemplateMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return wsFlinkKubernetesDeploymentTemplateMapper.deleteBatchIds(ids);
    }
}
