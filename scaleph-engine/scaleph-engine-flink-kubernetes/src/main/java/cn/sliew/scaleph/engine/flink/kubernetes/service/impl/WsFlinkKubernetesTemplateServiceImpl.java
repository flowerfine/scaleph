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
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkKubernetesTemplate;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkKubernetesTemplateMapper;
import cn.sliew.scaleph.engine.flink.kubernetes.factory.FlinkTemplateFactory;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.util.JsonPatchMerger;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.template.FlinkTemplate;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.template.FlinkTemplateConverter;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesTemplateService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.convert.WsFlinkKubernetesTemplateConvert;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesTemplateDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesTemplateListParam;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class WsFlinkKubernetesTemplateServiceImpl implements WsFlinkKubernetesTemplateService {

    @Autowired
    private WsFlinkKubernetesTemplateMapper wsFlinkKubernetesTemplateMapper;

    @Override
    public Page<WsFlinkKubernetesTemplateDTO> list(WsFlinkKubernetesTemplateListParam param) {
        Page<WsFlinkKubernetesTemplate> page = wsFlinkKubernetesTemplateMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(WsFlinkKubernetesTemplate.class)
                        .eq(WsFlinkKubernetesTemplate::getProjectId, param.getProjectId())
                        .like(StringUtils.hasText(param.getName()), WsFlinkKubernetesTemplate::getName, param.getName()));
        Page<WsFlinkKubernetesTemplateDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<WsFlinkKubernetesTemplateDTO> dtoList = WsFlinkKubernetesTemplateConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public WsFlinkKubernetesTemplateDTO selectOne(Long id) {
        WsFlinkKubernetesTemplate record = wsFlinkKubernetesTemplateMapper.selectById(id);
        return WsFlinkKubernetesTemplateConvert.INSTANCE.toDto(record);
    }

    @Override
    public FlinkTemplate asTemplate(WsFlinkKubernetesTemplateDTO dto) {
        return FlinkTemplateConverter.INSTANCE.convertTo(dto);
    }

    @Override
    public FlinkTemplate asTemplateWithDefault(WsFlinkKubernetesTemplateDTO dto) {
        return FlinkTemplateConverter.INSTANCE.convertTo(mergeDefault(dto));
    }

    @Override
    public WsFlinkKubernetesTemplateDTO mergeDefault(WsFlinkKubernetesTemplateDTO dto) {
        FlinkTemplate customTemplate = FlinkTemplateConverter.INSTANCE.convertTo(dto);
        FlinkTemplate defaultTemplate = FlinkTemplateFactory.create("default", "default", customTemplate);
        return doMergeDefault(FlinkTemplateConverter.INSTANCE.convertFrom(defaultTemplate));
    }

    private WsFlinkKubernetesTemplateDTO doMergeDefault(WsFlinkKubernetesTemplateDTO dto) {
        WsFlinkKubernetesTemplateDTO globalDefault = getGlobalDefault();
        WsFlinkKubernetesTemplateDTO result = new WsFlinkKubernetesTemplateDTO();
        result.setName(dto.getName());
        result.setMetadata(JsonPatchMerger.mergePatch(globalDefault.getMetadata(), dto.getMetadata()));
        result.setSpec(JsonPatchMerger.mergePatch(globalDefault.getSpec(), dto.getSpec()));
        return result;
    }

    private WsFlinkKubernetesTemplateDTO getGlobalDefault() {
        FlinkTemplate template = FlinkTemplateFactory.getDefaults();
        WsFlinkKubernetesTemplateDTO dto = new WsFlinkKubernetesTemplateDTO();
        dto.setName(template.getMetadata().getName());
        dto.setMetadata(JacksonUtil.toJsonNode(template.getMetadata()));
        dto.setSpec(JacksonUtil.toJsonNode(template.getSpec()));
        return dto;
    }

    @Override
    public int insert(WsFlinkKubernetesTemplateDTO dto) {
        WsFlinkKubernetesTemplate record = WsFlinkKubernetesTemplateConvert.INSTANCE.toDo(dto);
        record.setTemplateId(UUID.randomUUID().toString());
        return wsFlinkKubernetesTemplateMapper.insert(record);
    }

    @Override
    public int update(WsFlinkKubernetesTemplateDTO dto) {
        WsFlinkKubernetesTemplate record = WsFlinkKubernetesTemplateConvert.INSTANCE.toDo(dto);
        return wsFlinkKubernetesTemplateMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return wsFlinkKubernetesTemplateMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return wsFlinkKubernetesTemplateMapper.deleteBatchIds(ids);
    }
}
